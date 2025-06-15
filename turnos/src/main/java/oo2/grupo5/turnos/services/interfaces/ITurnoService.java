package oo2.grupo5.turnos.services.interfaces;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import oo2.grupo5.turnos.dtos.requests.DatosTurnoApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.TurnoApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.TurnoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.TurnoApiResponseDTO;
import oo2.grupo5.turnos.dtos.responses.TurnoResponseDTO;

public interface ITurnoService {

	TurnoResponseDTO save(TurnoRequestDTO turnoRequestDTO);
	
	public TurnoApiResponseDTO saveApi(TurnoApiRequestDTO turnoApiRequestDTO, DatosTurnoApiRequestDTO datosTurnoRequestDTO);
	
	TurnoResponseDTO findById(Integer idTurno);
	
	TurnoApiResponseDTO findByIdApi(Integer idTurno);
	
	Page<TurnoResponseDTO> findAll(Pageable pageable);
	
	TurnoResponseDTO update(Integer idTurno, TurnoRequestDTO turnoRequestDTO);
	
	void cancelById(Integer idTurno);
	
	void deleteById(Integer idTurno);
	
	public Page<TurnoResponseDTO> findTurnosByEmpleadoFecha(Pageable pageable, Integer idEmpleado, LocalDate fecha);
	
	public Page<TurnoResponseDTO> findTurnosByServicioFecha(Pageable pageable, Integer idServicio, LocalDate fecha);
	
	public Page<TurnoResponseDTO> findTurnosByPersona(Pageable pageable, Integer idPersona);
	
	public Page<TurnoApiResponseDTO> findAllApiPaginated(int page, int size, String sortBy, String sortDir);
	
}
