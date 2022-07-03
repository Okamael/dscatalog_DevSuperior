package com.devsuperior.dscatalog.resources.exceptions;

import java.io.Serializable;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class StandardError implements Serializable{
	

	private static final long serialVersionUID = 8050750075375827193L;
	private Instant timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
}
