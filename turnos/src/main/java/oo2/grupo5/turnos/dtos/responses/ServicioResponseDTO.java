package oo2.grupo5.turnos.dtos.responses;

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
public class ServicioResponseDTO {
	private Integer idServicio;
	
    private String nombre;

	private boolean requiereEmpleado;

	private int duracion;
	
    private boolean softDeleted;
}
