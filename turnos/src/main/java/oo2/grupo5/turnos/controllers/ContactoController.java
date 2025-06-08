package oo2.grupo5.turnos.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import oo2.grupo5.turnos.entities.Persona;
import oo2.grupo5.turnos.entities.User;
import oo2.grupo5.turnos.repositories.IPersonaRepository;

@Controller
@RequestMapping("/contacto")
public class ContactoController {
	
	private final IPersonaRepository personaRepository;

    public ContactoController(IPersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }
    
    @GetMapping("/mis-datosContacto")
    public String mostrarDatosContacto(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    	
    	Integer idPersona = null;
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            idPersona = user.getPersona().getIdPersona();
        }
        
        Persona persona = personaRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
        		.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        model.addAttribute("mail", persona.getContacto().getEmail());
        model.addAttribute("telefono", persona.getContacto().getTelefono());

    	return "contacto/form";
    }
    
    @GetMapping("/edit")
    public String mostrarFormularioEdicion(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Integer idPersona = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            idPersona = user.getPersona().getIdPersona();
        }

        // Obtener los datos de contacto actuales
        Persona persona = personaRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        model.addAttribute("persona", persona);
        model.addAttribute("contacto", persona.getContacto());

        return "contacto/edit"; // Vista de edición
    }
    
    @PostMapping("/save")
    public String actualizarContacto(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String mail, @RequestParam String telefono) {
        Integer idPersona = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            idPersona = user.getPersona().getIdPersona();
        }

        // Obtener la persona y actualizar los datos de contacto
        Persona persona = personaRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        persona.getContacto().setEmail(mail);
        persona.getContacto().setTelefono(telefono);
        personaRepository.save(persona); // Guardar cambios en la base de datos

        return "redirect:/contacto/mis-datosContacto";
    }
    
    

}
