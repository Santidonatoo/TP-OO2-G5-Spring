package oo2.grupo5.turnos.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import oo2.grupo5.turnos.dtos.requests.PersonaRequestDTO;
import oo2.grupo5.turnos.dtos.responses.PersonaResponseDTO;

public interface IPersonaService {
	
	PersonaResponseDTO save(PersonaRequestDTO personaRequestDTO);
	
	PersonaResponseDTO findById(Integer idPersona);
	
	//Find By Id not deleted
	PersonaResponseDTO findByIdNotDeleted(Integer idPersona);
	
	//FindAll
	Page<PersonaResponseDTO> findAll(Pageable pageable);
	
	//FindAll not deleted
	Page<PersonaResponseDTO> findAllNotDeleted(Pageable pageable);
	
	PersonaResponseDTO update(Integer idPersona, PersonaRequestDTO personaRequestDTO);
	
	void deleteById(Integer idPersona);
	
	PersonaResponseDTO restoreById(Integer idPersona);
}
