package oo2.grupo5.turnos.services.interfaces;

import oo2.grupo5.turnos.dtos.requests.ContactoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ContactoResponseDTO;

public interface IContactoService {

	ContactoResponseDTO update(Integer idContacto, ContactoRequestDTO contactoRequestDTO);
    ContactoResponseDTO findById(Integer idContacto);
}

