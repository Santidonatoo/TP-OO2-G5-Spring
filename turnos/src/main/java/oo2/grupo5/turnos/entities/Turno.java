package oo2.grupo5.turnos.entities;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oo2.grupo5.turnos.enums.EstadoTurno;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Turno {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idTurno")
	@Setter(AccessLevel.NONE)
	private Integer idTurno;
	
	@NotNull(message = "La hora no puede ser nula")
	@Column (name = "hora", nullable = false)
	private LocalTime hora;
	
    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado no puede ser nulo")
    @Column (name = "estado", nullable = false)
    private EstadoTurno estado;
    
    @NotNull(message = "La relacion con turno no puede ser nula")
    @OneToOne
    @JoinColumn(name = "idDatosTurno")
    private DatosTurno datosTurno;
    
}
