package oo2.grupo5.turnos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter @Setter
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("empleado")
public class Empleado extends Persona {
	
	@Column(name="puesto", nullable=true)
	private String puesto;
}
