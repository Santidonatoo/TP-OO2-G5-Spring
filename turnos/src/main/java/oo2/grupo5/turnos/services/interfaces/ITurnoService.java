package oo2.grupo5.turnos.services.interfaces;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import oo2.grupo5.turnos.dtos.requests.TurnoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoResponseDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;
import oo2.grupo5.turnos.dtos.responses.TurnoResponseDTO;
import oo2.grupo5.turnos.entities.Empleado;
import oo2.grupo5.turnos.entities.Servicio;

public interface ITurnoService {

	TurnoResponseDTO save(TurnoRequestDTO turnoRequestDTO);
	
	TurnoResponseDTO findById(Integer idTurno);
	
	Page<TurnoResponseDTO> findAll(Pageable pageable);
	
	TurnoResponseDTO update(Integer idTurno, TurnoRequestDTO turnoRequestDTO);
	
	void cancelById(Integer idTurno);
	
	void deleteById(Integer idTurno);
	
	public Page<TurnoResponseDTO> findTurnosByEmpleadoFecha(Pageable pageable, Integer idEmpleado, LocalDate fecha);
	
	public Page<TurnoResponseDTO> findTurnosByServicioFecha(Pageable pageable, Integer idServicio, LocalDate fecha);

}
