package com.andresa.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.andresa.cursomc.domain.Cidade;

public class CidadeDTO  implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer idEstado;
	
	@NotEmpty(message = "Preenchimento obrigatório.")
	@Length(min = 5, max=120, message = "O tamanho deve ser entre 5 e 120 caracteres")
	private String nome;
	
	public CidadeDTO() {
	}
	
	public CidadeDTO(Cidade obj) {
		this.id = obj.getId();
		this.nome = obj.getNome();
		this.idEstado = obj.getEstado().getId();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

}
