package oo2.grupo5.turnos.dtos.requests;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oo2.grupo5.turnos.enums.EstadoTurno;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TurnoRequestDTO {
	
	private Integer idTurno;
	
	private LocalTime hora;
	
	private EstadoTurno estado;
	
	private Integer idDatosTurno;
	
}
