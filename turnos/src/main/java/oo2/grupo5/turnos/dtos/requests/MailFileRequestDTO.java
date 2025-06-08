package oo2.grupo5.turnos.dtos.requests;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class MailFileRequestDTO {

	private String[] toUser;
	
	private String subject;
	
	private String message;
	
	MultipartFile file;
}
