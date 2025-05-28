package oo2.grupo5.turnos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oo2.grupo5.turnos.entities.Role;
import oo2.grupo5.turnos.enums.RoleType;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
	
	Optional<Role> findById(Integer integer);
	
	Optional<Role> findByType(RoleType type);
}
