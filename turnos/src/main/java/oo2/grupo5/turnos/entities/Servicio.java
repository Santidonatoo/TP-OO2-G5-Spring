package oo2.grupo5.turnos.entities;

import java.sql.Timestamp;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oo2.grupo5.turnos.validation.DuracionServicio;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idServicio")
    @Setter(AccessLevel.NONE)
    private Integer idServicio;
	
	@NotBlank(message = "The name cannot be empty")
	@Column(name = "nombre", nullable = false, unique = true)    
	private String nombre;

	@Column(name = "requiereEmpleado", nullable = false)
	private boolean requiereEmpleado;
	
	@DuracionServicio
	@Column(name = "duracion", nullable = false)
	private int duracion;
	
	@ManyToOne
	@JoinColumn(name = "idUbicacion", nullable = false)
	private Ubicacion ubicacion;
	
	@ManyToMany
	@JoinTable(name = "servicioxempleado", 
			  joinColumns = @JoinColumn(name = "idServicio"), 
			  inverseJoinColumns = @JoinColumn(name = "idEmpleado"))
	private Set<Empleado> listaEmpleados;
	
	@Column(name = "soft_deleted", nullable = false)
    @Builder.Default
    private boolean softDeleted = false;

    @Column(name = "create_at")
    @CreationTimestamp
    private Timestamp createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private Timestamp updateAt;
}
