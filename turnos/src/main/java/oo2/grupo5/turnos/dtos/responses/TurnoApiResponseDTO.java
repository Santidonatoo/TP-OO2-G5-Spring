package oo2.grupo5.turnos.dtos.responses;

import java.time.LocalTime;

import oo2.grupo5.turnos.enums.EstadoTurno;

public record TurnoApiResponseDTO(
	    Integer idTurno,
	    LocalTime hora,
	    EstadoTurno estado,
	    DatosTurnoApiResponseDTO datosTurno
	) {}
