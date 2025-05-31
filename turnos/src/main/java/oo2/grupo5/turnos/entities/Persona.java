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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	@Column(name="nombre")
	protected String nombre;
	
	@NotBlank(message = "The surname cannot be empty")
	@Column(name="apellido")
	protected String apellido;
	
	@NotNull
	@Column(name="dni")
	protected int dni;
	
	@NotNull
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
	
	//con esto marco que la idPersona es la clave foranea de Contacto 
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idContacto", referencedColumnName = "idCont")
	private Contacto contacto;
	
}
