package com.andresa.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andresa.cursomc.domain.Categoria;
import com.andresa.cursomc.exceptions.ObjectNotFoundException;
import com.andresa.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName(), null));
		
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return (Categoria) repo.saveAndFlush(obj);
	}
	
	public Categoria update(Categoria obj) {
		this.find(obj.getId());
		return (Categoria) repo.saveAndFlush(obj);
	}
}
