package oo2.grupo5.turnos.dtos.responses;




public record ServicioApiResponseDTO (
	Integer id,
    String nombre,
    Integer duracion,
    boolean requiereEmpleado,
    String ubicacion
    
    ){}

