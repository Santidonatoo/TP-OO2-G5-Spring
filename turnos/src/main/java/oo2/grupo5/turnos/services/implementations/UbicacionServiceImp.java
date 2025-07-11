package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.UbicacionRequestDTO;
import oo2.grupo5.turnos.dtos.responses.UbicacionResponseDTO;
import oo2.grupo5.turnos.entities.Ubicacion;
import oo2.grupo5.turnos.exceptions.UbicacionNotFoundException;
import oo2.grupo5.turnos.repositories.IUbicacionRepository;
import oo2.grupo5.turnos.services.interfaces.IUbicacionService;

@Service
public class UbicacionServiceImp implements IUbicacionService {

    private final IUbicacionRepository ubicacionRepository;
    private final ModelMapper modelMapper;


    public UbicacionServiceImp(IUbicacionRepository ubicacionRepository, ModelMapper modelMapper) {
        this.ubicacionRepository = ubicacionRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public UbicacionResponseDTO save(UbicacionRequestDTO ubicacionRequestDTO) {
        Ubicacion ubicacion = modelMapper.map(ubicacionRequestDTO, Ubicacion.class);
        Ubicacion saved = ubicacionRepository.save(ubicacion);
        return modelMapper.map(saved, UbicacionResponseDTO.class);
        
    }
    @Override
    public UbicacionResponseDTO findById(Integer idUbicacion) {
        Ubicacion ubicacion = ubicacionRepository.findById(idUbicacion)
            .orElseThrow(() -> new UbicacionNotFoundException(idUbicacion));
        return modelMapper.map(ubicacion, UbicacionResponseDTO.class);
    }
    
    @Override
    public Page<UbicacionResponseDTO> findAll(Pageable pageable, String sortBy) {
    	Sort sort = switch (sortBy.toLowerCase()) {
        case "localidad" -> Sort.by("localidad").ascending();
        default -> Sort.by("idUbicacion").ascending();
    	};
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return ubicacionRepository.findAll(sortedPageable)
            .map(entity -> modelMapper.map(entity, UbicacionResponseDTO.class));
    }
    @Override
    public Page<UbicacionResponseDTO> findAllNotDeleted(Pageable pageable) {
        return ubicacionRepository.findAllBySoftDeletedFalse(pageable)
            .map(entity -> modelMapper.map(entity, UbicacionResponseDTO.class));
    }
    @Override
    public UbicacionResponseDTO update(Integer idUbicacion, UbicacionRequestDTO ubicacionRequestDTO) {
    	Ubicacion ubicacion = ubicacionRepository.findByIdUbicacionAndSoftDeletedFalse(idUbicacion)
                .orElseThrow(() -> new UbicacionNotFoundException(idUbicacion));

    	ubicacion.setLocalidad(ubicacionRequestDTO.getLocalidad());
    	ubicacion.setCalle(ubicacionRequestDTO.getCalle());
    	ubicacion.setNumero(ubicacionRequestDTO.getNumero());

    	Ubicacion updated = ubicacionRepository.save(ubicacion);
        return modelMapper.map(updated, UbicacionResponseDTO.class);
    }
    @Override
    public void deleteById(Integer idUbicacion) {
    	Ubicacion ubicacion = ubicacionRepository.findByIdUbicacionAndSoftDeletedFalse(idUbicacion)
                .orElseThrow(() -> new UbicacionNotFoundException(idUbicacion));

    	ubicacion.setSoftDeleted(true);
    	ubicacionRepository.save(ubicacion);
    }

    @Override
    public UbicacionResponseDTO restoreById(Integer idUbicacion) {
    	Ubicacion ubicacion = ubicacionRepository.findById(idUbicacion)
                .orElseThrow(() -> new UbicacionNotFoundException(idUbicacion));

        if (!ubicacion.isSoftDeleted()) {
            throw new IllegalStateException(MessageFormat.format("Ubicacion with id {0} is not deleted",idUbicacion));
        }

        ubicacion.setSoftDeleted(false);
        Ubicacion restored = ubicacionRepository.save(ubicacion);
        return modelMapper.map(restored, UbicacionResponseDTO.class);
    }
}
