package oo2.grupo5.turnos.entities;

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
@DiscriminatorValue("cliente")
public class Cliente extends Persona {
	//Si despues agregamos algun atributo, habria que ponerle el AllArgsConstructor y cambiar tambien los dtos
}
