package com.devsuperior.dscatalog.dto;

public class UserInsetDTO extends UserDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 49838721800303985L;
	private String password;

	public UserInsetDTO() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
