package oo2.grupo5.turnos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oo2.grupo5.turnos.entities.DatosTurno;

@Repository
public interface IDatosTurnoRepository extends JpaRepository<DatosTurno, Integer> {
	
}
