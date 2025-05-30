package oo2.grupo5.turnos.dtos.responses;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oo2.grupo5.turnos.enums.EstadoTurno;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoResponseDTO {
	
	private Integer idTurno;
	
	private LocalTime hora;
	
	private EstadoTurno estado;
	
	private DatosTurnoResponseDTO datosTurno;
	
}
