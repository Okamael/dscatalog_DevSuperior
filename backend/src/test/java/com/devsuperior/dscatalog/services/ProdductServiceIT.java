package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProdductServiceIT {
	
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existId;
	private Long idNaoExistente;
	private Long totaldeAProdutos;
	
	@BeforeEach
	public void init() {
		
		existId = 1L;
		idNaoExistente = 1000l;
		totaldeAProdutos = 25l;
	}
	
	
	@Test
	public void deleteDevDeletarQuandoIdExistir() {
		
		service.delete(existId);
		Assertions.assertEquals(totaldeAProdutos-1, repository.count());
	}
	
	@Test
	public void deleteDevRetornarResourceNotFoundExceptioQuandoIdNaoExiste() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,() -> {
			service.delete(idNaoExistente);
		});
		
	}
	
	@Test
	public void findallPagedDeveRetornarPage0Tamanho10() {
		PageRequest page = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(totaldeAProdutos, result.getTotalElements());
	}
	
	@Test
	public void findallPagedDeveRetornarEmpytPageQuandoPaginaNaoExistir() {
		PageRequest page = PageRequest.of(50, 10);
		Page<ProductDTO> result = service.findAllPaged(page);
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findallPagedDeveRetornartPageOrdenadaQuandoOrdenadaPeloNome() {
		PageRequest page = PageRequest.of(0, 10, Sort.by("name"));
		Page<ProductDTO> result = service.findAllPaged(page);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}
}
