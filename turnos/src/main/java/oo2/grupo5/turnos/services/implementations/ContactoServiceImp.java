package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;

import org.modelmapper.ModelMapper;


import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.ContactoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ContactoResponseDTO;
import oo2.grupo5.turnos.entities.Contacto;
import oo2.grupo5.turnos.repositories.IContactoRepository;
import oo2.grupo5.turnos.services.interfaces.IContactoService;

public class ContactoServiceImp implements IContactoService {

	private final IContactoRepository contactoRepository;
    private final ModelMapper modelMapper;
    
    public ContactoServiceImp(IContactoRepository contactoRepository, ModelMapper modelMapper) {
        this.contactoRepository = contactoRepository;
        this.modelMapper = modelMapper;
    }
    
    @Override
    public ContactoResponseDTO update(Integer idContacto, ContactoRequestDTO contactoRequestDTO) {
        Contacto contacto = contactoRepository.findById(idContacto)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Contacto with id {0} not found", idContacto)));

        contacto.setEmail(contactoRequestDTO.getEmail());
        contacto.setTelefono(contactoRequestDTO.getTelefono());

        Contacto updated = contactoRepository.save(contacto);
        return modelMapper.map(updated, ContactoResponseDTO.class);
    }

    @Override
    public ContactoResponseDTO findById(Integer idContacto) {
        Contacto contacto = contactoRepository.findById(idContacto)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Contacto with id {0} not found", idContacto)));

        return modelMapper.map(contacto, ContactoResponseDTO.class);
    }
	
}
