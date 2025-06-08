package oo2.grupo5.turnos.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oo2.grupo5.turnos.services.implementations.ClienteServiceImp;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private final ClienteServiceImp clienteService;
	
	public CustomAuthenticationSuccessHandler(ClienteServiceImp clienteService) {
		this.clienteService = clienteService;
	}
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        // Obtener el username del usuario autenticado
        String username = authentication.getName();
        
        // Verificar si el usuario tiene rol de cliente (y NO es empleado/admin)
        boolean esCliente = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_CLIENT"));
        
        boolean esEmpleadoOAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_EMPLOYEE"));
        
        // Actualizar último inicio de sesión SOLO si es cliente y NO es empleado/admin
        if (esCliente && !esEmpleadoOAdmin) {
            try {
                clienteService.actualizarUltimoInicioSesionPorUsername(username);
                System.out.println("Último inicio de sesión actualizado para cliente: " + username);
            } catch (Exception e) {
                // Log del error pero no interrumpir el flujo de login
                System.err.println("Error actualizando último inicio de sesión para cliente " + username + ": " + e.getMessage());
            }
        } else if (esEmpleadoOAdmin) {
            System.out.println("Login de empleado/admin: " + username + " - No se actualiza último inicio de sesión");
        }
        
        // Redirigir a /auth/loginSuccess (mantiene la lógica original)
        response.sendRedirect("/auth/loginSuccess");
    }

}
