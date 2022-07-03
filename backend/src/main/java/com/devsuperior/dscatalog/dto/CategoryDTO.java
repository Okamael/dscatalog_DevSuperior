package com.devsuperior.dscatalog.dto;

import java.io.Serializable;

import com.devsuperior.dscatalog.entities.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO implements Serializable{
	
	private static final long serialVersionUID = -1351535197279998260L;
	private Long id;
	private String name;
	
	public CategoryDTO(Category entity) {
		this.id  =  entity.getId();
		this.name = entity.getName();
	}
	
}
