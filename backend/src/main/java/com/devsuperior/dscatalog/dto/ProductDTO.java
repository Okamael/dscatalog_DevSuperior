package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ProductDTO implements Serializable {

	private static final long serialVersionUID = -9035476276250408464L;
	
	private Long id;
	
	@Size(min = 5, max = 60, message = "Deve ter entre 5 a 60 caracteres")
	@NotBlank(message = "Campo Requirido")
	private String name;
	@NotBlank(message = "Campo Requirido")
	private String description;
	
	@Positive(message = "O preço deve ter valor so pode ser positivo")
	private Double price;
	private String imgUrl;
	
	@PastOrPresent(message = "A data do produto não pode ser futura")
	private Instant date;
	
	private List<CategoryDTO> categories = new ArrayList<>();
	
	public ProductDTO(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price=entity.getPrice();
		this.imgUrl=entity.getImgUrl();
		this.date=entity.getDate();
	}
	
	public ProductDTO(Product entity , Set<Category> categories) {
		this(entity);
		categories.forEach(categoria -> this.categories.add(new CategoryDTO(categoria)));
	}

}
