package oo2.grupo5.turnos.entities;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idUser;
	
	@NotBlank
	@Column(name="username", nullable=false, length=80)
	private String username;
	
	
	@NotBlank
	@Column(name="password", nullable=false, length=80)
	private String password;
	
	@Column(name="active_user", nullable=false)
	private boolean active;
	
	@Column(name="create_at_user")
	@CreationTimestamp
	private Timestamp createAt;
	
	@Column(name="update_at_user")
	@UpdateTimestamp
	private Timestamp updateAt;
	
	@NotNull
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name="id_user"),
			inverseJoinColumns = @JoinColumn(name="id_role")
	)
	private Set<Role> roleEntities;
	
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoleEntities().stream()
                .map((Role role) -> new SimpleGrantedAuthority(role.getType().getPrefixedName()))
                .collect(Collectors.toSet());
    }
    
    @OneToOne
    @JoinColumn(name="idPersona", referencedColumnName="idPersona")
    private Persona persona;
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
