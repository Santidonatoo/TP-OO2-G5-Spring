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

import jakarta.validation.Valid;
import oo2.grupo5.turnos.dtos.requests.EmpleadoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoResponseDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;
import oo2.grupo5.turnos.helpers.ViewRouteHelper;
import oo2.grupo5.turnos.services.interfaces.IEmpleadoService;
import oo2.grupo5.turnos.services.interfaces.IServicioService;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {
	
	private final IEmpleadoService empleadoService;
	private final IServicioService servicioService;

	public EmpleadoController(IEmpleadoService empleadoService, IServicioService servicioService) {
		this.empleadoService = empleadoService;
		this.servicioService = servicioService;
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
	
	@GetMapping("/form")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public String createForm(Model model) {
		model.addAttribute("empleadoRequestDTO", new EmpleadoRequestDTO());
        model.addAttribute("servicios", servicioService.findAllNotDeleted(PageRequest.of(0, 5))); 
	    return ViewRouteHelper.EMPLEADO_FORM;
	}
	
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String save(@Valid @ModelAttribute EmpleadoRequestDTO empleadoRequestDTO, BindingResult bindingResult) {
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
        model.addAttribute("servicios", servicioService.findAll(PageRequest.of(0, 5))); 

        return ViewRouteHelper.EMPLEADO_FORM;
    }
	
    @PostMapping("/update/{idPersona}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String update(@PathVariable Integer idPersona,
                         @Valid @ModelAttribute EmpleadoRequestDTO empleadoRequestDTO,
                         BindingResult bindingResult) {
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
