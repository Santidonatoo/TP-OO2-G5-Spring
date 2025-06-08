package oo2.grupo5.turnos.dtos.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ClienteResponseDTO extends PersonaResponseDTO {
	private LocalDateTime ultimoInicioSesion;
}
