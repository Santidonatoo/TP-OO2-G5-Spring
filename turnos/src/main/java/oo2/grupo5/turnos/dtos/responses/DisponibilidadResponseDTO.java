package oo2.grupo5.turnos.dtos.responses;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

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
public class DisponibilidadResponseDTO {

	private Integer idDisponibilidad;
	
	private DayOfWeek diaSemana;
	
	private LocalTime horaInicio;
	
	private LocalTime horaFin;
	
	private Set<ServicioResponseDTO> listaServicios; 
	
}
