package com.andresa.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andresa.cursomc.domain.Cidade;
import com.andresa.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repo;
	
	public List<Cidade> find(Integer idEstado) {
		
		return repo.findCidades(idEstado);
		
	}
	
}
