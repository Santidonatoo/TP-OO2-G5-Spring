package oo2.grupo5.turnos.controllers.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import oo2.grupo5.turnos.dtos.requests.LoginRequestDTO;
import oo2.grupo5.turnos.dtos.responses.LoginResponseDTO;
import oo2.grupo5.turnos.entities.User;
import oo2.grupo5.turnos.services.implementations.ClienteServiceImp;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación de usuarios")
public class AuthRestController {
	
	private final AuthenticationManager authenticationManager;
	private final ClienteServiceImp clienteService;
	
	public AuthRestController(AuthenticationManager authenticationManager, ClienteServiceImp clienteService){
		this.authenticationManager = authenticationManager;
		this.clienteService = clienteService;
	}

	@Operation(summary = "Login de usuario", description = "Autentica un usuario y devuelve token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDto) {
		try {
			//Autenticamos usando el AuthenticationManager
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequestDto.username(),
							loginRequestDto.password()
					)
			);
			
			//Establecemos la autenticacion en el contexto de seguridad
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			//Obtenemos info del usuario autenticado
			User user = (User) authentication.getPrincipal();
			String roles = authentication.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
					.reduce((a, b) -> a + ", " + b)
					.orElse("");
			
			//Actualizar ultimo inicio de sesion si es cliente
			//Chequeo si es cliente
			boolean esCliente = authentication.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
					.anyMatch(role -> role.equals("ROLE_CLIENT"));
			
			//Chequeo si empleado o admin
			boolean esEmpleadoOAdmin = authentication.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
					.anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_EMPLOYEE"));
			
			
			if(esCliente && !esEmpleadoOAdmin) {
				try {
					clienteService.actualizarUltimoInicioSesionPorUsername(user.getUsername());
				} catch (Exception e) {
					System.err.println("Error actualizando ultimo inicio de sesion para usuario " + user.getUsername() + 
							": " + e.getMessage());
				}
			}
			
			
			//Generar respuesta exitosa
			String sessionToken = "SESSION_" + System.currentTimeMillis() + "_" + user.getUsername();
			
			return ResponseEntity.ok(new LoginResponseDTO(
					sessionToken,
					"Login exitoso, Roles: " + roles,
					true
			));
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(
					null,
					"Credenciales invalidas",
					false
			));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponseDTO(
					null,
					"Error interno del servidor",
					false
			));
		}	
	}
	
	
}
