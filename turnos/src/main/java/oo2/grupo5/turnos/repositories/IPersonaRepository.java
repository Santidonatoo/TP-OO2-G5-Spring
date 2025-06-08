package oo2.grupo5.turnos.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oo2.grupo5.turnos.entities.Persona;

@Repository
public interface IPersonaRepository extends JpaRepository<Persona, Integer> {
	
	//Search Persona with softDeleted = False
	Page<Persona> findAllBySoftDeletedFalse(Pageable pageable);
	
	//Search Persona with softDeleted = False by ID
	Optional<Persona> findByIdPersonaAndSoftDeletedFalse(Integer idPersona);
	
	boolean existsByDni(int dni);
	
	
}
