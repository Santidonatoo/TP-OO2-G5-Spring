package oo2.grupo5.turnos.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import oo2.grupo5.turnos.dtos.requests.EmpleadoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoResponseDTO;

public interface IEmpleadoService {
	
	EmpleadoResponseDTO save(EmpleadoRequestDTO empleadoRequestDTO);
	
	EmpleadoResponseDTO findById(Integer idPersona);
	
	//Find By Id not deleted
	EmpleadoResponseDTO findByIdNotDeleted(Integer idPersona);
	
	//FindAll
	Page<EmpleadoResponseDTO> findAll(Pageable pageable);
	
	//FindAll not deleted
	Page<EmpleadoResponseDTO> findAllNotDeleted(Pageable pageable);
	
	EmpleadoResponseDTO update(Integer idPersona, EmpleadoRequestDTO empleadoRequestDTO);
	
	void deleteById(Integer idPersona);
	
	EmpleadoResponseDTO restoreById(Integer idPersona);
}
