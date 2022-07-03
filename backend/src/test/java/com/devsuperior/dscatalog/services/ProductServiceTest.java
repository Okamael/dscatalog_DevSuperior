package com.devsuperior.dscatalog.services;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.repositories.tests.Factory;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoriyRepository;
	
	
	private Long idExistente;
	private Long idNaoExistente;
	private Long idDependente;
	private PageImpl<Product> page;
	private Product produto;
	private ProductDTO produtoDTO;
	private Long idCategoryExistente;
	private Category categoria;
	@BeforeEach
	public void init() {
		idExistente = 1l;
		idNaoExistente = 2l;
		idDependente = 3l;
		produto = Factory.createProduct();
		page = new PageImpl<Product>(List.of(produto));
		produtoDTO = Factory.createProductDTO();
		categoria = new Category(2L, "Eletronics");
		idCategoryExistente = 5l;
		Mockito.when(repository.findAll((Pageable) Mockito.any())).thenReturn(page);
		
		Mockito.when(repository.save(Mockito.any())).thenReturn(produto);
		
		Mockito.when(repository.findById(idExistente)).thenReturn(Optional.of(produto));
		
		Mockito.when(repository.findById(idNaoExistente)).thenReturn(Optional.empty());
		
		when(categoriyRepository.findById(idCategoryExistente)).thenReturn(Optional.of(categoria));
		
		Mockito.doThrow(EntityNotFoundException.class).when(repository).getById(idNaoExistente);
		
		when(repository.getById(idExistente)).thenReturn(produto);
		
		
		Mockito.doNothing().when(repository).deleteById(idExistente);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(idNaoExistente);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(idDependente);
		
	}
	
	@Test
	public void deveDeletarComIdExistente() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(idExistente);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(idExistente);
	}
	
	@Test
	public void deveRetornaDataBaseExceptionQuandoIdDependente() {
		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(idDependente);
		} );
		Mockito.verify(repository, times(1)).deleteById(idDependente);
	}
	
	@Test
	public void deveRetornaResourceNotFoundExceptionQuandoIdNaoExiste() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(idNaoExistente);
		} );
		Mockito.verify(repository, times(1)).deleteById(idNaoExistente);
	}
	
	
	@Test
	public void deveRetornarPageDeProdutos() {
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		verify(repository, times(1)).findAll(pageable);
	}
	
	@Test
	public void deveRetornarProductDTOComIdExistente() {
		
		ProductDTO result = service.findById(idExistente);
		Assertions.assertEquals(idExistente, result.getId());
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void deveResourceNotFoundExceptionComIdNaoExistente() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(idNaoExistente);
		});
		verify(repository, times(1)).findById(idNaoExistente);
		
	}
	
	@Test
	public void deveInserirProduto() {
		
		produtoDTO.setId(null);
		
		produtoDTO = service.insert(produtoDTO);
		
		Assertions.assertEquals(idExistente, produtoDTO.getId());
		
	}
	
	@Test
	public void deveRetornarResourceNotFoundExceptionParaIdNaoExistente() {
		Assertions.assertThrows(ResourceNotFoundException.class, () ->{
			produtoDTO.setId(idNaoExistente);
			service.update(idNaoExistente, produtoDTO);
		});
		verify(repository, times(1)).getById(idNaoExistente);
	}
	
	@Test
	public void deveRetornarProductDtoComNomeAlterado() {
		
		produtoDTO.setName("Abofete");
		produtoDTO = service.update(idExistente, produtoDTO);
		
		Assertions.assertEquals("Abofete", produtoDTO.getName());
	}
}
