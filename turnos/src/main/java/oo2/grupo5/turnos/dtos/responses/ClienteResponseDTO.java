package oo2.grupo5.turnos.dtos.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@SuperBuilder
public class ClienteResponseDTO extends PersonaResponseDTO {
	//Extiende de persona asi que ya tiene los otros atributos, si se agregan atributos a cliente, hay que agregarlos aca tambien.
}
