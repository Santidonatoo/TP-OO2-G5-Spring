package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.ClienteApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.ClienteRequestDTO;
import oo2.grupo5.turnos.dtos.requests.EmpleadoApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.RegistroClienteRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ClienteApiResponseDTO;
import oo2.grupo5.turnos.dtos.responses.ClienteResponseDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoApiResponseDTO;
import oo2.grupo5.turnos.entities.Cliente;
import oo2.grupo5.turnos.entities.Contacto;
import oo2.grupo5.turnos.entities.Empleado;
import oo2.grupo5.turnos.entities.Role;
import oo2.grupo5.turnos.entities.User;
import oo2.grupo5.turnos.enums.RoleType;
import oo2.grupo5.turnos.exceptions.ClienteNotFoundException;
import oo2.grupo5.turnos.exceptions.DniDuplicadoException;
import oo2.grupo5.turnos.exceptions.EmpleadoNotFoundException;
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
			IRoleRepository roleRepository, @Lazy PasswordEncoder passwordEncoder, IPersonaRepository personaRepository) {
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
	        throw new DniDuplicadoException(dto.getDni());
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
                .orElseThrow(() -> new ClienteNotFoundException(idPersona));
        return modelMapper.map(cliente, ClienteResponseDTO.class);
	}
	
	@Override
	public ClienteResponseDTO findByIdNotDeleted(Integer idPersona) {
		Cliente cliente = clienteRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
    			.orElseThrow(() -> new ClienteNotFoundException(idPersona));
    	return modelMapper.map(cliente, ClienteResponseDTO.class);
	}
	
	@Override
    public Page<ClienteResponseDTO> findAll(Pageable pageable, String sortBy) {
		Sort sort = switch (sortBy.toLowerCase()) {
        case "apellido" -> Sort.by("apellido").ascending();
        case "dni" -> Sort.by("dni").ascending();
        default -> Sort.by("idPersona").ascending();
    	};
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    	return clienteRepository.findAll(sortedPageable)
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
                .orElseThrow(() -> new ClienteNotFoundException(idPersona));
		
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
    			.orElseThrow(() -> new ClienteNotFoundException(idPersona));
    	
    	cliente.setSoftDeleted(true);
    	clienteRepository.save(cliente);
	}
	
	@Override
	public ClienteResponseDTO restoreById(Integer idPersona) {
		Cliente cliente = clienteRepository.findById(idPersona)
    			.orElseThrow(() -> new ClienteNotFoundException(idPersona));
    	
    	if(!cliente.isSoftDeleted()) {
    		throw new IllegalStateException(MessageFormat.format("Cliente with id {0} not found", idPersona));
    	}
    	
    	cliente.setSoftDeleted(false);
    	Cliente restored = clienteRepository.save(cliente);
    	return modelMapper.map(restored, ClienteResponseDTO.class);
	}
	
	@Transactional
	public void actualizarUltimoInicioSesion(Integer idPersona) {
		 // Verificar que realmente sea un cliente antes de actualizar
	    Optional<Cliente> cliente = clienteRepository.findByIdPersonaAndSoftDeletedFalse(idPersona);
	    if (cliente.isPresent()) {
	        clienteRepository.actualizarUltimoInicioSesion(idPersona, LocalDateTime.now());
	    } else {
	        throw new IllegalArgumentException("No se encontró cliente con ID: " + idPersona);
	    }
	}
	
	@Transactional
	public void actualizarUltimoInicioSesionPorUsername(String username) {
	    Optional<Cliente> cliente = clienteRepository.findByUsername(username);
	    if (cliente.isPresent() && !cliente.get().isSoftDeleted()) {
	        clienteRepository.actualizarUltimoInicioSesion(cliente.get().getIdPersona(), LocalDateTime.now());
	    } else {
	        // No es un error crítico - puede ser que el usuario sea empleado
	        System.out.println("No se encontró cliente activo con username: " + username + 
	                          " (puede ser empleado o cliente inactivo)");
	    }
	}
	
	//METODOS PARA API REST CONTROLLER
	
	public ClienteApiResponseDTO saveApi (ClienteApiRequestDTO clienteApiRequestDTO) {
		
		String dniStr = clienteApiRequestDTO.dni();
		int dni = Integer.parseInt(dniStr);
		
		if (personaRepository.existsByDni(dni)) {
	        throw new IllegalArgumentException("Ya existe una persona con el mismo dni.");
    	}
    	if(userRepository.existsByUsername(clienteApiRequestDTO.username())) {
    		 throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre de usuario.");
    	}
    	
    	Cliente cliente = new Cliente();
    	cliente.setNombre(clienteApiRequestDTO.nombre());
    	cliente.setApellido(clienteApiRequestDTO.apellido());
    	cliente.setDni(Integer.parseInt(clienteApiRequestDTO.dni()));
    	cliente.setUltimoInicioSesion(clienteApiRequestDTO.ultimoInicioSesion());
    	
    	Contacto contacto = Contacto.builder()
    			.email(clienteApiRequestDTO.email())
    			.telefono(clienteApiRequestDTO.telefono())
    			.build();
    	cliente.setContacto(contacto);
    	
    	Cliente saved = clienteRepository.save(cliente);
    	
    	Role rolCliente = roleRepository.findByType(RoleType.CLIENT)
    			.orElseThrow(() -> new RuntimeException("Rol CLIENT no encontrado"));
    	
    	User user = User.builder()
        		.username(clienteApiRequestDTO.username())
        		.password(clienteApiRequestDTO.password())
        		.active(true)
        		.roleEntities(Set.of(rolCliente))        		
        		.persona(saved)
        		.build();
    	
    	 userRepository.save(user);
    	 
    	 return new ClienteApiResponseDTO(
    			 saved.getIdPersona(),
         		saved.getNombre(),
         		saved.getApellido(),
         		String.valueOf(saved.getDni()),
         		contacto.getEmail(),
         		contacto.getTelefono(),
         		user.getUsername(),
         		saved.getUltimoInicioSesion() 
    			 );
    	
	}

	/*
	public Page<ClienteApiResponseDTO> findAllNotDeletedApi(Pageable pageable){
		return clienteRepository.findAllBySoftDeletedFalse(pageable)
    			.map(entity -> modelMapper.map(entity, ClienteApiResponseDTO.class));
	} 
	*/
		
	public ClienteApiResponseDTO findByIdApi(Integer idPersona) {
		
		Cliente cliente = clienteRepository.findById(idPersona)
				.orElseThrow(() -> new ClienteNotFoundException(idPersona));
		
		return new ClienteApiResponseDTO(
				cliente.getIdPersona(),
				cliente.getNombre(),
				cliente.getApellido(),
				String.valueOf(cliente.getDni()),
				cliente.getContacto() != null ? 
						cliente.getContacto().getEmail() : null,
			    cliente.getContacto() != null ? 
			    		cliente.getContacto().getTelefono() : null,
			    cliente.getUser() != null ? 
			                cliente.getUser().getUsername() : null,
			    cliente.getUltimoInicioSesion()
				);
	}
	
	
	@Override
    public List<ClienteApiResponseDTO> findAllApi(String sortBy) {
		
		Sort sort = switch (sortBy.toLowerCase()) {
        case "nombre" -> Sort.by("nombre").ascending();
        case "dni" -> Sort.by("dni").ascending();
        default -> Sort.by("idPersona").ascending();
    };
		
        List<Cliente> clientes = clienteRepository.findAll(sort);

        return clientes.stream()
                .map(cliente -> {

                    return new ClienteApiResponseDTO(
                    		cliente.getIdPersona(),
                    		cliente.getNombre(),
                    		cliente.getApellido(),
                            String.valueOf(cliente.getDni()),
                            cliente.getContacto() != null ?
                            cliente.getContacto().getEmail() : null,
                            	cliente.getContacto() != null ?
                            cliente.getContacto().getTelefono() : null,
                            		cliente.getUser() != null ?
                            cliente.getUser().getUsername() : null,
                            cliente.getUltimoInicioSesion()
                            );
                })
                .toList();
    }
	
}
