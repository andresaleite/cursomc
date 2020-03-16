package com.andresa.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.andresa.cursomc.domain.Cliente;
import com.andresa.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);
	/*void sendOrderConfirmationHtmlEmail(Pedido obj);
	
	void sendHtmlEmail(MimeMessage msg); */
}
