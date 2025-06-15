package oo2.grupo5.turnos.controllers.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import oo2.grupo5.turnos.dtos.requests.EmpleadoApiRequestDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoApiResponseDTO;
import oo2.grupo5.turnos.exceptions.EmpleadoNotFoundException;
import oo2.grupo5.turnos.repositories.IPersonaRepository;
import oo2.grupo5.turnos.repositories.IUserRepository;
import oo2.grupo5.turnos.services.interfaces.IEmpleadoService;
import oo2.grupo5.turnos.services.interfaces.IServicioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/empleado")
@Validated
@Tag(name = "Empleado", description = "Endpoints para la gestión de empleados")
public class EmpleadoRestController {

    private final IEmpleadoService empleadoService;
    private final IServicioService servicioService;
    private final IPersonaRepository personaRepository;
    private final IUserRepository userRepository;
    
    private static final Logger log = LoggerFactory.getLogger(EmpleadoRestController.class);
    
    public EmpleadoRestController(IEmpleadoService empleadoService, IServicioService servicioService,
            IPersonaRepository personaRepository, IUserRepository userRepository) {
		this.empleadoService = empleadoService;
		this.servicioService = servicioService;
		this.personaRepository = personaRepository;
		this.userRepository = userRepository;
	}
	
    /* POR EL MOMENTO NO FUNCIONA, SI TENGO TIEMPO LO SOLUCIONO
    @Operation(summary = "Obtener todos los empleados activos", 
    		description = "Retorna una lista paginada de empleados que no están eliminados")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Lista de empleados obtenida exitosamente"),
    		@ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'CLIENT')")
    public ResponseEntity<Page<EmpleadoApiResponseDTO>> getAllEmpleados(
    		@Parameter(description = "Número de página (empezando en 0)")
    		@RequestParam(defaultValue = "0") int page,
    		
    		@Parameter(description = "Tamaño de página")
    		@RequestParam(defaultValue = "10") int size){
    	
    	try {
    		Pageable pageable = PageRequest.of(page, size);
    		Page<EmpleadoApiResponseDTO> empleados = empleadoService.findAllNotDeletedApi(pageable);
    		return ResponseEntity.ok(empleados);
    	} catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    	}
    }
    */
    
	    @Operation(summary = "Obtener empleado por ID", 
	            description = "Retorna los datos de un empleado específico por su ID")
	 @ApiResponses(value = {
	     @ApiResponse(responseCode = "200", description = "Empleado encontrado"),
	     @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
	     @ApiResponse(responseCode = "403", description = "No autorizado")
	 })
	 @GetMapping("/{idPersona}")
	 @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
	 public ResponseEntity<EmpleadoApiResponseDTO> getEmpleadoById(
	         @Parameter(description = "ID del empleado a buscar")
	         @PathVariable Integer idPersona) {
	     
		     try {
		         EmpleadoApiResponseDTO empleado = empleadoService.findByIdApi(idPersona);
		         return ResponseEntity.ok(empleado);
		     } catch (EmpleadoNotFoundException e) {
		         return ResponseEntity.notFound().build();
		     } catch (Exception e) {
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		     }
	    }
    
	    @Operation(summary = "Crear nuevo empleado", 
	               description = "Crea un nuevo empleado en el sistema")
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente"),
	        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
	        @ApiResponse(responseCode = "409", description = "Conflicto: DNI o username ya existe"),
	        @ApiResponse(responseCode = "403", description = "No autorizado")
	    })
	    @PostMapping
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<?> createEmpleado(@Valid @RequestBody EmpleadoApiRequestDTO empleadoApiRequestDTO) {
	    	//log.info("Datos recibidos en la solicitud: {}", empleadoApiRequestDTO);
	    	try {
	            // Validación de DNI duplicado
	        	String dniStr = empleadoApiRequestDTO.dni();
	        	int dni = Integer.parseInt(dniStr);
	            if (personaRepository.existsByDni(dni)) {
	                return ResponseEntity.status(HttpStatus.CONFLICT)
	                    .body(new ErrorResponse("Ya existe una persona con el DNI: " + empleadoApiRequestDTO.dni()));
	            }
	            
	            // Validación de username duplicado
	            if (userRepository.existsByUsername(empleadoApiRequestDTO.username())) {
	                return ResponseEntity.status(HttpStatus.CONFLICT)
	                    .body(new ErrorResponse("Ya existe una persona con el username: " + empleadoApiRequestDTO.username()));
	            }
	            
	            EmpleadoApiResponseDTO empleadoCreado = empleadoService.saveApi(empleadoApiRequestDTO);
	            return ResponseEntity.status(HttpStatus.CREATED).body(empleadoCreado);
	            
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ErrorResponse("Error interno del servidor: " + e.getMessage()));
	        }
	    }
	    
	    // Clase interna para respuestas de error
	    public static class ErrorResponse {
	        private String message;
	        private long timestamp;

	        public ErrorResponse(String message) {
	            this.message = message;
	            this.timestamp = System.currentTimeMillis();
	        }

	        public String getMessage() {
	            return message;
	        }

	        public void setMessage(String message) {
	            this.message = message;
	        }

	        public long getTimestamp() {
	            return timestamp;
	        }

	        public void setTimestamp(long timestamp) {
	            this.timestamp = timestamp;
	        }
	    }
	    
}
