package oo2.grupo5.turnos.dtos.requests;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@SuperBuilder
public class EmpleadoRequestDTO extends PersonaRequestDTO {
	
	@NotBlank(message="The position cannot be empty")
	private String puesto;
	
	private Set<Integer> idServicios;
}
