package oo2.grupo5.turnos.dtos.responses;

import java.time.LocalDateTime;
import java.util.Set;

public record EmpleadoApiResponseDTO(
	    Integer idPersona,
	    String nombre,
	    String apellido,
	    String dni,
	    String email,
	    String telefono,
	    String username,
	    String puesto,
	    Set<ServicioResponseDTO> servicios
		) {

}
