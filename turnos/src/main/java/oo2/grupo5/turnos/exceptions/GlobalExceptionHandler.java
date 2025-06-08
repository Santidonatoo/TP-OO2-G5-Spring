package oo2.grupo5.turnos.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
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
	@ExceptionHandler(UbicacionNotFoundException.class)
    public String handleUbicacionNotFoundException(UbicacionNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return ViewRouteHelper.ERROR_NOT_FOUND_UBICACION;
    }

	@ExceptionHandler(NoDisponibilidadException.class)
    public ModelAndView handleNoDisponibilidadException(NoDisponibilidadException ex) {
        ModelAndView mav = new ModelAndView(ViewRouteHelper.TURNO_FECHA);
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

	@ExceptionHandler(DniDuplicadoException.class)
	public String handleDniDuplicadoException(DniDuplicadoException ex, Model model) {
	    model.addAttribute("errorMessage", ex.getMessage());
	    return ViewRouteHelper.ERROR_DNI_REGISTRO; 
	  }
	/*
	@ExceptionHandler(HorarioDisponibilidadInvalidoException.class)
    public ModelAndView handleHorarioInvalidoException(HorarioDisponibilidadInvalidoException ex) {
        ModelAndView mav = new ModelAndView(ViewRouteHelper.ERROR_INVALID_HORARIO);
        mav.addObject("mensajeError", ex.getMessage());
        return mav;
    }
	
	@ExceptionHandler(HorarioDisponibilidadInvalidoException.class)
    public ModelAndView handleHorarioInvalidoExceptionEnEdicion(HorarioDisponibilidadInvalidoException ex) {
        ModelAndView mav = new ModelAndView("error/error-edit-horario"); // Redirige a la nueva vista
        mav.addObject("mensajeError", ex.getMessage());
        return mav;
    }
    */
	
	@ExceptionHandler(HorarioDisponibilidadInvalidoException.class)
	public ModelAndView handleHorarioDisponibilidadInvalidoException(HorarioDisponibilidadInvalidoException ex, HttpServletRequest request) {
	    ModelAndView mav = new ModelAndView();
	    mav.addObject("mensajeError", ex.getMessage());
	    
	    String requestUri = request.getRequestURI(); // Obtiene la URL que originó el error
	    String idServicio = null;

	 // Verifica si el error ocurrió en edición y extrae el ID del servicio
	    if (requestUri.contains("/servicio/edit") || requestUri.contains("/servicio/update/")) {
	        mav.setViewName(ViewRouteHelper.ERROR_EDIT_HORARIO);

	        // Extrae el ID si la URL contiene "/servicio/update/{id}"
	        if (requestUri.startsWith("/servicio/update/")) {
	            idServicio = requestUri.replace("/servicio/update/", ""); // Extrae el número de la URL
	        } else if (requestUri.startsWith("/servicio/edit/")) {
	            idServicio = requestUri.replace("/servicio/edit/", "");
	        }

	        // Agrega el ID del servicio a la vista, para que el botón pueda redirigir correctamente
	        mav.addObject("servicioId", idServicio);
	    } else {
	        mav.setViewName(ViewRouteHelper.ERROR_INVALID_HORARIO);
	    }

	    return mav;
	}
	
}
