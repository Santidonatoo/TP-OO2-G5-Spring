package oo2.grupo5.turnos.exceptions;

public class DniDuplicadoException extends RuntimeException {
    public DniDuplicadoException(int dni) {
        super("Ya existe una persona registrada con el DNI: " + dni);
    }
}