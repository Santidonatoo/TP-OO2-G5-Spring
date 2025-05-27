package oo2.grupo5.turnos.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DuracionServicioValidator implements ConstraintValidator<DuracionServicio, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value >= 15 && value % 15 == 0; 
    }

}
