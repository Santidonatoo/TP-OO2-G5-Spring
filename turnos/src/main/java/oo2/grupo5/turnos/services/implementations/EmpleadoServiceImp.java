package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.EmpleadoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoResponseDTO;
import oo2.grupo5.turnos.entities.Contacto;
import oo2.grupo5.turnos.entities.Empleado;
import oo2.grupo5.turnos.entities.Role;
import oo2.grupo5.turnos.entities.Servicio;
import oo2.grupo5.turnos.entities.User;
import oo2.grupo5.turnos.enums.RoleType;
import oo2.grupo5.turnos.exceptions.EmpleadoNotFoundException;
import oo2.grupo5.turnos.exceptions.ServicioNotFoundException;
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
	
	@Override
	public EmpleadoResponseDTO findById(Integer idPersona) {
		Empleado empleado = empleadoRepository.findById(idPersona)
                .orElseThrow(() -> new EmpleadoNotFoundException(idPersona));
        return modelMapper.map(empleado, EmpleadoResponseDTO.class);
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
	
    public Page<EmpleadoResponseDTO> findAllEmpleadosbyServicio(Integer idServicio, Pageable pageable){
    	return empleadoRepository.findAllByListaServicios_IdServicioAndSoftDeletedFalse(idServicio, pageable)
    			.map(entity -> modelMapper.map(entity, EmpleadoResponseDTO.class));
    }

	@Override
	public EmpleadoResponseDTO update(Integer idPersona, EmpleadoRequestDTO empleadoRequestDTO) {
		Empleado empleado = empleadoRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Empleado with id {0} not found",idPersona)));
		
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
    			.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Empleado with id {0} not found", idPersona)));
    	
    	empleado.setSoftDeleted(true);
    	empleadoRepository.save(empleado);
	}
	
	@Override
	public EmpleadoResponseDTO restoreById(Integer idPersona) {
		Empleado empleado = empleadoRepository.findById(idPersona)
    			.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Empleado with id {0} not found", idPersona)));
    	
    	if(!empleado.isSoftDeleted()) {
    		throw new IllegalStateException(MessageFormat.format("Empleado with id {0} not found", idPersona));
    	}
    	
    	empleado.setSoftDeleted(false);
    	Empleado restored = empleadoRepository.save(empleado);
    	return modelMapper.map(restored, EmpleadoResponseDTO.class);
	}
}
