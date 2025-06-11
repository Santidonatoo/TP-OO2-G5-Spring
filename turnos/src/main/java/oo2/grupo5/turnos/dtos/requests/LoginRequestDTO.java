package oo2.grupo5.turnos.dtos.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
		
		@NotBlank(message = "El username no puede estar vacio")
		@JsonProperty("username")
		String username, 
		
		@NotBlank(message = "La password no puede estar vacia")
		@JsonProperty("password")
		String password) {	
	
	@JsonCreator
	public LoginRequestDTO(
			@JsonProperty("username") String username,
			@JsonProperty("password") String password) {
		this.username = username;
		this.password = password;
	}
}
