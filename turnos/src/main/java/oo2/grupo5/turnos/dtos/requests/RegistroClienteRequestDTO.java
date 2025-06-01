package oo2.grupo5.turnos.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroClienteRequestDTO {

	@NotBlank(message="El nombre no puede estar vacio")
	private String nombre;
	
	@NotBlank(message="El apellido no puede estar vacio")
	private String apellido;
	
	@NotNull(message="El DNI no puede estar vacio")
	private int dni;
	
	@NotNull(message="La fecha de nacimiento no puede ser nula")
	private LocalDate fechaDeNacimiento;
	
	@NotBlank(message="El nombre de usuario no puede estar vacio")
	private String username;
	
	@NotBlank(message="La contraseña no puede estar vacia")
	private String password;
	
	@Valid
	@NotNull(message = "Contacto cannot be null")
	private ContactoRequestDTO contacto;
}
