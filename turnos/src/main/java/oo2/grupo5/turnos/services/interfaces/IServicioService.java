package oo2.grupo5.turnos.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import oo2.grupo5.turnos.dtos.requests.ServicioRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;


public interface IServicioService {

	ServicioResponseDTO save(ServicioRequestDTO servicioRequestDTO);

	//ServicioResponseDTO findById(Integer id);

    //Find By ID not deleted
	//ServicioResponseDTO findByIdNotDeleted(Integer id);

    // FindAll
   // Page<ServicioResponseDTO> findAll(Pageable pageable);

    // FindAll not deleted
    //Page<ServicioResponseDTO> findAllNotDeleted(Pageable pageable);

    //ServicioResponseDTO update(Integer id, ServicioRequestDTO servicioRequestDTO);

    //void deleteById(Integer id);

    //ServicioResponseDTO restoreById(Integer id);
}
