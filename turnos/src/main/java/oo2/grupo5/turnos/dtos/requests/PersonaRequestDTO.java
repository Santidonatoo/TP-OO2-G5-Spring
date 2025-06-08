package oo2.grupo5.turnos.dtos.requests;

import java.time.LocalDate;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PersonaRequestDTO {
	
	private Integer idPersona;
	
	@NotBlank(message = "The name cannot be empty")
	@Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
	private String nombre;
	
	@NotBlank(message = "The name cannot be empty")
	@Size(min = 3, max = 50, message = "El apellido debe tener entre 3 y 50 caracteres")
	private String apellido;
	
	@Min(value = 1000000, message = "El DNI debe tener al menos 7 dígitos")
	@Max(value = 99999999, message = "El DNI no puede tener más de 8 dígitos")
	private int dni;
	
	@NotNull(message = "La fecha de nacimiento no puede ser nula")
	@Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
	private LocalDate fechaDeNacimiento;
	
	@Valid
	@NotNull(message = "Contacto cannot be null")
	private ContactoRequestDTO contacto;
}
