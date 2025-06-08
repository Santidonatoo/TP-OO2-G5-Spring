package oo2.grupo5.turnos.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import oo2.grupo5.turnos.helpers.ViewRouteHelper;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ServicioNotFoundException.class)
    public String handleServicioNotFoundException(ServicioNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return ViewRouteHelper.ERROR_NOT_FOUND_SERVICIO;
    }
	@ExceptionHandler(EmpleadoNotFoundException.class)
    public String handleEmpeladoNotFoundException(EmpleadoNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return ViewRouteHelper.ERROR_NOT_FOUND_EMPLEADO;
    }
	@ExceptionHandler(ClienteNotFoundException.class)
    public String handleClienteNotFoundException(ClienteNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return ViewRouteHelper.ERROR_NOT_FOUND_CLIENTE;
    }
}
