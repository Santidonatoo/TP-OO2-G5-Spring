package oo2.grupo5.turnos.services.interfaces;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import oo2.grupo5.turnos.dtos.requests.UbicacionRequestDTO;
import oo2.grupo5.turnos.dtos.responses.UbicacionResponseDTO;

public interface IUbicacionService {
   
    UbicacionResponseDTO save(UbicacionRequestDTO ubicacionRequestDTO);

    UbicacionResponseDTO findById(Integer idUbicacion);

    Page<UbicacionResponseDTO> findAll(Pageable pageable, String sortBy);
    
    Page<UbicacionResponseDTO> findAllNotDeleted(Pageable pageable);

    UbicacionResponseDTO update(Integer idUbicacion, UbicacionRequestDTO ubicacionRequestDTO);

    void deleteById(Integer idUbicacion);

    UbicacionResponseDTO restoreById(Integer idUbicacion);
}
