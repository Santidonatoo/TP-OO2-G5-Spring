package oo2.grupo5.turnos.dtos.requests;


public record ServicioApiRequestDTO (
		
		String nombre,
	    
	    Integer duracion,
	    
	    Boolean requiereEmpleado,
	    
	    Integer idUbicacion
		) {}
