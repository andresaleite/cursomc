package com.andresa.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.andresa.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {

	
	public BoletoService() {
		
	}
	
	/**
	 * Provisório, teria que pegar a data de vencimento do webservice do boleto
	 * @param pgto
	 * @param instanteDoPedido
	 */
	public void preencherPagamentoComBoleto(PagamentoComBoleto pgto, Date instanteDoPedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pgto.setDataPagamento(cal.getTime());
	}

}
