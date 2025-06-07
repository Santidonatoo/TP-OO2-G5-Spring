package oo2.grupo5.turnos.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import oo2.grupo5.turnos.dtos.requests.ClienteRequestDTO;
import oo2.grupo5.turnos.dtos.requests.ContactoRequestDTO;
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
	
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String listNotDeleted(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ClienteResponseDTO> clientes = clienteService.findAllNotDeleted(pageable);
        model.addAttribute("clientes", clientes);
        return ViewRouteHelper.CLIENTE_LIST;
    }
	
	@GetMapping("/admin/list")
	@PreAuthorize("hasRole('ADMIN')")
    public String listAll(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ClienteResponseDTO> clientes = clienteService.findAll(pageable);
        model.addAttribute("clientes", clientes);
        return ViewRouteHelper.CLIENTE_ADMIN_LIST;
    }
	
	@GetMapping("/form")
	@PreAuthorize("hasRole('ADMIN')")
	public String createForm(Model model) {
		ClienteRequestDTO clienteRequestDTO = new ClienteRequestDTO();
		
		//inicalizo ContactoRequestDTO 
		clienteRequestDTO.setContacto(new ContactoRequestDTO());
		
		model.addAttribute("clienteRequestDTO", clienteRequestDTO);
	    return ViewRouteHelper.CLIENTE_FORM;
	}
	
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String save(@Valid @ModelAttribute ClienteRequestDTO clienteRequestDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
        	model.addAttribute("clienteRequestDTO", clienteRequestDTO);
            return ViewRouteHelper.CLIENTE_FORM;
        }
        
        if(clienteRequestDTO.getIdPersona() == null) {
        	clienteService.save(clienteRequestDTO);
        } else {
        	clienteService.update(clienteRequestDTO.getIdPersona(), clienteRequestDTO);
        }
        return "redirect:/cliente/admin/list";
    }
    
    @GetMapping("/edit/{idPersona}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Integer idPersona, Model model) {
        ClienteResponseDTO dto = clienteService.findById(idPersona);

        ClienteRequestDTO requestDTO = new ClienteRequestDTO();
        requestDTO.setIdPersona(dto.getIdPersona());
        requestDTO.setNombre(dto.getNombre());
        requestDTO.setApellido(dto.getApellido());
        requestDTO.setDni(dto.getDni());
     
        model.addAttribute("clienteRequestDTO", requestDTO);
        return ViewRouteHelper.CLIENTE_FORM;
    }
    
    @PostMapping("/update/{idPersona}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Integer idPersona,
                         @Valid @ModelAttribute ClienteRequestDTO clienteRequestDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewRouteHelper.CLIENTE_FORM;
        }

        clienteService.update(idPersona, clienteRequestDTO);
        return "redirect:/cliente/admin/list";
    }
    
    @PostMapping("/delete/{idPersona}")
    @PreAuthorize("hasRole('ADMIN')")
    public String softDelete(@PathVariable Integer idPersona) {
        clienteService.deleteById(idPersona);
        return "redirect:/cliente/admin/list";
    }
	
    @PostMapping("/restore/{idPersona}")
    @PreAuthorize("hasRole('ADMIN')")
    public String restore(@PathVariable Integer idPersona) {
        clienteService.restoreById(idPersona);
        return "redirect:/cliente/admin/list";
    }
	
}
