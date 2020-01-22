package com.andresa.cursomc.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andresa.cursomc.domain.Cidade;
import com.andresa.cursomc.domain.Cliente;
import com.andresa.cursomc.domain.Endereco;
import com.andresa.cursomc.domain.enums.Perfil;
import com.andresa.cursomc.domain.enums.TipoCliente;
import com.andresa.cursomc.dto.ClienteDTO;
import com.andresa.cursomc.dto.ClienteNewDTO;
import com.andresa.cursomc.repositories.ClienteRepository;
import com.andresa.cursomc.repositories.EnderecoRepository;
import com.andresa.cursomc.security.UserSS;
import com.andresa.cursomc.services.exceptions.AuthorizationException;
import com.andresa.cursomc.services.exceptions.DataIntegrityException;
import com.andresa.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository repo;
	@Autowired
	private EnderecoRepository enderecoRepo;

	public Cliente find(Integer id) {
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName(), null));
		
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage, Direction.valueOf(direction),	orderBy);
		return repo.findAll(pageRequest);
	}
	
	public List<Cliente> findAll() {
		return repo.findAll();
		
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.saveAndFlush(obj);
		enderecoRepo.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = this.find(obj.getId());
		updateData(newObj, obj);
		return (Cliente) repo.saveAndFlush(newObj);
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public void delete(Integer id) {
		this.find(id);
		try {
			repo.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir pois há entidades relacionadas.");
		}
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null); 
	}
	
	public Cliente fromNewDTO(ClienteNewDTO objNewDTO) {
		Cliente cli 	= new Cliente(null, objNewDTO.getNome(), objNewDTO.getEmail(), objNewDTO.getCpfOuCnpj(), TipoCliente.toEnum(objNewDTO.getTipo()), pe.encode(objNewDTO.getSenha()));
		Cidade cid		= new Cidade(objNewDTO.getCidadeId(), null, null);
		Endereco end 	= new Endereco(null, objNewDTO.getLogradouro(), 
						objNewDTO.getNumero(), objNewDTO.getComplemento(), 
						objNewDTO.getBairro(), objNewDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().addAll(Arrays.asList(objNewDTO.getTelefone1(), objNewDTO.getTelefone2(), objNewDTO.getTelefone3()));
		
		return cli;
	}
}
