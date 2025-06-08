package oo2.grupo5.turnos.exceptions;

public class EmpleadoNotFoundException extends RuntimeException{	
	public EmpleadoNotFoundException(Integer idPersona) {
        super("El empleado con ID " + idPersona + " no existe.");
	}
}
