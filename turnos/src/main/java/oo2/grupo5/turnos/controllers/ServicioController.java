package oo2.grupo5.turnos.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;
import oo2.grupo5.turnos.helpers.ViewRouteHelper;
import oo2.grupo5.turnos.services.interfaces.IServicioService;
import oo2.grupo5.turnos.services.interfaces.IUbicacionService;

@Controller
@RequestMapping("/servicio")
public class ServicioController {
	
	private final IServicioService servicioService;
	private final IUbicacionService ubicacionService;

	

    public ServicioController(IServicioService servicioService, IUbicacionService ubicacionService) {
        this.servicioService = servicioService;
        this.ubicacionService = ubicacionService;
    }
    @GetMapping("/list")
    public String listNotDeleted(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ServicioResponseDTO> servicios = servicioService.findAllNotDeleted(pageable);
        model.addAttribute("servicios", servicios);
        return ViewRouteHelper.SERVICIO_LIST;
    }
    @GetMapping("/admin/list")
    public String listAll(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ServicioResponseDTO> servicios = servicioService.findAll(pageable);
        model.addAttribute("servicios", servicios);
        return ViewRouteHelper.SERVICIO_ADMIN_LIST;
    }
    
    @GetMapping("/form")
    public String createForm(Model model) {
        model.addAttribute("servicioRequestDTO", new ServicioRequestDTO());
        model.addAttribute("ubicaciones", ubicacionService.findAll(PageRequest.of(0, 5)));
        return ViewRouteHelper.SERVICIO_FORM;
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute ServicioRequestDTO servicioRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewRouteHelper.SERVICIO_FORM;
        }
        servicioService.save(servicioRequestDTO);
        return "redirect:/servicio/admin/list";
    }

    @GetMapping("/edit/{idServicio}")
    public String editForm(@PathVariable Integer idServicio, Model model) {
    	ServicioResponseDTO dto = servicioService.findById(idServicio);

    	ServicioRequestDTO requestDTO = new ServicioRequestDTO();
        requestDTO.setIdServicio(dto.getIdServicio());
        requestDTO.setNombre(dto.getNombre());
        requestDTO.setDuracion(dto.getDuracion());
        requestDTO.setRequiereEmpleado(dto.isRequiereEmpleado());
        requestDTO.setIdUbicacion(dto.getUbicacion().getIdUbicacion());

        model.addAttribute("servicioRequestDTO", requestDTO);
        model.addAttribute("ubicaciones", ubicacionService.findAll(PageRequest.of(0, 5))); 

        return ViewRouteHelper.SERVICIO_FORM;
    }

    @PostMapping("/update/{idServicio}")
    public String update(@PathVariable Integer idServicio, @Valid @ModelAttribute ServicioRequestDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ViewRouteHelper.SERVICIO_FORM;
        }
        servicioService.update(idServicio, dto);
        return "redirect:/servicio/admin/list";
    }

    @PostMapping("/delete/{idServicio}")
    public String softDelete(@PathVariable Integer idServicio) {
        servicioService.deleteById(idServicio);
        return "redirect:/servicio/admin/list";
    }

    @PostMapping("/restore/{idServicio}")
    public String restore(@PathVariable Integer idServicio) {
    		servicioService.restoreById(idServicio);
        return "redirect:/servicio/admin/list";
    }
}
