package oo2.grupo5.turnos.repositories;


import org.springframework.data.domain.Pageable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oo2.grupo5.turnos.entities.Disponibilidad;


@Repository
public interface IDisponibilidadRepository extends JpaRepository<Disponibilidad,Integer>{
	
	//traigo lista de disponibilidades de un dia de la semana
	List<Disponibilidad> findByDiaSemana(DayOfWeek diaSemana);
	
	//trae lista de disponibilidades de una servicio (esto le deberia seguir a jose)
	List<Disponibilidad> findByListaServiciosIdServicio(Integer idServicio);
	
	Optional<Disponibilidad> findByIdDisponibilidadAndSoftDeletedFalse(Integer idDisponibilidad);
	
	//checkea si ya existe una disponibilidad con esa hora de inicio y fin en ese dia de la semana
	boolean existsByDiaSemanaAndHoraInicioAndHoraFin(DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin);
	
	//boolean existsById(Integer idDisponibilidad);
	
	//si existe una disponibilidad con los parametros indicados, la devuelve
	Optional<Disponibilidad> findByDiaSemanaAndHoraInicioAndHoraFin(DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin);
	
	Page <Disponibilidad> findAllBySoftDeletedFalse(Pageable pageable);
}
