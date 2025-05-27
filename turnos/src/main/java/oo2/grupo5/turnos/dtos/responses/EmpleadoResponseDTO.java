package oo2.grupo5.turnos.dtos.responses;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@SuperBuilder
public class EmpleadoResponseDTO extends PersonaResponseDTO {
	
	private String puesto;
	
    private Set<ServicioResponseDTO> listaServicios; 

}
