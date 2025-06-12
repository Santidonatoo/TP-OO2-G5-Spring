package oo2.grupo5.turnos.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record ServicioApiRequestDTO (
	
    @NotBlank(message = "The name cannot be empty")
	String nombre,
    
    Integer duracion,
    
    boolean requiereEmpleado,
    
    Integer idUbicacion
	) {}
