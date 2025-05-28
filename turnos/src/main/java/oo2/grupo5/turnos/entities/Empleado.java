package oo2.grupo5.turnos.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
	
	@ManyToMany
	@JoinTable(name = "servicioxempleado", 
	  joinColumns = @JoinColumn(name = "idEmpleado"), 
	  inverseJoinColumns = @JoinColumn(name = "idServicio"))
	private Set<Servicio> listaServicios;
}
