package oo2.grupo5.turnos.dtos.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@SuperBuilder
public class EmpleadoResponseDTO extends PersonaResponseDTO {
	
	private String puesto;
}
