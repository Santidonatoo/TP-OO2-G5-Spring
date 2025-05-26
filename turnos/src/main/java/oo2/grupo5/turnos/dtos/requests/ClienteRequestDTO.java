package oo2.grupo5.turnos.dtos.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@SuperBuilder
public class ClienteRequestDTO extends PersonaRequestDTO{
	//Si se agregan nuevos atributos a cliente, hay que agregarlos aca.
}
