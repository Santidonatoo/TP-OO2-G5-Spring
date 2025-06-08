package oo2.grupo5.turnos.services.implementations;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import oo2.grupo5.turnos.dtos.responses.MailResponseDTO;
import oo2.grupo5.turnos.services.interfaces.IMailService;



@Service
@RequiredArgsConstructor
public class MailServiceImp implements IMailService{
	
	private final JavaMailSender javaMailSender;
	
	
	 @Value("${spring.mail.username}")
	    private String fromEmail;
	
	@Override
	public MailResponseDTO sendEmail(String[] toUser, String subject, String message) {
		
		String status;
	    String description;
		
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
		
			mailMessage.setFrom(fromEmail);
			mailMessage.setTo(toUser);
			mailMessage.setSubject(subject);
			mailMessage.setText(message);
		
			javaMailSender.send(mailMessage);
		
			status = "SUCCESS";
	        description = "Correo enviado con éxito.";
			
	    } catch (Exception e) {
	    	status = "FAILURE";
	        description = "Error al enviar correo: " + e.getMessage();
	    }
		 return new MailResponseDTO(status, description);
	}
	
}
