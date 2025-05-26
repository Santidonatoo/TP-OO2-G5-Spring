package oo2.grupo5.turnos.dtos.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaResponseDTO {
	private Integer idPersona;
	
	private String nombre;
	
	private String apellido;
	
	private LocalDate fechaDeNacimiento;
	
	private boolean softDeleted;
}
