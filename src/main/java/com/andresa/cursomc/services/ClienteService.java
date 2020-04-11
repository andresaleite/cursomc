package com.andresa.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Value("${img.profile.size}")
	private Integer size;

	public Cliente find(Integer id) {
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName(), null));
		
	}
	
	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}
	
		Cliente obj = repo.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
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
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);
		
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage,"jpg"), fileName, "image");
	}
}
