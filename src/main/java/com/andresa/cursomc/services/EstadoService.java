package com.andresa.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andresa.cursomc.domain.Estado;
import com.andresa.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository repo;
	
	public List<Estado> find() {
		return repo.findAllByOrderByNome();
		
	}
}
