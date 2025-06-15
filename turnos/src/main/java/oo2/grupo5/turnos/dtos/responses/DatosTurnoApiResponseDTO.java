package oo2.grupo5.turnos.dtos.responses;

import java.time.LocalDate;

public record DatosTurnoApiResponseDTO(
	    Integer idDatosTurno,
	    LocalDate fecha,
	    String cliente,
	    String empleado,
	    String servicio
	) {}