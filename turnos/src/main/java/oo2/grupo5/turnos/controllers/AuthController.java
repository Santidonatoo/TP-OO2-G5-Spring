package oo2.grupo5.turnos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import oo2.grupo5.turnos.dtos.requests.RegistroClienteRequestDTO;
import oo2.grupo5.turnos.dtos.responses.RegistroClienteResponseDTO;
import oo2.grupo5.turnos.helpers.ViewRouteHelper;
import oo2.grupo5.turnos.repositories.IPersonaRepository;
import oo2.grupo5.turnos.services.implementations.ClienteServiceImp;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	private final ClienteServiceImp clienteService;
	private final IPersonaRepository personaRepository;
	
	public AuthController(ClienteServiceImp clienteService, IPersonaRepository personaRepository) {
		this.clienteService = clienteService;
		this.personaRepository = personaRepository;
	}
	
    //GET auth/login --> Return the view in path authentication/login
    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(name="error",required=false) String error,
                        @RequestParam(name="logout", required=false) String logout) {
        model.addAttribute("error", error);
        model.addAttribute("logout", logout);
        return ViewRouteHelper.USER_LOGIN;
    }
    
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("clienteDTO", new RegistroClienteRequestDTO());
        return ViewRouteHelper.USER_REGISTER; // nombre del HTML
    }
    
    @PostMapping("/registro-cliente")
    public String registrarNuevoCliente(@Valid @ModelAttribute RegistroClienteRequestDTO dto, BindingResult bindingResult, Model model) {
    	// Validar si el DNI ya existe en cualquier tipo de persona (empleado o cliente)
        if (personaRepository.existsByDni(dto.getDni())) {
            bindingResult.rejectValue("dni", "error.cliente", "Este DNI ya está registrado en el sistema.");
        }
        
        // Si hay errores, volver al formulario de registro con los mensajes
        if (bindingResult.hasErrors()) {
            model.addAttribute("clienteDTO", dto);
            return ViewRouteHelper.USER_REGISTER;
        }
        
        try {
            clienteService.registrarCliente(dto);
            return "redirect:/auth/login?registro=success";
        } catch (IllegalArgumentException e) {
            // En caso de que la validación del service también capture algo
            bindingResult.rejectValue("dni", "error.cliente", e.getMessage());
            model.addAttribute("clienteDTO", dto);
            return ViewRouteHelper.USER_REGISTER;
        } catch (Exception e) {
            // Manejar cualquier otra excepción
            bindingResult.rejectValue("dni", "error.cliente", "Ha ocurrido un error inesperado. Intente nuevamente.");
            model.addAttribute("clienteDTO", dto);
            return ViewRouteHelper.USER_REGISTER;
        }
    }
    

    //GET auth/loginSuccess --> Return the view in path home/index if login is successful
    @GetMapping("/loginSuccess")
    public String loginCheck() {
        return "redirect:/index";
    }
}
