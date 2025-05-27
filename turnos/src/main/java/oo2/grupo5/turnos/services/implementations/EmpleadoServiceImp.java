package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.EmpleadoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoResponseDTO;
import oo2.grupo5.turnos.entities.Empleado;
import oo2.grupo5.turnos.repositories.IEmpleadoRepository;
import oo2.grupo5.turnos.services.interfaces.IEmpleadoService;

@Service
public class EmpleadoServiceImp implements IEmpleadoService{
	
	private final IEmpleadoRepository empleadoRepository;
	private final ModelMapper modelMapper;
	
	public EmpleadoServiceImp(IEmpleadoRepository empleadoRepository, ModelMapper modelMapper) {
		this.empleadoRepository = empleadoRepository;
		this.modelMapper = modelMapper;
	}
	
	@Override
	public EmpleadoResponseDTO save(EmpleadoRequestDTO empleadoRequestDTO) {
		Empleado empleado = modelMapper.map(empleadoRequestDTO, Empleado.class);
    	Empleado saved = empleadoRepository.save(empleado);
    	return modelMapper.map(saved, EmpleadoResponseDTO.class);
	}
	
	@Override
	public EmpleadoResponseDTO findById(Integer idPersona) {
		Empleado empleado = empleadoRepository.findById(idPersona)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Empleado with id {0} not found",idPersona)));
        return modelMapper.map(empleado, EmpleadoResponseDTO.class);
	}
	
	@Override
	public EmpleadoResponseDTO findByIdNotDeleted(Integer idPersona) {
		Empleado empleado = empleadoRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
    			.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Empleado with id {0} not found", idPersona)));
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
	
	@Override
	public EmpleadoResponseDTO update(Integer idPersona, EmpleadoRequestDTO empleadoRequestDTO) {
		Empleado empleado = empleadoRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Empleado with id {0} not found",idPersona)));
		
        empleado.setNombre(empleadoRequestDTO.getNombre());
        empleado.setApellido(empleadoRequestDTO.getApellido());
        empleado.setDni(empleadoRequestDTO.getDni());
        empleado.setFechaDeNacimiento(empleadoRequestDTO.getFechaDeNacimiento());
        empleado.setPuesto(empleadoRequestDTO.getPuesto());
        
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
