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
import oo2.grupo5.turnos.dtos.requests.ServicioRequestDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoResponseDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;
import oo2.grupo5.turnos.helpers.ViewRouteHelper;
import oo2.grupo5.turnos.repositories.IServicioRepository;
import oo2.grupo5.turnos.services.interfaces.IEmpleadoService;
import oo2.grupo5.turnos.services.interfaces.IServicioService;
import oo2.grupo5.turnos.services.interfaces.IUbicacionService;

@Controller
@RequestMapping("/servicio")
public class ServicioController {
	
	private final IServicioService servicioService;
	private final IUbicacionService ubicacionService;
	private final IEmpleadoService empleadoService;
	private final IServicioRepository servicioRepository;
	

    public ServicioController(IServicioService servicioService, IUbicacionService ubicacionService,
    		IEmpleadoService empleadoService, IServicioRepository servicioRepository) {
        this.servicioService = servicioService;
        this.ubicacionService = ubicacionService;
        this.empleadoService = empleadoService;
        this.servicioRepository = servicioRepository;
    }
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'EMPLOYEE')")
    public String listNotDeleted(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ServicioResponseDTO> servicios = servicioService.findAllNotDeleted(pageable);
        model.addAttribute("servicios", servicios);
        return ViewRouteHelper.SERVICIO_LIST;
    }
    @GetMapping("/admin/list")
    @PreAuthorize("hasRole('ADMIN')")
    public String listAll(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ServicioResponseDTO> servicios = servicioService.findAll(pageable);
        model.addAttribute("servicios", servicios);
        return ViewRouteHelper.SERVICIO_ADMIN_LIST;
    }
    @GetMapping("/form")    
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("servicioRequestDTO", new ServicioRequestDTO());
        model.addAttribute("ubicaciones", ubicacionService.findAllNotDeleted(PageRequest.of(0, 5)));
        model.addAttribute("empleados", empleadoService.findAllNotDeleted(PageRequest.of(0, 5))); 
        return ViewRouteHelper.SERVICIO_FORM;
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String save(@Valid @ModelAttribute ServicioRequestDTO servicioRequestDTO, BindingResult bindingResult, Model model) {
    	if (servicioRepository.existsByNombre(servicioRequestDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "error.servicio", "Ya existe un servicio con el mismo nombre.");
        }
    	if (!servicioRequestDTO.isRequiereEmpleado() && 
			    (servicioRequestDTO.getIdEmpleados() != null && !servicioRequestDTO.getIdEmpleados().isEmpty())) {
			    throw new IllegalArgumentException("El servicio no puede tener empleados asignados si no requiere empleados.");
    	}
    	if (bindingResult.hasErrors()) {
            model.addAttribute("ubicaciones", ubicacionService.findAllNotDeleted(PageRequest.of(0, 5))); 
            return ViewRouteHelper.SERVICIO_FORM;
        }
        servicioService.save(servicioRequestDTO);
        return "redirect:/servicio/admin/list";
    }

    @GetMapping("/edit/{idServicio}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Integer idServicio, Model model) {
    	ServicioResponseDTO dto = servicioService.findById(idServicio);

    	ServicioRequestDTO requestDTO = new ServicioRequestDTO();
        requestDTO.setIdServicio(dto.getIdServicio());
        requestDTO.setNombre(dto.getNombre());
        requestDTO.setDuracion(dto.getDuracion());
        requestDTO.setRequiereEmpleado(dto.isRequiereEmpleado());
        requestDTO.setIdUbicacion(dto.getUbicacion().getIdUbicacion());
        requestDTO.setIdEmpleados(dto.getListaEmpleados().stream().map(EmpleadoResponseDTO::getIdPersona).collect(Collectors.toSet()));

        model.addAttribute("servicioRequestDTO", requestDTO);
        model.addAttribute("ubicaciones", ubicacionService.findAll(PageRequest.of(0, 5))); 
        model.addAttribute("empleados", empleadoService.findAll(PageRequest.of(0, 5))); 

        return ViewRouteHelper.SERVICIO_FORM;
    }

    @PostMapping("/update/{idServicio}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Integer idServicio, @Valid @ModelAttribute ServicioRequestDTO dto, BindingResult result) {
    	if (servicioRepository.existsByNombre(dto.getNombre()) &&
    		    !servicioRepository.findById(idServicio).get().getNombre().equals(dto.getNombre())) {
    		    result.rejectValue("nombre", "error.servicio", "Ya existe otro servicio con este nombre.");
    	}
    	if (!dto.isRequiereEmpleado() && 
			    (dto.getIdEmpleados() != null && !dto.getIdEmpleados().isEmpty())) {
			    throw new IllegalArgumentException("El servicio no puede tener empleados asignados si no requiere empleados.");
    	}
    	if (result.hasErrors()) {
            return ViewRouteHelper.SERVICIO_FORM;
        }
        
        servicioService.update(idServicio, dto);
        return "redirect:/servicio/admin/list";
    }

    @PostMapping("/delete/{idServicio}")
    @PreAuthorize("hasRole('ADMIN')")
    public String softDelete(@PathVariable Integer idServicio) {
        servicioService.deleteById(idServicio);
        return "redirect:/servicio/admin/list";
    }

    @PostMapping("/restore/{idServicio}")
    @PreAuthorize("hasRole('ADMIN')")
    public String restore(@PathVariable Integer idServicio) {
    		servicioService.restoreById(idServicio);
        return "redirect:/servicio/admin/list";
    }
}
