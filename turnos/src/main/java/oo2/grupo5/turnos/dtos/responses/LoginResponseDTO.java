package oo2.grupo5.turnos.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDTO(
		
		@JsonProperty("token")
		String token,
		
		@JsonProperty("message")
		String message,
		
		@JsonProperty("success")
		boolean success) {

}
