package oo2.grupo5.turnos.controllers;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import oo2.grupo5.turnos.dtos.requests.ContactoRequestDTO;
import oo2.grupo5.turnos.dtos.requests.EmpleadoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoResponseDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;
import oo2.grupo5.turnos.exceptions.EmpleadoNotFoundException;
import oo2.grupo5.turnos.helpers.ViewRouteHelper;
import oo2.grupo5.turnos.repositories.IPersonaRepository;
import oo2.grupo5.turnos.repositories.IUserRepository;
import oo2.grupo5.turnos.services.interfaces.IEmpleadoService;
import oo2.grupo5.turnos.services.interfaces.IServicioService;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {
	
	private final IEmpleadoService empleadoService;
	private final IServicioService servicioService;
	private final IPersonaRepository personaRepository;
	private final IUserRepository userRepository;

	public EmpleadoController(IEmpleadoService empleadoService, IServicioService servicioService, 
			IPersonaRepository personaRepository, IUserRepository userRepository) {
		this.empleadoService = empleadoService;
		this.servicioService = servicioService;
		this.personaRepository = personaRepository;
		this.userRepository = userRepository;
	}
	
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public String listNotDeleted(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<EmpleadoResponseDTO> empleados = empleadoService.findAllNotDeleted(pageable);
        model.addAttribute("empleados", empleados);
        return ViewRouteHelper.EMPLEADO_LIST;
    }
	
	@GetMapping("/admin/list")
	@PreAuthorize("hasAnyRole('ADMIN')")
    public String listAll(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<EmpleadoResponseDTO> empleados = empleadoService.findAll(pageable);
        model.addAttribute("empleados", empleados);
        return ViewRouteHelper.EMPLEADO_ADMIN_LIST;
    }
	@GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN')")
    public String buscarEmpleado(@RequestParam Integer idPersona, Model model) {
        try {
        	EmpleadoResponseDTO empleado = empleadoService.findById(idPersona);
            model.addAttribute("empleado", empleado);
            return ViewRouteHelper.EMPLEADO_DETALLE;
        } catch (EmpleadoNotFoundException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return ViewRouteHelper.ERROR_NOT_FOUND_EMPLEADO;
        }
    }
	@GetMapping("/form")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String createForm(Model model) {
		
		EmpleadoRequestDTO empleadoRequestDTO = new EmpleadoRequestDTO();
		//Inicializo ContactoRquestDTO
		empleadoRequestDTO.setContacto(new ContactoRequestDTO());
		model.addAttribute("empleadoRequestDTO", empleadoRequestDTO);
        model.addAttribute("servicios", servicioService.findAllByNotDeletedAndRequiereEmpleadoTrue(PageRequest.of(0, 5))); 
	    return ViewRouteHelper.EMPLEADO_FORM;
	}
	
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String save(@Valid @ModelAttribute EmpleadoRequestDTO empleadoRequestDTO, BindingResult bindingResult) {
    	//Validacion dni repetido
    	if (personaRepository.existsByDni(empleadoRequestDTO.getDni())) {
            bindingResult.rejectValue("dni", "error.persona", "Ya existe una persona con el mismo dni.");
        }
    	
    	//Validacion username repetido
    	if (userRepository.existsByUsername(empleadoRequestDTO.getUsername())) {
            bindingResult.rejectValue("username", "error.persona", "Ya existe una persona con el mismo username.");
        }
    	
    	if (bindingResult.hasErrors()) {
            return ViewRouteHelper.EMPLEADO_FORM;
        }
        
        if(empleadoRequestDTO.getIdPersona() == null) {
        	empleadoService.save(empleadoRequestDTO);
        } else {
        	empleadoService.update(empleadoRequestDTO.getIdPersona(), empleadoRequestDTO);
        }
        return "redirect:/empleado/admin/list";
    }
	
    @GetMapping("/edit/{idPersona}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String editForm(@PathVariable Integer idPersona, Model model) {
        EmpleadoResponseDTO dto = empleadoService.findById(idPersona);
        
        EmpleadoRequestDTO requestDTO = new EmpleadoRequestDTO();
        requestDTO.setIdPersona(dto.getIdPersona());
        requestDTO.setNombre(dto.getNombre());
        requestDTO.setApellido(dto.getApellido());
        requestDTO.setDni(dto.getDni());
        requestDTO.setPuesto(dto.getPuesto());
        requestDTO.setIdServicios(dto.getListaServicios().stream().map(ServicioResponseDTO::getIdServicio).collect(Collectors.toSet()));

        model.addAttribute("empleadoRequestDTO", requestDTO);
        model.addAttribute("servicios", servicioService.findAllByNotDeletedAndRequiereEmpleadoTrue(PageRequest.of(0, 5))); 

        return ViewRouteHelper.EMPLEADO_FORM;
    }
	
    @PostMapping("/update/{idPersona}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String update(@PathVariable Integer idPersona,
                         @Valid @ModelAttribute EmpleadoRequestDTO empleadoRequestDTO,
                         BindingResult bindingResult) {
    	//Validacion dni repetido
    	if (personaRepository.existsByDni(empleadoRequestDTO.getDni()) &&
    		    personaRepository.findById(idPersona).get().getDni() == empleadoRequestDTO.getDni()) {
    			bindingResult.rejectValue("dni", "error.persona", "Ya existe otra persona con este dni.");
    	}
    	
    	//Validacion username repetido
    	if (userRepository.existsByUsername(empleadoRequestDTO.getUsername()) &&
    		    !userRepository.findById(idPersona).get().getUsername().equals(empleadoRequestDTO.getUsername())) {
    			bindingResult.rejectValue("username", "error.persona", "Ya existe otra persona con este username.");
    	}
    	
    	if (bindingResult.hasErrors()) {
            return ViewRouteHelper.EMPLEADO_FORM;
        }

        empleadoService.update(idPersona, empleadoRequestDTO);
        return "redirect:/empleado/admin/list";
    }
    
    @PostMapping("/delete/{idPersona}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String softDelete(@PathVariable Integer idPersona) {
        empleadoService.deleteById(idPersona);
        return "redirect:/empleado/admin/list";
    }
	
    @PostMapping("/restore/{idPersona}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String restore(@PathVariable Integer idPersona) {
        empleadoService.restoreById(idPersona);
        return "redirect:/empleado/admin/list";
    }

}