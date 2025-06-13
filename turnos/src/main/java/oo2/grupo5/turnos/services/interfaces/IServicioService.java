package oo2.grupo5.turnos.services.interfaces;

import java.util.List;
import java.util.Set;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import oo2.grupo5.turnos.dtos.requests.ServicioApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.ServicioRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioApiResponseDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;
import oo2.grupo5.turnos.entities.Servicio;


public interface IServicioService {

	ServicioResponseDTO save(ServicioRequestDTO servicioRequestDTO);

	ServicioResponseDTO findById(Integer idServicio);

    //Find By ID not deleted
	ServicioResponseDTO findByIdNotDeleted(Integer idServicio);

    // FindAll
	Page<ServicioResponseDTO> findAll(Pageable pageable);

    // FindAll not deleted
    Page<ServicioResponseDTO> findAllNotDeleted(Pageable pageable);

    Page<ServicioResponseDTO> findAllByNotDeletedAndRequiereEmpleadoTrue(Pageable pageable);
    
    Page<ServicioResponseDTO> findAllServiciosbyEmpleado(Integer idPersona, Pageable pageable);
    
    ServicioResponseDTO update(Integer idServicio, ServicioRequestDTO servicioRequestDTO, List<Integer> eliminarDisponibilidades);

    void deleteById(Integer idServicio);

    ServicioResponseDTO restoreById(Integer idServicio);
    
    ServicioApiResponseDTO findByIdApi(Integer id);

    ServicioApiResponseDTO crearServicioDesdeApi(ServicioApiRequestDTO dto);
}
