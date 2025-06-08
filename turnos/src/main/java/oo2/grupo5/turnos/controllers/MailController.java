package oo2.grupo5.turnos.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oo2.grupo5.turnos.dtos.requests.MailRequestDTO;
import oo2.grupo5.turnos.dtos.responses.MailResponseDTO;
import oo2.grupo5.turnos.services.interfaces.IMailService;

@Controller
@RequestMapping("/mail")
public class MailController {

	private final IMailService mailService; 
	
	public MailController(IMailService mailService) {
		
		this.mailService = mailService;
	}
	
	@PostMapping("/sendMessage")
	public ResponseEntity<MailResponseDTO> receiveRequestEmail(@RequestBody MailRequestDTO mailRequestDTO){
		
		//System.out.println("Mensaje Recibido" + mailRequestDTO);
		//mailService.sendEmail(mailRequestDTO.getToUser(), mailRequestDTO.getSubject(), mailRequestDTO.getMessage());
		//Map<String, String> response = new HashMap<>();
		//response.put("estado", "Enviado");
		
		MailResponseDTO response = mailService.sendEmail(
		        mailRequestDTO.getToUser(),
		        mailRequestDTO.getSubject(),
		        mailRequestDTO.getMessage()
		    );
		
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/form")
	public String mostrarFormulario() {
	    return "mail/send-mail";
	}

	@PostMapping("/send-view")
	public String enviarDesdeFormulario(@RequestParam("toUser") String toUser, @RequestParam("subject") String subject,
	        @RequestParam("message") String message, Model model) {
		
		
	    String[] destinatarios = toUser.split(",");
	    MailResponseDTO response = mailService.sendEmail(destinatarios, subject, message);
	    model.addAttribute("estado", response.getStatus() + ": " + response.getMessage());
	    return "mail/send-mail";
	}
	
	
	
	
	
	
	
	
	
	
	
}
