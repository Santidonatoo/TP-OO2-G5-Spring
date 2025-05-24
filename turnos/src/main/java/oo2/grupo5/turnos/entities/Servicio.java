package oo2.grupo5.turnos.entities;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
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
public class Servicio {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idServicio")
    @Setter(AccessLevel.NONE)
    private Integer idServicio;
	
	@NotBlank(message = "The name cannot be empty")
    @Column(name = "nombre", nullable = false)
    private String nombre;

	@Column(name = "requiereEmpleado", nullable = false)
	private boolean requiereEmpleado;
	
	@Column(name = "duracion", nullable = false)
	private int duracion;
	
	
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
