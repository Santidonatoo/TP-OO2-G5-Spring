package oo2.grupo5.turnos.entities;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.validation.constraints.NotNull;
import oo2.grupo5.turnos.enums.RoleType;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name="roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_role")
	@Setter(AccessLevel.NONE)
	private Integer idRole;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name="name_role", nullable=false, length=80, unique=true)
	private RoleType type;
	
	@Column(name = "create_at_role")
	@CreationTimestamp
	private Timestamp createAt;
	
	@Column(name="update_at_role")
	@UpdateTimestamp
	private Timestamp updateAt;
	
	public Role(@NotNull RoleType type) {
		this.type = type;
	}
}
