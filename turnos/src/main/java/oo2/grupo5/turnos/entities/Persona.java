package oo2.grupo5.turnos.entities;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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
	@Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
	@Column(name="nombre", length = 50)
	protected String nombre;
	
	@NotBlank(message = "The surname cannot be empty")
	@Size(min = 3, max = 50, message = "El apellido debe tener entre 3 y 50 caracteres")
	@Column(name="apellido", length = 50)
	protected String apellido;
	
	@NotNull (message = "The DNI cannot be empty")
	@Min(value = 1000000, message = "El DNI debe tener al menos 7 digitos")
	@Max(value= 99999999, message = "El DNI no puede tener mas de 8 digitos")
	@Column(name="dni", unique = true)
	protected int dni;
	
	@NotNull(message = "La fecha de nacimiento no puede ser nula")
	@Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
	@Column(name="fechaDeNacimiento")
	protected LocalDate fechaDeNacimiento;
	
	@OneToOne(mappedBy="persona")
	private User user;
	
	@Column(name = "soft_deleted", nullable = false)
	@Builder.Default
	protected boolean softDeleted = false;
	
	@Column(name = "create_at")
	@CreationTimestamp
	protected Timestamp createAt;
	
	@Column(name = "update_at")
	@UpdateTimestamp
	protected Timestamp updateAt;
	
	@Valid
	@NotNull(message = "El Contacto no puede ser nulo")
	//con esto marco que la idPersona es la clave foranea de Contacto 
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idContacto", referencedColumnName = "idCont")
	private Contacto contacto;
	
}
