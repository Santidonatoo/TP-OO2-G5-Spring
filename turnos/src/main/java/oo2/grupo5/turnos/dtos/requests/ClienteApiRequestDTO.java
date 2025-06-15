package oo2.grupo5.turnos.dtos.requests;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record ClienteApiRequestDTO (
		
		@NotBlank(message = "El nombre es obligatorio")
		@JsonProperty("nombre")
		String nombre,
		
		@NotBlank(message = "El apellido es obligatorio")
		@JsonProperty("apellido")
		String apellido,
		
		@NotBlank(message = "El DNI es obligatorio")
		@JsonProperty("dni")
		String dni,
		
		@Email(message = "Email debe tener formato valido")
		@JsonProperty("email")
		String email,
		
		@JsonProperty("telefono")
		String telefono,
		
		@NotBlank(message = "El username es obligatorio")
		@JsonProperty("username")
		String username,
		
		@NotBlank(message = "La password es obligatoria")
		@JsonProperty("password")
		String password,
		
		@JsonProperty("ultimoInicioSesion")
		LocalDateTime ultimoInicioSesion
		) {
	@JsonCreator
	public ClienteApiRequestDTO(
			@JsonProperty("nombre") String nombre,
			@JsonProperty("apellido") String apellido,
			@JsonProperty("dni") String dni,
			@JsonProperty("email") String email,
			@JsonProperty("telefono") String telefono,
			@JsonProperty("username") String username,
			@JsonProperty("password") String password,
			@JsonProperty("ultimoInicioSesion") LocalDateTime ultimoInicioSesion) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.email = email;
		this.telefono = telefono;
		this.username = username;
		this.password = password;
		this.ultimoInicioSesion = ultimoInicioSesion;
	}	
}



