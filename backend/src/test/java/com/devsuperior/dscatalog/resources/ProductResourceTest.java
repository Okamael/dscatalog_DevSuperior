package com.devsuperior.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.tests.Factory;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper mapper;
	
	private PageImpl<ProductDTO> page;
	private ProductDTO productDTO;
	private Long idExistente;
	private Long idNaoExistente;
	private Long dependentId;
	
	@BeforeEach
	void init() throws Exception{
		idExistente = 1l;
		idNaoExistente = 2l;
		dependentId=3l;
		
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		
		when(service.findAllPaged(any())).thenReturn(page);
		
		when(service.findById(idExistente)).thenReturn(productDTO);
		when(service.findById(idNaoExistente)).thenThrow(ResourceNotFoundException.class);
		
		when(service.update(eq(idExistente),any())).thenReturn(productDTO);
		when(service.update(eq(idNaoExistente),any())).thenThrow(ResourceNotFoundException.class);
		
		
		when(service.insert(any())).thenReturn(productDTO);
		
		doNothing().when(service).delete(idExistente);
		doThrow(ResourceNotFoundException.class).when(service).delete(idNaoExistente);		
		doThrow(DataBaseException.class).when(service).delete(dependentId);
		
	}
	
	@Test
	public void findAllDeveRetornarPaged() throws Exception {
		mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void findByIdDeveRetornarIdExistente() throws Exception{
		ResultActions  reuslt = mockMvc.perform(get("/products/{id}", idExistente)
				.accept(MediaType.APPLICATION_JSON));

		reuslt.andExpect(status().isOk());
		reuslt.andExpect(jsonPath("$.id").exists());
		reuslt.andExpect(jsonPath("$.name").exists());
		
		
	}  
	

	@Test
	public void findByIdDeveNotFoundIdNaoExistente() throws Exception{
		ResultActions  reuslt = mockMvc.perform(get("/products/{id}", idNaoExistente)
				.accept(MediaType.APPLICATION_JSON));
		reuslt.andExpect(status().isNotFound());
	} 
	
	@Test
	public void  updateProductDTOQuandoIdExistente() throws Exception {
		String jsonBody = mapper.writeValueAsString(productDTO);
		
		ResultActions  reuslt = mockMvc.perform(put("/products/{id}", idExistente)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON));
		
		reuslt.andExpect(status().isOk());
		reuslt.andExpect(jsonPath("$.id").exists());
		reuslt.andExpect(jsonPath("$.name").exists());
	}
	
	@Test
	public void updateDeveResourceNotFoundExceptionIdNaoExistente  () throws Exception{
	String jsonBody = mapper.writeValueAsString(productDTO);
		
		ResultActions  reuslt = mockMvc.perform(put("/products/{id}", idNaoExistente)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON));
		reuslt.andExpect(status().isNotFound());
	}
	
	@Test
	public void insertDeveCriarUmProdutoDTO() throws Exception{
		
		String jsonBody = mapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(post("/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
	}
	
	@Test
	public void deletenaoDeveFazerNadaAoDeletarIdExistente() throws Exception {
		
		ResultActions  result = mockMvc.perform(delete("/products/{id}", idExistente));
		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteDeveRetornarEmptyResultDataAccessExceptionIDNaoExistente() throws Exception{
		ResultActions result = mockMvc.perform(delete("/products/{id}", idNaoExistente));
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteDeveRetornarDataBaseExceptionQuandoIdDependente() throws Exception{
		ResultActions result = mockMvc.perform(delete("/products/{id}", dependentId));
		result.andExpect(status().isBadRequest());
	}
}
