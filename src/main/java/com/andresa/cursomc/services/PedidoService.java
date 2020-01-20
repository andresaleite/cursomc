package com.andresa.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andresa.cursomc.domain.ItemPedido;
import com.andresa.cursomc.domain.PagamentoComBoleto;
import com.andresa.cursomc.domain.Pedido;
import com.andresa.cursomc.domain.Produto;
import com.andresa.cursomc.domain.enums.EstadoPagamento;
import com.andresa.cursomc.repositories.ItemPedidoRepository;
import com.andresa.cursomc.repositories.PagamentoRepository;
import com.andresa.cursomc.repositories.PedidoRepository;
import com.andresa.cursomc.servicies.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private PagamentoRepository pagamentoRepo;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName(), null));
		
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pgto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pgto, obj.getInstante());
		}
		obj = repo.saveAndFlush(obj);
		pagamentoRepo.saveAndFlush(obj.getPagamento());
		
		for(ItemPedido i : obj.getItens()) {
			i.setProduto(produtoService.find(i.getProduto().getId()));
			i.setDesconto(0.00);
			i.setPreco(i.getProduto().getPreco());
			i.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	

	
	
}
