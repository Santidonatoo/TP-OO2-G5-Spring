package oo2.grupo5.turnos.dtos.requests;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DisponibilidadRequestDTO {

	private Integer idDisponibilidad;
	
	@NotNull(message = "The dayWeek cannot be empty")
	private DayOfWeek diaSemana;
	
	@NotNull(message = "The startTime cannot be empty")
	private LocalTime horaInicio;
	
	@NotNull(message = "The endTime cannot be empty")
	private LocalTime horaFin;
	
	
	
}
