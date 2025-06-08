package oo2.grupo5.turnos.exceptions;

public class ServicioNotFoundException extends RuntimeException{	
	public ServicioNotFoundException(Integer idServicio) {
        super("El servicio con ID " + idServicio + " no existe.");
    }
}
