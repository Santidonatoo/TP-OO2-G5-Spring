package oo2.grupo5.turnos.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import oo2.grupo5.turnos.dtos.requests.ClienteRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ClienteResponseDTO;

public interface IClienteService {
	
	ClienteResponseDTO save(ClienteRequestDTO clienteRequestDTO);
	
	ClienteResponseDTO findById(Integer idPersona);
	
	//Find By Id not deleted
	ClienteResponseDTO findByIdNotDeleted(Integer idPersona);
	
	//FindAll
	Page<ClienteResponseDTO> findAll(Pageable pageable);
	
	//FindAll not deleted
	Page<ClienteResponseDTO> findAllNotDeleted(Pageable pageable);
	
	ClienteResponseDTO update(Integer idPersona, ClienteRequestDTO clienteRequestDTO);
	
	void deleteById(Integer idPersona);
	
	ClienteResponseDTO restoreById(Integer idPersona);
	
	void actualizarUltimoInicioSesion(Integer idPersona);
	
	void actualizarUltimoInicioSesionPorUsername(String username);
}