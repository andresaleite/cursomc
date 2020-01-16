package com.andresa.cursomc.services.validations;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.andresa.cursomc.domain.Cliente;
import com.andresa.cursomc.domain.enums.TipoCliente;
import com.andresa.cursomc.dto.ClienteNewDTO;
import com.andresa.cursomc.repositories.ClienteRepository;
import com.andresa.cursomc.resources.exceptions.FieldMessage;
import com.andresa.cursomc.services.validations.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	 public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}else if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if(aux != null) {
			list.add(new FieldMessage("email", "O e-mail já foi cadastrado."));
		}
		
		for (FieldMessage e : list) {
			 context.disableDefaultConstraintViolation();
			 context.buildConstraintViolationWithTemplate(e.getMessage())
			 .addPropertyNode(e.getFieldName()).addConstraintViolation();
		 }
		 return list.isEmpty();
	}
}