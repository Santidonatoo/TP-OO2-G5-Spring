package oo2.grupo5.turnos.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DuracionServicioValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DuracionServicio {
	String message() default "La duración debe ser un múltiplo de 15";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
