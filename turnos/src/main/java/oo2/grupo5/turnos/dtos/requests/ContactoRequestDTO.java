package oo2.grupo5.turnos.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactoRequestDTO {

private Integer idContacto;
	
	@NotBlank(message = "The email cannot be empty")
	@Email(message = "You must enter a valid email address")
	private String email;
	
	@NotBlank(message = "The phone number cannot be empty")
	@Pattern(regexp = "^\\d{8,15}$", message = "The phone number should only contain numbers")
	private String telefono;
	
}
