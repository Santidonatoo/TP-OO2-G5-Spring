package oo2.grupo5.turnos.controllers.rest;

import java.util.Enumeration;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import oo2.grupo5.turnos.dtos.requests.CrearTurnoApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.DatosTurnoApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.TurnoApiRequestDTO;
import oo2.grupo5.turnos.dtos.responses.TurnoApiResponseDTO;
import oo2.grupo5.turnos.enums.EstadoTurno;
import oo2.grupo5.turnos.services.interfaces.IDatosTurnoService;
import oo2.grupo5.turnos.services.interfaces.ITurnoService;

@RestController
@RequestMapping("/api/turno")
@Tag(name = "Turnos", description = "Endpoints para gestionar turnos")
public class TurnoRestController {
	
	private final ITurnoService turnoService;
	private final IDatosTurnoService datosTurnoService;
	private final ModelMapper modelMapper;
	
    public TurnoRestController(ITurnoService turnoService, ModelMapper modelMapper, IDatosTurnoService datosTurnoService) {
        this.turnoService = turnoService;
		this.datosTurnoService = datosTurnoService;
		this.modelMapper = modelMapper;
    }
    
    @Operation(summary = "Turno por id", description = "Devuelve un turno por su id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<TurnoApiResponseDTO> getTurnoById(@PathVariable Integer id) {
        try {
            TurnoApiResponseDTO turno = turnoService.findByIdApi(id);
            return ResponseEntity.ok(turno);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Crear un nuevo turno", description = "Crea un turno con sus datos asociados a partir de los datos enviados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Turno creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación o datos incorrectos"),
        @ApiResponse(responseCode = "403", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Cliente, empleado o servicio no encontrado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TurnoApiResponseDTO> crearTurno(
            @Valid @RequestBody CrearTurnoApiRequestDTO dto) {
        
        System.out.println("_____Creando Turno_____");
        System.out.println("Hora: " + dto.hora());
        System.out.println("Estado: " + dto.estado());
        System.out.println("Fecha: " + dto.fecha());
        System.out.println("ID Cliente: " + dto.idCliente());
        System.out.println("ID Empleado: " + dto.idEmpleado());
        System.out.println("ID Servicio: " + dto.idServicio());

        DatosTurnoApiRequestDTO datosTurnoRequest = new DatosTurnoApiRequestDTO(
            dto.fecha(),
            dto.idCliente(),
            dto.idEmpleado(),
            dto.idServicio()
        );
        
        TurnoApiRequestDTO turnoRequest = new TurnoApiRequestDTO(
            dto.hora(),
            dto.estado() != null ? dto.estado() : EstadoTurno.PENDIENTE, 
            null 
        );

        TurnoApiResponseDTO creado = turnoService.saveApi(turnoRequest, datosTurnoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
    
    @Operation(summary = "Lista de turnos paginada", description = "Devuelve una lista paginada de turnos")
    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<Page<TurnoApiResponseDTO>> getAllTurnosPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(
                    description = "Campo por el cual ordenar",
                    schema = @Schema(allowableValues = {"idTurno", "hora"})
                )
            @RequestParam(defaultValue = "idTurno") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<TurnoApiResponseDTO> turnos = turnoService.findAllApiPaginated(page, size, sortBy, sortDir);
            return ResponseEntity.ok(turnos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
