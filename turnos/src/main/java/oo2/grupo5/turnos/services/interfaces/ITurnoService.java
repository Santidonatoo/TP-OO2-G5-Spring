package oo2.grupo5.turnos.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import oo2.grupo5.turnos.dtos.requests.TurnoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.TurnoResponseDTO;

public interface ITurnoService {

	TurnoResponseDTO save(TurnoRequestDTO turnoRequestDTO);
	
	TurnoResponseDTO findById(Integer idTurno);
	
	Page<TurnoResponseDTO> findAll(Pageable pageable);
	
	TurnoResponseDTO update(Integer idTurno, TurnoRequestDTO turnoRequestDTO);
	
	void cancelById(Integer idTurno);
	
	void deleteById(Integer idTurno);

}
