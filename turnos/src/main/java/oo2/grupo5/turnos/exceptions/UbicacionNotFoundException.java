package oo2.grupo5.turnos.exceptions;

public class UbicacionNotFoundException extends RuntimeException{	
	public UbicacionNotFoundException(Integer idUbicacion) {
        super("El ubicacion con ID " + idUbicacion + " no existe.");
    }
}
