package com.andresa.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.andresa.cursomc.domain.Cliente;
import com.andresa.cursomc.repositories.ClienteRepository;
import com.andresa.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if(cliente == null) {
			throw new ObjectNotFoundException("E-mail não encontrado");
		}
		
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));
		clienteRepository.saveAndFlush(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for(int i = 0; i<10; i++) {
			vet[i] = randomChar();
		}
		
		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);
		
		switch (opt) {
		case 0://numeros
			return (char) (rand.nextInt(10) + 48);
		case 1://letras maiúsculas
			return (char) (rand.nextInt(26) + 65);
		default://letras minúsculas
			return (char) (rand.nextInt(26) + 97);
		
		}
	}

}
