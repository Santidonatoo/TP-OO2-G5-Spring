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
import oo2.grupo5.turnos.dtos.requests.UbicacionRequestDTO;
import oo2.grupo5.turnos.dtos.responses.UbicacionResponseDTO;
import oo2.grupo5.turnos.helpers.ViewRouteHelper;
import oo2.grupo5.turnos.services.interfaces.IUbicacionService;

@Controller
@RequestMapping("/ubicacion")
public class UbicacionController {
    
    private final IUbicacionService ubicacionService;

    public UbicacionController(IUbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping("/admin/list")
    @PreAuthorize("hasRole('ADMIN')")
    public String listAll(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<UbicacionResponseDTO> ubicaciones = ubicacionService.findAll(pageable);
        model.addAttribute("ubicaciones", ubicaciones);
        return ViewRouteHelper.UBICACION_ADMIN_LIST;
    }

    @GetMapping("/form")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("ubicacionRequestDTO", new UbicacionRequestDTO());
        return ViewRouteHelper.UBICACION_FORM;
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String save(@Valid @ModelAttribute UbicacionRequestDTO ubicacionRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewRouteHelper.UBICACION_FORM;
        }
        ubicacionService.save(ubicacionRequestDTO);
        return "redirect:/ubicacion/list";
    }

    @GetMapping("/edit/{idUbicacion}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Integer idUbicacion, Model model) {
        UbicacionResponseDTO dto = ubicacionService.findById(idUbicacion);

        UbicacionRequestDTO requestDTO = new UbicacionRequestDTO();
        requestDTO.setIdUbicacion(dto.getIdUbicacion());
        requestDTO.setLocalidad(dto.getLocalidad());
        requestDTO.setCalle(dto.getCalle());
        requestDTO.setNumero(dto.getNumero());

        model.addAttribute("ubicacionRequestDTO", requestDTO);
        return ViewRouteHelper.UBICACION_FORM;
    }

    @PostMapping("/update/{idUbicacion}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Integer idUbicacion, @Valid @ModelAttribute UbicacionRequestDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ViewRouteHelper.UBICACION_FORM;
        }
        ubicacionService.update(idUbicacion, dto);
        return "redirect:/ubicacion/admin/list";
    }

    @PostMapping("/delete/{idUbicacion}")
    @PreAuthorize("hasRole('ADMIN')")
    public String softDelete(@PathVariable Integer idUbicacion) {
        ubicacionService.deleteById(idUbicacion);
        return "redirect:/ubicacion/admin/list";
    }

    @PostMapping("/restore/{idUbicacion}")
    @PreAuthorize("hasRole('ADMIN')")
    public String restore(@PathVariable Integer idUbicacion) {
        ubicacionService.restoreById(idUbicacion);
        return "redirect:/ubicacion/admin/list";
    }
}
