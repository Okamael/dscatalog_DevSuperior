package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.tests.Factory;

@DataJpaTest
public class ProductRepositoryTest {
	
	@Autowired
	private ProductRepository repository;
	
	
	private Long idExistente;
	private Long idNaoExistente;
	private long contaTotalProduto;
	
	@BeforeEach
	void init() {
		idExistente = 1l;
		idNaoExistente =  1000l;
		contaTotalProduto = 25;
		
		
	}
	
	@Test
	public void deveDeteletarObjetoComIDExistente() {
		
		repository.deleteById(idExistente);
		Optional<Product> result =  repository.findById(idExistente);
		Assertions.assertFalse(result.isPresent());
	}
	
	
	@Test
	public void deveLancarThrowEmptyResultDataAccessExceptionQuandoIdNaoExistir() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () ->{repository.deleteById(idNaoExistente);});
	}
	
	@Test
	public void devSalvarObjetoComAutoIncremente() {
		
		Product product = Factory.createProduct();
		product .setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(contaTotalProduto+1, product.getId());
	}
	
	
	@Test
	public void deveBuscarPortIDComSucesso() {
		Optional<Product> entity = repository.findById(idExistente);
		Assertions.assertTrue(entity.isPresent());
	}
	
	@Test
	public void deveRetornarOptionalVazioComIdNaoExistente() {
		Optional<Product> entity = repository.findById(idNaoExistente);
		Assertions.assertTrue(entity.isEmpty());
	}
	
}
