package oo2.grupo5.turnos.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import oo2.grupo5.turnos.dtos.requests.DatosTurnoApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.DatosTurnoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.DatosTurnoApiResponseDTO;
import oo2.grupo5.turnos.dtos.responses.DatosTurnoResponseDTO;

public interface IDatosTurnoService {
	
	DatosTurnoResponseDTO save(DatosTurnoRequestDTO datosTurnoRequestDTO);
	
	DatosTurnoApiResponseDTO saveDatosTurnoApi(DatosTurnoApiRequestDTO datosTurnoRequestDTO);
	
	DatosTurnoResponseDTO findById(Integer idDatosTurno);
	
	Page<DatosTurnoResponseDTO> findAll(Pageable pageable);
	
	DatosTurnoResponseDTO update(Integer idDatosTurno, DatosTurnoRequestDTO servicioRequestDTO);
	
	void deleteById(Integer idDatosTurno);
}
