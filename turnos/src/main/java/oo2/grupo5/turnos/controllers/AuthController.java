package oo2.grupo5.turnos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oo2.grupo5.turnos.dtos.requests.RegistroClienteRequestDTO;
import oo2.grupo5.turnos.dtos.responses.RegistroClienteResponseDTO;
import oo2.grupo5.turnos.helpers.ViewRouteHelper;
import oo2.grupo5.turnos.services.implementations.ClienteServiceImp;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	private final ClienteServiceImp clienteService;
	
	public AuthController(ClienteServiceImp clienteService) {
		this.clienteService = clienteService;
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
    public String registrarNuevoCliente(@ModelAttribute RegistroClienteResponseDTO dto) {
    	//System.out.println("Entró al método de registrar cliente");
    	clienteService.registrarCliente(dto);
        return "redirect:/auth/login";
    }
    

    //GET auth/loginSuccess --> Return the view in path home/index if login is successful
    @GetMapping("/loginSuccess")
    public String loginCheck() {
        return "redirect:/index";
    }
}
