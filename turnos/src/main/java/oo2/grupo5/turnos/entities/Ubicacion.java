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
import jakarta.persistence.OneToMany;
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
public class Ubicacion {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUbicacion")
    @Setter(AccessLevel.NONE)
	private Integer idUbicacion;
	
	@NotBlank(message = "The localidad cannot be empty")
    @Column(name = "localidad", nullable = false)
	private String localidad;
	
	@NotBlank(message = "The calle cannot be empty")
    @Column(name = "calle", nullable = false)
	private String calle;
	
    @Column(name = "numero", nullable = false)
	private int numero;
	
    @OneToMany(mappedBy = "ubicacion")
    private Set<Servicio> servicios;

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
