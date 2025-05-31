package oo2.grupo5.turnos.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ContactoResponseDTO {

	private Integer idContacto;
	
	private String email;
	
	private String telefono;
	
}
