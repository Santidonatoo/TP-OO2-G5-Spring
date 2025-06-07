package oo2.grupo5.turnos.entities;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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

public class Disponibilidad {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idDisponibilidad")
	@Setter(AccessLevel.NONE)
	private Integer idDisponibilidad;
	
	@NotNull(message = "The dayWeek cannot be empty")
	@Column(name = "diaSemana", nullable = false)    
	private DayOfWeek diaSemana;
	
	
	@NotNull(message = "The startTime cannot be empty")
	@Column(name = "horaInicio", nullable = false)    
	private LocalTime horaInicio;
	
	@NotNull(message = "The endTime cannot be empty")
	@Column(name = "horaFin", nullable = false)    
	private LocalTime horaFin;
	
	@ManyToMany(mappedBy = "listaDisponibilidades")
    private Set<Servicio> listaServicios;

	
	@Column(name = "soft_deleted", nullable = false)
	@Builder.Default
	protected boolean softDeleted = false;
	
	@Column(name = "create_at")
	@CreationTimestamp
	protected Timestamp createAt;
	
	@Column(name = "update_at")
	@UpdateTimestamp
	protected Timestamp updateAt;
	
	
}
