package oo2.grupo5.turnos.controllers.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.media.Schema;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import oo2.grupo5.turnos.dtos.responses.ClienteApiResponseDTO;
import oo2.grupo5.turnos.entities.User;
import oo2.grupo5.turnos.exceptions.ClienteNotFoundException;
import oo2.grupo5.turnos.repositories.IPersonaRepository;
import oo2.grupo5.turnos.repositories.IUserRepository;
import oo2.grupo5.turnos.services.interfaces.IClienteService;
import oo2.grupo5.turnos.services.interfaces.IServicioService;

@RestController
@RequestMapping("/api/cliente")
@Validated
@Tag(name = "Cliente", description = "Endpoints para la gestión de clientes")
public class ClienteRestController {

		private final IClienteService clienteService;
		private final IServicioService servicioService;
	    private final IPersonaRepository personaRepository;
	    private final IUserRepository userRepository;
	    
	    private static final Logger log = LoggerFactory.getLogger(ClienteRestController.class);
	    
	    public ClienteRestController (IClienteService clienteService, IServicioService servicioService,
	    		IPersonaRepository personaRepository, IUserRepository userRepository) {
	    	
	    	this.clienteService = clienteService;
	    	this.servicioService = servicioService;
			this.personaRepository = personaRepository;
			this.userRepository = userRepository;
	    }
	    
	    @Operation(summary = "Obtener empleado por ID", 
	            description = "Retorna los datos de un empleado específico por su ID")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
	     @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
	     @ApiResponse(responseCode = "403", description = "No autorizado")
	 })
	 @GetMapping("/{idPersona}")
	 @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
	 public ResponseEntity<ClienteApiResponseDTO> getClienteById(
			 @Parameter(description = "ID del cliente a buscar")
	         @PathVariable Integer idPersona){
	    	
	    	try {
	    		ClienteApiResponseDTO cliente = clienteService.findByIdApi(idPersona);
	    		return ResponseEntity.ok(cliente);
	    	} catch (ClienteNotFoundException e) {
	    		return ResponseEntity.notFound().build();
	    	} catch (Exception e) {
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		     }
	    }
	 
	    @Operation(summary = "Obtener lista de clientes", 
	               description = "Retorna una lista de todos los clientes del sistema")
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente"),
	        @ApiResponse(responseCode = "403", description = "No autorizado"),
	        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
	    })
	    @GetMapping
	    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
	    public ResponseEntity<List<ClienteApiResponseDTO>> getListaClientes(@Parameter(
                description = "Campo por el cual ordenar",
                schema = @Schema(allowableValues = {"idPersona", "nombre", "dni"})
            )
        @RequestParam(defaultValue = "idPersona") String sortBy) {
	        try {
	            List<ClienteApiResponseDTO> clientes = clienteService.findAllApi(sortBy);
	            return ResponseEntity.ok(clientes);
	        } catch (Exception e) {
	            log.error("Error al obtener la lista de clientes: {}", e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }
	   
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
}
