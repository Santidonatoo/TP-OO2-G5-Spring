package oo2.grupo5.turnos.services.interfaces;

import java.io.File;

import oo2.grupo5.turnos.dtos.responses.MailResponseDTO;

public interface IMailService {
	
	MailResponseDTO sendEmail(String[] toUser, String subject, String message);
	
	MailResponseDTO sendEmailWithFile(String[] toUser, String subject, String message, File file);
	// MailDetailsResponseDTO sendTemplatedMail(MailDetailsRequestDTO mailRequestDTO);

}
