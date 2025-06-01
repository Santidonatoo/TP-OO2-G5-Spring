package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.PersonaRequestDTO;
import oo2.grupo5.turnos.dtos.responses.PersonaResponseDTO;
import oo2.grupo5.turnos.entities.Persona;
import oo2.grupo5.turnos.repositories.IPersonaRepository;
import oo2.grupo5.turnos.services.interfaces.IPersonaService;

@Service
public class PersonaServiceImp implements IPersonaService {
	
	private final IPersonaRepository personaRepository;
	private final ModelMapper modelMapper;
	
    public PersonaServiceImp(IPersonaRepository personaRepository, ModelMapper modelMapper) {
        this.personaRepository = personaRepository;
        this.modelMapper = modelMapper;
    }
    
    @Override
    public PersonaResponseDTO save(PersonaRequestDTO personaRequestDTO) {
    	//mappeo la persona con personaRequestDTO
    	Persona persona = modelMapper.map(personaRequestDTO, Persona.class);
    	//ahora mappeo a contacto solo
    	if (personaRequestDTO.getContacto() != null) {
	        persona.setContacto(
	            oo2.grupo5.turnos.entities.Contacto.builder()
	                .email(personaRequestDTO.getContacto().getEmail())
	                .telefono(personaRequestDTO.getContacto().getTelefono())
	                .build()
	        );
	    }
    	//ahora si guardo la persona
    	Persona saved = personaRepository.save(persona);
    	return modelMapper.map(saved, PersonaResponseDTO.class);
    }
    
    @Override
    public PersonaResponseDTO findById(Integer idPersona) {
        Persona persona = personaRepository.findById(idPersona)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Persona with id {0} not found",idPersona)));
        return modelMapper.map(persona, PersonaResponseDTO.class);
    }
    
    @Override
    public PersonaResponseDTO findByIdNotDeleted(Integer idPersona) {
    	Persona persona = personaRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
    			.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Persona with id {0} not found", idPersona)));
    	return modelMapper.map(persona, PersonaResponseDTO.class);
    }
    
    @Override
    public Page<PersonaResponseDTO> findAll(Pageable pageable) {
    	return personaRepository.findAll(pageable)
    			.map(entity -> modelMapper.map(entity, PersonaResponseDTO.class));
    }
    
    @Override
    public Page<PersonaResponseDTO> findAllNotDeleted(Pageable pageable) {
    	return personaRepository.findAllBySoftDeletedFalse(pageable)
    			.map(entity -> modelMapper.map(entity, PersonaResponseDTO.class));
    }
    
    @Override
    public PersonaResponseDTO update(Integer idPersona, PersonaRequestDTO personaRequestDTO) {
    	Persona persona = personaRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Persona with id {0} not found",idPersona)));

        persona.setNombre(personaRequestDTO.getNombre());
        persona.setApellido(personaRequestDTO.getApellido());
        persona.setDni(personaRequestDTO.getDni());
        persona.setFechaDeNacimiento(personaRequestDTO.getFechaDeNacimiento());

        Persona updated = personaRepository.save(persona);
        return modelMapper.map(updated, PersonaResponseDTO.class);
    }
    
    @Override
    public void deleteById(Integer idPersona) {
    	Persona persona = personaRepository.findByIdPersonaAndSoftDeletedFalse(idPersona)
    			.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Persona with id {0} not found", idPersona)));
    	
    	persona.setSoftDeleted(true);
    	personaRepository.save(persona);
    }
    
    @Override
    public PersonaResponseDTO restoreById(Integer idPersona) {
    	Persona persona = personaRepository.findById(idPersona)
    			.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Persona with id {0} not found", idPersona)));
    	
    	if(!persona.isSoftDeleted()) {
    		throw new IllegalStateException(MessageFormat.format("Persona with id {0} not found", idPersona));
    	}
    	
    	persona.setSoftDeleted(false);
    	Persona restored = personaRepository.save(persona);
    	return modelMapper.map(restored, PersonaResponseDTO.class);
    }
}
