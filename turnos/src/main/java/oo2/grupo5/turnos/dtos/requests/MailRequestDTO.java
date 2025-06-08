package oo2.grupo5.turnos.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class MailRequestDTO {
	
	private String[] toUser;
	
	private String subject;
	
	private String message;

}
