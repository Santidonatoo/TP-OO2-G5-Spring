package oo2.grupo5.turnos.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import oo2.grupo5.turnos.dtos.requests.EmpleadoApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.EmpleadoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoApiResponseDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoResponseDTO;

public interface IEmpleadoService {
	
	EmpleadoResponseDTO save(EmpleadoRequestDTO empleadoRequestDTO);
	
	//Save for Api Rest Controller
	EmpleadoApiResponseDTO saveApi(EmpleadoApiRequestDTO empleadoApiRequestDTO);
	
	EmpleadoResponseDTO findById(Integer idPersona);
	
	//Find By Id for Api Rest Controller
	EmpleadoApiResponseDTO findByIdApi(Integer idPersona); 
	
	//Find By Id not deleted
	EmpleadoResponseDTO findByIdNotDeleted(Integer idPersona);
	
	//FindAll
	Page<EmpleadoResponseDTO> findAll(Pageable pageable, String sortBy);
	
	
	//FindAll for Api Rest Controller
	List<EmpleadoApiResponseDTO> findAllApi(String sortBy);
	
	//FindAll not deleted
	Page<EmpleadoResponseDTO> findAllNotDeleted(Pageable pageable);
	
	//FindAll not deleted for Api Rest Controller
	Page<EmpleadoApiResponseDTO> findAllNotDeletedApi(Pageable pageable);
	
    Page<EmpleadoResponseDTO> findAllEmpleadosbyServicio(Integer idServicio, Pageable pageable);

	
	EmpleadoResponseDTO update(Integer idPersona, EmpleadoRequestDTO empleadoRequestDTO);
	
	void deleteById(Integer idPersona);
	
	EmpleadoResponseDTO restoreById(Integer idPersona);
	
}
