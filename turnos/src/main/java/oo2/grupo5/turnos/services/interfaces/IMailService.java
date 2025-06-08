package oo2.grupo5.turnos.services.interfaces;


import oo2.grupo5.turnos.dtos.responses.MailResponseDTO;

public interface IMailService {
	
	MailResponseDTO sendEmail(String[] toUser, String subject, String message);
	
}
