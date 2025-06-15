package oo2.grupo5.turnos.dtos.requests;

import java.time.LocalTime;

import oo2.grupo5.turnos.enums.EstadoTurno;

public record TurnoApiRequestDTO(
	    LocalTime hora,
	    EstadoTurno estado,
	    Integer DatosTurnoId
	) {}
