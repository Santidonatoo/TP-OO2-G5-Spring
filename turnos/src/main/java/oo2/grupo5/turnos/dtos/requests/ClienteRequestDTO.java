package oo2.grupo5.turnos.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@SuperBuilder
public class ClienteRequestDTO extends PersonaRequestDTO{
	
	@NotBlank(message="El nombre de usuario no puede estar vacio")
	@Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
	private String username;
	
	@NotBlank(message="La contraseña no puede estar vacia")
	@Size(min = 7, max = 100, message = "La contraseña debe tener al menos 7 caracteres")
	private String password;
}
