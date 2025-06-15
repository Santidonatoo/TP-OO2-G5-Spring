package oo2.grupo5.turnos.controllers.rest;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import oo2.grupo5.turnos.dtos.requests.ServicioApiRequestDTO;
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
	
	@Operation(summary = "Obtener lista de servicios", 
            description = "Retorna una lista de todos los servicios del sistema, ordenados segun el filtro (idServicio o nombre)")
	@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de servicios obtenida exitosamente"),
    @ApiResponse(responseCode = "403", description = "No autorizado"),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
	public ResponseEntity<List<ServicioApiResponseDTO>> getListaServicios(
			@Parameter(
			        description = "Campo por el cual ordenar",
			        schema = @Schema(allowableValues = {"idServicio", "nombre"})
			    )
	        @RequestParam(defaultValue = "idServicio") String sortBy) {
   
		List<ServicioApiResponseDTO> servicios = servicioService.findAllApi(sortBy);
		return ResponseEntity.ok(servicios);
     
 }

	
	@Operation(summary = "Crear un nuevo servicio", description = "Crea un servicio a partir de los datos enviados")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "201", description = "Servicio creado correctamente"),
	    @ApiResponse(responseCode = "400", description = "Error de validación o datos incorrectos"),
	    @ApiResponse(responseCode = "403", description = "No autorizado")
	})
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ServicioApiResponseDTO> crearServicio(
	        @Valid @RequestBody ServicioApiRequestDTO dto) {
		System.out.println("_____Prueba_____");
		System.out.println("Nombre: " + dto.nombre());
		System.out.println("Duracion: " + dto.duracion());
		System.out.println("RequiereEmpleado: " + dto.requiereEmpleado());
		System.out.println("idUbicacion " + dto.idUbicacion());


		ServicioApiResponseDTO creado = servicioService.crearServicioDesdeApi(dto);
	    return ResponseEntity.status(HttpStatus.CREATED).body(creado);
	}


}