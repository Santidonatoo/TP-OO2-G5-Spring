package oo2.grupo5.turnos.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
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
	private String nombre;
	
	@NotBlank(message = "The name cannot be empty")
	private String apellido;
	
	private int dni;
	
	private LocalDate fechaDeNacimiento;
}
