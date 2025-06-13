package oo2.grupo5.turnos.dtos.requests;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmpleadoApiRequestDTO(
		
		@NotBlank(message = "El nombre es obligatorio")
		String nombre,
		
		@NotBlank(message = "El apellido es obligatorio")
		String apellido,
		
		@NotBlank(message = "El DNI es obligatorio")
		String dni,
		
		@Email(message = "Email debe tener formato valido")
		String email,
		
		String telefono,
		
		@NotBlank(message = "El username es obligatorio")
		String username,
		
		@NotBlank(message = "La password es obligatoria")
		String password,
		
		String puesto,
		
		Set<Integer> idServicios
		) {}
