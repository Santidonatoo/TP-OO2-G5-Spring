package oo2.grupo5.turnos.entities;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="contacto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contacto {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="idCont")
	@Setter(AccessLevel.NONE)
	protected Integer idContacto;
	
	@NotBlank(message = "The email cannot be empty")
	@Email(message = "You must enter a valid email address")
	@Column(name="email")
	protected String email;
	
	@NotBlank(message = "The phone number cannot be empty")
	@Pattern(regexp = "^\\d{8,15}$", message = "The phone number should only contain numbers")
	@Column(name="telefono")
	protected String telefono;
	
	@Column(name = "create_at")
	@CreationTimestamp
	protected Timestamp createAt;
	
	@Column(name = "update_at")
	@UpdateTimestamp
	protected Timestamp updateAt;
	
	
}
