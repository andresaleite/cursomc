package com.andresa.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.andresa.cursomc.services.DBService;

@Configuration
@Profile("teste")
public class TesteConfig {

	@Autowired
	private DBService dbservice;

	@Bean
	public boolean instantiateDatabase() throws ParseException {
		dbservice.instantiateTestDatabase();
		return true;
	}

}
