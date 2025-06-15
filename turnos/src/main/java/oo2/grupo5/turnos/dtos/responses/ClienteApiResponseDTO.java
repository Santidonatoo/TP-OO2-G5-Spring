package oo2.grupo5.turnos.dtos.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public record ClienteApiResponseDTO (
	
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
    
    @JsonProperty("ultimoInicioSesion")
	LocalDateTime ultimoInicioSesion
	) {
}
