package oo2.grupo5.turnos.controllers.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import oo2.grupo5.turnos.dtos.responses.ServicioApiResponseDTO;
import oo2.grupo5.turnos.exceptions.ServicioNotFoundException;
import oo2.grupo5.turnos.services.interfaces.IServicioService;

@RestController
@RequestMapping("/api/servicios")
@Tag(name = "Servicios", description = "Endpoints para gestionar servicios")
public class ServicioRestController {

    private final IServicioService servicioService;

	public ServicioRestController(IServicioService servicioService) {
		this.servicioService = servicioService;
	}
	
	
	@Operation(summary = "Obtener un servicio por id", description = "Busca un servicio por id  y lo devuelve")
	@ApiResponses(value = {
		     @ApiResponse(responseCode = "200", description = "Servicio encontrado"),
		     @ApiResponse(responseCode = "404", description = "Servicio no encontrado"),
		     @ApiResponse(responseCode = "403", description = "No autorizado")
		 })
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ServicioApiResponseDTO> getServicioById(@PathVariable Integer id) {

		try {
		ServicioApiResponseDTO servicio = servicioService.findByIdApi(id);
        return ResponseEntity.ok(servicio);
        
        }catch(ServicioNotFoundException e) {
	         return ResponseEntity.notFound().build();
        }catch(Exception e) {
        	 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	/*@Operation(summary = "Crear un servicio", description = "Crea un nuevo servicio")
    @PostMapping
    public ResponseEntity<ServicioApiResponseDTO> crear(@RequestBody ServicioApiRequestDTO dto) {
        ServicioApiResponseDTO creado = servicioService.crearServicioDesdeApi(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }*/

}