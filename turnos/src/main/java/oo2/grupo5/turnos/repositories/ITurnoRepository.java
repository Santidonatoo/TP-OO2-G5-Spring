package oo2.grupo5.turnos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oo2.grupo5.turnos.entities.Turno;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Integer> {
	
}
