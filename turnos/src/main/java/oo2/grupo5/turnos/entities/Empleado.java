package oo2.grupo5.turnos.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
	
	@NotBlank(message = "El puesto no puede estar vacio")
	@Size(min = 3, max = 100, message = "El puesto debe tener entre 3 y 100 caracteres")
	@Column(name="puesto", nullable=true, length = 100)
	private String puesto;
	
	@ManyToMany
	@JoinTable(name = "servicioxempleado", 
	  joinColumns = @JoinColumn(name = "idEmpleado"), 
	  inverseJoinColumns = @JoinColumn(name = "idServicio"))
	private Set<Servicio> listaServicios;
}
