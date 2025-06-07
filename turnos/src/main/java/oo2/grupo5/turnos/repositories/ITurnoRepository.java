package oo2.grupo5.turnos.repositories;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oo2.grupo5.turnos.entities.Empleado;
import oo2.grupo5.turnos.entities.Servicio;
import oo2.grupo5.turnos.entities.Turno;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Integer> {
	
	//Trae los turnos con un cierto empleado, servicio y fecha
	Page<Turno> findByDatosTurno_EmpleadoAndDatosTurno_Fecha(Empleado empleado, LocalDate fecha, Pageable pageable);
	
	Page<Turno> findByDatosTurno_ServicioAndDatosTurno_Fecha(Servicio servicio, LocalDate fecha, Pageable pageable);
	
}
