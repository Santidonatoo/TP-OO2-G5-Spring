package oo2.grupo5.turnos.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oo2.grupo5.turnos.entities.Cliente;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Integer>{
	
	//Search Cliente with softDeleted = False
	Page<Cliente> findAllBySoftDeletedFalse(Pageable pageable);
	
	//Search Cliente with softDeleted = False by ID
	Optional<Cliente> findByIdPersonaAndSoftDeletedFalse(Integer idPersona);
}
