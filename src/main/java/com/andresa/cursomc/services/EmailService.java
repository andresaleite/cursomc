package com.andresa.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.andresa.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);
}
