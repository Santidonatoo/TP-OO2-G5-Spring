package oo2.grupo5.turnos.dtos.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EmpleadoApiResponseDTO(
		
		@JsonProperty("idPersona")
		Integer idPersona,
		
		@JsonProperty("nombre")
	    String nombre,
	   
	    @JsonProperty("apellido")
	    String apellido,
	   
	    @JsonProperty("dni")
	    String dni,
	   
	    @JsonProperty("email")
	    String email,
	    
	    @JsonProperty("telefono")
	    String telefono,
	    
	    @JsonProperty("username")
	    String username,
	    
	    @JsonProperty("puesto")
	    String puesto,
	    
	    @JsonProperty("servicios")
	    List<String> servicios
		) {

}
