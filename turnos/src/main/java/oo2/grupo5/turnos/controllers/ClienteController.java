package oo2.grupo5.turnos.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import oo2.grupo5.turnos.dtos.responses.ClienteResponseDTO;
import oo2.grupo5.turnos.helpers.ViewRouteHelper;
import oo2.grupo5.turnos.services.interfaces.IClienteService;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	private final IClienteService clienteService;
	
	public ClienteController(IClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	/*
	@GetMapping("/admin/list")
    public String listAll(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ClienteResponseDTO> clientes = clienteService.findAll(pageable);
        model.addAttribute("clientes", clientes);
        //return ViewRouteHelper.SERVICIO_ADMIN_LIST;
    }
	*/
	
}
