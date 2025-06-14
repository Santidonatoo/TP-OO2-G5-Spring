package oo2.grupo5.turnos.dtos.responses;

import java.util.List;

public record ServicioApiResponseDTO(
		Integer id,
	    String nombre,
	    Integer duracion,
	    boolean requiereEmpleado,
	    String ubicacion,
	    List<String> empleados
	    ){}
