package oo2.grupo5.turnos.services.interfaces;

import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;

import oo2.grupo5.turnos.dtos.requests.DisponibilidadRequestDTO;
import oo2.grupo5.turnos.dtos.responses.DisponibilidadResponseDTO;

public interface IDisponibilidadService {

	void deleteById(Integer idDisponibilidad);
	
	Page <DisponibilidadResponseDTO> findAllNotDeleted(Pageable pageable);
}
