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
import oo2.grupo5.turnos.exceptions.DniDuplicadoException;
import oo2.grupo5.turnos.helpers.ViewRouteHelper;
import oo2.grupo5.turnos.repositories.IPersonaRepository;
import oo2.grupo5.turnos.repositories.IUserRepository;
import oo2.grupo5.turnos.services.implementations.ClienteServiceImp;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	private final ClienteServiceImp clienteService;
	private final IPersonaRepository personaRepository;
	private final IUserRepository userRepository;
	
	public AuthController(ClienteServiceImp clienteService, IPersonaRepository personaRepository, IUserRepository userRepository) {
		this.clienteService = clienteService;
		this.personaRepository = personaRepository;
		this.userRepository = userRepository;
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
    public String registrarNuevoCliente(@Valid @ModelAttribute("clienteDTO") RegistroClienteRequestDTO dto,
                                       BindingResult bindingResult, Model model) {

        // Validar si el username ya está en uso
        if (userRepository.existsByUsername(dto.getUsername())) {
            bindingResult.rejectValue("username", "error.cliente", "Este nombre de usuario ya está en uso.");
        }

        // Si hay errores, retornar al formulario de registro
        if (bindingResult.hasErrors()) {
            model.addAttribute("clienteDTO", dto);
            return ViewRouteHelper.USER_REGISTER;
        }

        clienteService.registrarCliente(dto); // Si el DNI ya existe, lanzará DniDuplicadoException
        return "redirect:/auth/login?registro=success";
    }
    

    //GET auth/loginSuccess --> Return the view in path home/index if login is successful
    @GetMapping("/loginSuccess")
    public String loginCheck() {
        return "redirect:/index";
    }
}
