package oo2.grupo5.turnos.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import oo2.grupo5.turnos.dtos.requests.TurnoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.TurnoResponseDTO;
import oo2.grupo5.turnos.entities.User;
import oo2.grupo5.turnos.services.interfaces.ITurnoService;

@RestController
@RequestMapping("/api/turno")
@Tag(name = "Turnos", description = "Endpoints para gestionar turnos")
public class TurnoRestController {
	
	private final ITurnoService turnoService;
    
    public TurnoRestController(ITurnoService turnoService) {
        this.turnoService = turnoService;
    }
    
    @Operation(summary = "Lista de turnos de una persona", description = "Devuelve la lista de turnos del usuario autenticado")
    @GetMapping("/lista-turnos")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE')")
    public ResponseEntity<Page<TurnoResponseDTO>> listaTurnosPersona(Pageable pageable) {
    	
    	Integer idPersona = null;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            idPersona = user.getPersona().getIdPersona();
        }
        
    	Page<TurnoResponseDTO> turnos = turnoService.findTurnosByPersona(pageable, idPersona);
    	return ResponseEntity.ok(turnos);
    }
    
    @Operation(summary = "Guardado de un turno", description = "Guarda un turno en base a un turnoRequestDTO")
    @PostMapping("/guardar-turno")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<TurnoResponseDTO> guardarTurno(@RequestBody TurnoRequestDTO turno) {
    	
    	TurnoResponseDTO turnoCreado = turnoService.save(turno);
    	
    	 return ResponseEntity.status(HttpStatus.CREATED).body(turnoCreado);
    }
}
