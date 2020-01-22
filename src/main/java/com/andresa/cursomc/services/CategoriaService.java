package com.andresa.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.andresa.cursomc.domain.Categoria;
import com.andresa.cursomc.dto.CategoriaDTO;
import com.andresa.cursomc.repositories.CategoriaRepository;
import com.andresa.cursomc.services.exceptions.DataIntegrityException;
import com.andresa.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName(), null));
		
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage, Direction.valueOf(direction),	orderBy);
		return repo.findAll(pageRequest);
	}
	
	public List<Categoria> findAll() {
		return repo.findAll();
		
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return (Categoria) repo.saveAndFlush(obj);
	}
	
		
	public Categoria update(Categoria obj) {
		Categoria newObj = this.find(obj.getId());
		updateData(newObj, obj);
		return (Categoria) repo.saveAndFlush(newObj);
	}
	
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}
	
	public void delete(Integer id) {
		this.find(id);
		try {
			repo.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos.");
		}
	}
	
	public Categoria fromDTO(CategoriaDTO objDTO) {
		return new Categoria(objDTO.getId(), objDTO.getNome());
	}
}
