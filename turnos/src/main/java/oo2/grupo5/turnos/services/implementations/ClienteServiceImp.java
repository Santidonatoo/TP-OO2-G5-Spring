package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.ClienteRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ClienteResponseDTO;
import oo2.grupo5.turnos.entities.Cliente;
import oo2.grupo5.turnos.repositories.IClienteRepository;
import oo2.grupo5.turnos.services.interfaces.IClienteService;

@Service
public class ClienteServiceImp implements IClienteService{
	
	private final IClienteRepository clienteRepository;
	private final ModelMapper modelMapper;
	
	public ClienteServiceImp(IClienteRepository clienteRepository, ModelMapper modelMapper) {
		this.clienteRepository = clienteRepository;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public ClienteResponseDTO save(ClienteRequestDTO clienteRequestDTO) {
		Cliente cliente = modelMapper.map(clienteRequestDTO, Cliente.class);
    	Cliente saved = clienteRepository.save(cliente);
    	return modelMapper.map(saved, ClienteResponseDTO.class);
	}
	
	@Override
	public ClienteResponseDTO findById(Integer idPersona) {
		Cliente cliente = clienteRepository.findById(idPersona)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cliente with id {0} not found",idPersona)));
        return modelMapper.map(cliente, ClienteResponseDTO.class);
	}
	
	@Override
	public ClienteResponseDTO findByIdNotDeleted(Integer idPersona) {
		Cliente cliente = clienteRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
    			.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cliente with id {0} not found", idPersona)));
    	return modelMapper.map(cliente, ClienteResponseDTO.class);
	}
	
	@Override
    public Page<ClienteResponseDTO> findAll(Pageable pageable) {
    	return clienteRepository.findAll(pageable)
    			.map(entity -> modelMapper.map(entity, ClienteResponseDTO.class));
    }
	
	@Override
    public Page<ClienteResponseDTO> findAllNotDeleted(Pageable pageable) {
    	return clienteRepository.findAllBySoftDeletedFalse(pageable)
    			.map(entity -> modelMapper.map(entity, ClienteResponseDTO.class));
    }
	
	@Override
	public ClienteResponseDTO update(Integer idPersona, ClienteRequestDTO clienteRequestDTO) {
		Cliente cliente = clienteRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cliente with id {0} not found",idPersona)));
		
        cliente.setNombre(clienteRequestDTO.getNombre());
        cliente.setApellido(clienteRequestDTO.getApellido());
        cliente.setDni(clienteRequestDTO.getDni());
        cliente.setFechaDeNacimiento(clienteRequestDTO.getFechaDeNacimiento());

        Cliente updated = clienteRepository.save(cliente);
        return modelMapper.map(updated, ClienteResponseDTO.class);
	}
	
	@Override
	public void deleteById(Integer idPersona) {
		Cliente cliente = clienteRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
    			.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cliente with id {0} not found", idPersona)));
    	
    	cliente.setSoftDeleted(true);
    	clienteRepository.save(cliente);
	}
	
	@Override
	public ClienteResponseDTO restoreById(Integer idPersona) {
		Cliente cliente = clienteRepository.findById(idPersona)
    			.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cliente with id {0} not found", idPersona)));
    	
    	if(!cliente.isSoftDeleted()) {
    		throw new IllegalStateException(MessageFormat.format("Cliente with id {0} not found", idPersona));
    	}
    	
    	cliente.setSoftDeleted(false);
    	Cliente restored = clienteRepository.save(cliente);
    	return modelMapper.map(restored, ClienteResponseDTO.class);
	}
}
