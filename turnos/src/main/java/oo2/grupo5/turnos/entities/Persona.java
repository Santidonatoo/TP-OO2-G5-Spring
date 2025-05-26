package oo2.grupo5.turnos.entities;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="persona")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipoPersona", discriminatorType = DiscriminatorType.STRING)
public class Persona {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="idPersona")
	@Setter(AccessLevel.NONE)
	protected Integer idPersona;
	
	@NotBlank(message = "The name cannot be empty")
	@Column(name="nombre")
	protected String nombre;
	
	@NotBlank(message = "The surname cannot be empty")
	@Column(name="apellido")
	protected String apellido;
	
	@NotBlank(message = "The dni cannot be empty")
	@Column(name="dni")
	protected int dni;
	
	@NotBlank(message = "The birthday cannot be empty")
	@Column(name="fechaDeNacimiento")
	protected LocalDate fechaDeNacimiento;
	
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
