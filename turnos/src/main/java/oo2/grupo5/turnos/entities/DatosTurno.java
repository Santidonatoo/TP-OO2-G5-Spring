package oo2.grupo5.turnos.entities;

import java.time.LocalDate;	

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DatosTurno {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idDatosTurno")
	@Setter(AccessLevel.NONE)
	private Integer idDatosTurno;
	
	@NotNull(message = "La fecha no puede ser nula")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "idCliente", nullable = false)
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name = "idEmpleado")
	private Empleado empleado;
	
	@NotNull
	@ManyToOne
    @JoinColumn(name = "servicioId", nullable = false)
    private Servicio servicio;
	
}
