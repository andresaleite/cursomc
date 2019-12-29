package com.andresa.cursomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.andresa.cursomc.domain.Categoria;
import com.andresa.cursomc.domain.Cidade;
import com.andresa.cursomc.domain.Cliente;
import com.andresa.cursomc.domain.Endereco;
import com.andresa.cursomc.domain.Estado;
import com.andresa.cursomc.domain.Produto;
import com.andresa.cursomc.domain.enums.TipoCliente;
import com.andresa.cursomc.repositories.CategoriaRepository;
import com.andresa.cursomc.repositories.CidadeRepository;
import com.andresa.cursomc.repositories.ClienteRepository;
import com.andresa.cursomc.repositories.EnderecoRepository;
import com.andresa.cursomc.repositories.EstadoRepository;
import com.andresa.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepo;
	@Autowired
	private ProdutoRepository produtoRepo;
	@Autowired
	private CidadeRepository cidadeRepo;	
	@Autowired
	private EstadoRepository estadoRepo;
	@Autowired
	private EnderecoRepository enderecoRepo;
	@Autowired
	private ClienteRepository clienteRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		
		Produto p1 = new Produto(null, "Computador", 2600.00);
		Produto p2 = new Produto(null, "Impressora", 550.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		
		categoriaRepo.saveAll(Arrays.asList(cat1, cat2));
		produtoRepo.saveAll(Arrays.asList(p1, p2, p3));

		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est2);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est1);
		
		est1.getCidades().addAll(Arrays.asList(c3));
		est2.getCidades().addAll(Arrays.asList(c1, c2));

		estadoRepo.saveAll(Arrays.asList(est1, est2));
		cidadeRepo.saveAll(Arrays.asList(c1, c2, c3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("55123456","661234566","551354544"));
		
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 203", "Jardim", "33225511", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "5588888", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		clienteRepo.saveAll(Arrays.asList(cli1));
		enderecoRepo.saveAll(Arrays.asList(e1, e2));
		
		
	}

}
