package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import oo2.grupo5.turnos.dtos.requests.EmpleadoApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.EmpleadoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoApiResponseDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoResponseDTO;
import oo2.grupo5.turnos.entities.Contacto;
import oo2.grupo5.turnos.entities.Empleado;
import oo2.grupo5.turnos.entities.Role;
import oo2.grupo5.turnos.entities.Servicio;
import oo2.grupo5.turnos.entities.User;
import oo2.grupo5.turnos.enums.RoleType;
import oo2.grupo5.turnos.exceptions.EmpleadoNotFoundException;
import oo2.grupo5.turnos.repositories.IEmpleadoRepository;
import oo2.grupo5.turnos.repositories.IPersonaRepository;
import oo2.grupo5.turnos.repositories.IRoleRepository;
import oo2.grupo5.turnos.repositories.IServicioRepository;
import oo2.grupo5.turnos.repositories.IUserRepository;
import oo2.grupo5.turnos.services.interfaces.IEmpleadoService;

@Service
public class EmpleadoServiceImp implements IEmpleadoService{
	
	private final IEmpleadoRepository empleadoRepository;
	private final IServicioRepository servicioRepository;
	private final IRoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final IUserRepository userRepository;
	private final IPersonaRepository personaRepository;
	
	public EmpleadoServiceImp(IEmpleadoRepository empleadoRepository, IServicioRepository servicioRepository,ModelMapper modelMapper,
			IRoleRepository roleRepository, PasswordEncoder passwordEncoder, IUserRepository userRepository, 
			IPersonaRepository personaRepository) {
		this.empleadoRepository = empleadoRepository;
		this.servicioRepository = servicioRepository;
		this.modelMapper = modelMapper;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.personaRepository = personaRepository;
	}
	
	@Override
	public EmpleadoResponseDTO save(EmpleadoRequestDTO empleadoRequestDTO) {
		
    	if (personaRepository.existsByDni(empleadoRequestDTO.getDni())) {
	        throw new IllegalArgumentException("Ya existe una persona con el mismo dni.");
    	}
    	
        if (userRepository.existsByUsername(empleadoRequestDTO.getUsername())) {
            throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre de usuario.");
        }
    	
		Empleado empleado = modelMapper.map(empleadoRequestDTO, Empleado.class);
		
		Contacto contacto = Contacto.builder()
	            .email(empleadoRequestDTO.getContacto().getEmail())
	            .telefono(empleadoRequestDTO.getContacto().getTelefono())
	            .build();
	    empleado.setContacto(contacto);
		
    	Empleado saved = empleadoRepository.save(empleado);
    	
    	if (empleadoRequestDTO.getIdServicios() != null && !empleadoRequestDTO.getIdServicios().isEmpty()) {
            Set<Servicio> servicios = new HashSet<>(servicioRepository.findAllById(empleadoRequestDTO.getIdServicios()));
            empleado.setListaServicios(servicios);
        }
    	
    	Role rolEmpleado = roleRepository.findByType(RoleType.EMPLOYEE)
    			.orElseThrow(() -> new RuntimeException("Rol EMPLOYEE no encontrado"));
    	
        User user = User.builder()
                .username(empleadoRequestDTO.getUsername())
                .password(passwordEncoder.encode(empleadoRequestDTO.getPassword()))
                .active(true)
                .roleEntities(Set.of(rolEmpleado))
                .persona(saved) // Relación con Persona (Empleado)
                .build();
        
        userRepository.save(user);
    	
    	return modelMapper.map(saved, EmpleadoResponseDTO.class);
	}
	
	//API REST CONTROLLER
	public EmpleadoApiResponseDTO saveApi(EmpleadoApiRequestDTO empleadoApiRequestDTO) {
    	
		String dniStr = empleadoApiRequestDTO.dni();
		int dni = Integer.parseInt(dniStr);
		
		if (personaRepository.existsByDni(dni)) {
	        throw new IllegalArgumentException("Ya existe una persona con el mismo dni.");
    	}
    	
        if (userRepository.existsByUsername(empleadoApiRequestDTO.username())) {
            throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre de usuario.");
        }
        
        Empleado empleado = new Empleado();
        empleado.setNombre(empleadoApiRequestDTO.nombre());
        empleado.setApellido(empleadoApiRequestDTO.apellido());
        empleado.setDni(Integer.parseInt(empleadoApiRequestDTO.dni()));
        empleado.setPuesto(empleadoApiRequestDTO.puesto());
        
        
        //Creamos contacto y lo añadimos a empleado
        Contacto contacto = Contacto.builder()
        		.email(empleadoApiRequestDTO.email())
        		.telefono(empleadoApiRequestDTO.telefono())
        		.build();
        empleado.setContacto(contacto);
        
        Empleado saved = empleadoRepository.save(empleado);
        List<String> servicios = new ArrayList<>();
        
        Role rolEmpleado = roleRepository.findByType(RoleType.EMPLOYEE)
        		.orElseThrow(() -> new RuntimeException("Rol EMPLOYEE no encontrado"));
        
        User user = User.builder()
        		.username(empleadoApiRequestDTO.username())
        		.password(empleadoApiRequestDTO.password())
        		.active(true)
        		.roleEntities(Set.of(rolEmpleado))        		
        		.persona(saved)
        		.build();
        
        userRepository.save(user);
        
        return new EmpleadoApiResponseDTO(
        		saved.getIdPersona(),
        		saved.getNombre(),
        		saved.getApellido(),
        		String.valueOf(saved.getDni()),
        		contacto.getEmail(),
        		contacto.getTelefono(),
        		user.getUsername(),
        		saved.getPuesto(),
        		servicios
        		);
    }
	
	@Override
	public EmpleadoResponseDTO findById(Integer idPersona) {
		Empleado empleado = empleadoRepository.findById(idPersona)
                .orElseThrow(() -> new EmpleadoNotFoundException(idPersona));
        return modelMapper.map(empleado, EmpleadoResponseDTO.class);
	}
	
	//API REST CONTROLLER
	public EmpleadoApiResponseDTO findByIdApi(Integer idPersona) {
		Empleado empleado = empleadoRepository.findById(idPersona)
				.orElseThrow(() -> new EmpleadoNotFoundException(idPersona));
		
		List<String> servicios = empleado.getListaServicios().stream()
				.map(e -> e.getNombre())
				.toList();
		//Mapeo manual
	    return new EmpleadoApiResponseDTO(
	            empleado.getIdPersona(),           
	            empleado.getNombre(),              
	            empleado.getApellido(),            
	            String.valueOf(empleado.getDni()),                 
	            empleado.getContacto() != null ? 
	                empleado.getContacto().getEmail() : null,
	            empleado.getContacto() != null ? 
	                empleado.getContacto().getTelefono() : null,
	            empleado.getUser() != null ? 
	                empleado.getUser().getUsername() : null,
	            empleado.getPuesto(),              
	            servicios
	        );
	}
	
	@Override
	public EmpleadoResponseDTO findByIdNotDeleted(Integer idPersona) {
		Empleado empleado = empleadoRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
    			.orElseThrow(() -> new EmpleadoNotFoundException(idPersona));
    	return modelMapper.map(empleado, EmpleadoResponseDTO.class);
	}
	
	@Override
	public Page<EmpleadoResponseDTO> findAll(Pageable pageable) {
    	return empleadoRepository.findAll(pageable)
    			.map(entity -> modelMapper.map(entity, EmpleadoResponseDTO.class));
	}
	
	@Override
	public Page<EmpleadoResponseDTO> findAllNotDeleted(Pageable pageable){
    	return empleadoRepository.findAllBySoftDeletedFalse(pageable)
    			.map(entity -> modelMapper.map(entity, EmpleadoResponseDTO.class));
	}
	
	//API REST CONTROLLER
	public Page<EmpleadoApiResponseDTO> findAllNotDeletedApi(Pageable pageable){
		return empleadoRepository.findAllBySoftDeletedFalse(pageable)
				.map(entity -> modelMapper.map(entity, EmpleadoApiResponseDTO.class));
	}
	
    public Page<EmpleadoResponseDTO> findAllEmpleadosbyServicio(Integer idServicio, Pageable pageable){
    	return empleadoRepository.findAllByListaServicios_IdServicioAndSoftDeletedFalse(idServicio, pageable)
    			.map(entity -> modelMapper.map(entity, EmpleadoResponseDTO.class));
    }

	@Override
	public EmpleadoResponseDTO update(Integer idPersona, EmpleadoRequestDTO empleadoRequestDTO) {
		Empleado empleado = empleadoRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
                .orElseThrow(() -> new EmpleadoNotFoundException(idPersona));
		
		if (personaRepository.existsByDni(empleadoRequestDTO.getDni())&&
    		    personaRepository.findById(idPersona).get().getDni() != empleadoRequestDTO.getDni()) {
	        throw new IllegalArgumentException("Ya existe una persona con el mismo dni.");
	    }
		
        empleado.setNombre(empleadoRequestDTO.getNombre());
        empleado.setApellido(empleadoRequestDTO.getApellido());
        empleado.setDni(empleadoRequestDTO.getDni());
        empleado.setFechaDeNacimiento(empleadoRequestDTO.getFechaDeNacimiento());
        empleado.setPuesto(empleadoRequestDTO.getPuesto());
        
        if (empleadoRequestDTO.getIdServicios() != null) {
            Set<Servicio> servicios = new HashSet<>(servicioRepository.findAllById(empleadoRequestDTO.getIdServicios()));
            empleado.setListaServicios(servicios);
        }
        
        Empleado updated = empleadoRepository.save(empleado);
        return modelMapper.map(updated, EmpleadoResponseDTO.class);
	}
	
	@Override
	public void deleteById(Integer idPersona) {
		Empleado empleado = empleadoRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
    			.orElseThrow(() -> new EmpleadoNotFoundException(idPersona));
    	
    	empleado.setSoftDeleted(true);
    	empleadoRepository.save(empleado);
	}
	
	@Override
	public EmpleadoResponseDTO restoreById(Integer idPersona) {
		Empleado empleado = empleadoRepository.findById(idPersona)
    			.orElseThrow(() -> new EmpleadoNotFoundException(idPersona));
    	
    	if(!empleado.isSoftDeleted()) {
    		throw new IllegalStateException(MessageFormat.format("Empleado with id {0} is not deleted",idPersona));
    	}
    	
    	empleado.setSoftDeleted(false);
    	Empleado restored = empleadoRepository.save(empleado);
    	return modelMapper.map(restored, EmpleadoResponseDTO.class);
	}
}
