package com.andresa.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andresa.cursomc.domain.Pagamento;
import com.andresa.cursomc.domain.Produto;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer>{

}
