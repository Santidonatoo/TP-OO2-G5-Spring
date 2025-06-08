package oo2.grupo5.turnos.exceptions;

public class ClienteNotFoundException extends RuntimeException{	
	public ClienteNotFoundException(Integer idPersona) {
        super("El cliente con ID " + idPersona + " no existe.");
	}
}
