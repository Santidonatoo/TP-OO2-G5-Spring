package oo2.grupo5.turnos.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import oo2.grupo5.turnos.dtos.requests.ClienteApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.ClienteRequestDTO;
import oo2.grupo5.turnos.dtos.requests.EmpleadoApiRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ClienteApiResponseDTO;
import oo2.grupo5.turnos.dtos.responses.ClienteResponseDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoApiResponseDTO;

public interface IClienteService {
	
	ClienteResponseDTO save(ClienteRequestDTO clienteRequestDTO);
	
	ClienteResponseDTO findById(Integer idPersona);
	
	//Find By Id not deleted
	ClienteResponseDTO findByIdNotDeleted(Integer idPersona);
	
	//FindAll
	Page<ClienteResponseDTO> findAll(Pageable pageable, String sortBy);
	
	//FindAll not deleted
	Page<ClienteResponseDTO> findAllNotDeleted(Pageable pageable);
	
	ClienteResponseDTO update(Integer idPersona, ClienteRequestDTO clienteRequestDTO);
	
	void deleteById(Integer idPersona);
	
	ClienteResponseDTO restoreById(Integer idPersona);
	
	void actualizarUltimoInicioSesion(Integer idPersona);
	
	void actualizarUltimoInicioSesionPorUsername(String username);
	
	//FindAll  for Api Rest Controller
	List<ClienteApiResponseDTO> findAllApi(String sortBy);
	//Save for Api Rest Controller
	ClienteApiResponseDTO saveApi(ClienteApiRequestDTO clienteApiRequestDTO);
	
	ClienteApiResponseDTO findByIdApi(Integer idPersona);
	
	
		
	
}