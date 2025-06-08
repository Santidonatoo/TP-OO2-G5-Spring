package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.ClienteRequestDTO;
import oo2.grupo5.turnos.dtos.requests.RegistroClienteRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ClienteResponseDTO;
import oo2.grupo5.turnos.dtos.responses.RegistroClienteResponseDTO;
import oo2.grupo5.turnos.entities.Cliente;
import oo2.grupo5.turnos.entities.Contacto;
import oo2.grupo5.turnos.entities.Role;
import oo2.grupo5.turnos.entities.User;
import oo2.grupo5.turnos.enums.RoleType;
import oo2.grupo5.turnos.repositories.IClienteRepository;
import oo2.grupo5.turnos.repositories.IPersonaRepository;
import oo2.grupo5.turnos.repositories.IRoleRepository;
import oo2.grupo5.turnos.repositories.IUserRepository;
import oo2.grupo5.turnos.services.interfaces.IClienteService;

@Service
public class ClienteServiceImp implements IClienteService{
	
	private final IClienteRepository clienteRepository;
	private final ModelMapper modelMapper;
	private final IUserRepository userRepository;
	private final IRoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final IPersonaRepository personaRepository;
	
	public ClienteServiceImp(IClienteRepository clienteRepository, ModelMapper modelMapper, IUserRepository userRepository,
			IRoleRepository roleRepository, PasswordEncoder passwordEncoder, IPersonaRepository personaRepository) {
		this.clienteRepository = clienteRepository;
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.personaRepository = personaRepository;
	}
	
	public void registrarCliente(RegistroClienteRequestDTO dto) {
		Role rolCliente = roleRepository.findByType(RoleType.CLIENT).orElseThrow();
		
    	if (personaRepository.existsByDni(dto.getDni())) {
	        throw new IllegalArgumentException("Ya existe una persona con el mismo dni.");
    	}
    	
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre de usuario.");
        }
		
		//Creo un contacto
		Contacto contacto = Contacto.builder()
	            .email(dto.getContacto().getEmail())
	            .telefono(dto.getContacto().getTelefono())
	            .build();
		
		
		//Creo un nuevo cliente
		Cliente cliente = new Cliente();
		cliente.setNombre(dto.getNombre());
		cliente.setApellido(dto.getApellido());
		cliente.setDni(dto.getDni());
		cliente.setFechaDeNacimiento(dto.getFechaDeNacimiento());
		cliente.setContacto(contacto);
		
		//Guardo el nuevo cliente
		cliente = clienteRepository.save(cliente);
		
		//Creo un nuevo user relacionado con el nuevo cliente
		User user = User.builder()
				.username(dto.getUsername())
				.password(passwordEncoder.encode(dto.getPassword()))
				.active(true)
				.roleEntities(Set.of(rolCliente))
				.persona(cliente)
				.build();
		
		//Guardo el nuevo user con el cliente relacionado
		userRepository.save(user);
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
		
		if (personaRepository.existsByDni(clienteRequestDTO.getDni())&&
    		    personaRepository.findById(idPersona).get().getDni() != clienteRequestDTO.getDni()) {
	        throw new IllegalArgumentException("Ya existe una persona con el mismo dni.");
	    }
		
        cliente.setNombre(clienteRequestDTO.getNombre());
        cliente.setApellido(clienteRequestDTO.getApellido());
        cliente.setDni(clienteRequestDTO.getDni());
        cliente.setFechaDeNacimiento(clienteRequestDTO.getFechaDeNacimiento());
        
        //recordar que por default cliente siempre tiene un contacto, por eso se da por sentado que el contacto no es nulo 
        if (clienteRequestDTO.getContacto() != null) {
            cliente.getContacto().setEmail(clienteRequestDTO.getContacto().getEmail());
            cliente.getContacto().setTelefono(clienteRequestDTO.getContacto().getTelefono());
        }
        

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
