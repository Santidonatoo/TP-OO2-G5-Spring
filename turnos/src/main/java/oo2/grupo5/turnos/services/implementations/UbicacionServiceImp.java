package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.UbicacionRequestDTO;
import oo2.grupo5.turnos.dtos.responses.UbicacionResponseDTO;
import oo2.grupo5.turnos.entities.Ubicacion;
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
            .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Ubicación con id {0} no encontrada", idUbicacion)));
        return modelMapper.map(ubicacion, UbicacionResponseDTO.class);
    }
    
    @Override
    public Page<UbicacionResponseDTO> findAll(Pageable pageable) {
        return ubicacionRepository.findAll(pageable)
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
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Ubicacion with id {0} not found",idUbicacion)));

    	ubicacion.setLocalidad(ubicacionRequestDTO.getLocalidad());
    	ubicacion.setCalle(ubicacionRequestDTO.getCalle());
    	ubicacion.setNumero(ubicacionRequestDTO.getNumero());

    	Ubicacion updated = ubicacionRepository.save(ubicacion);
        return modelMapper.map(updated, UbicacionResponseDTO.class);
    }
    @Override
    public void deleteById(Integer idUbicacion) {
    	Ubicacion ubicacion = ubicacionRepository.findByIdUbicacionAndSoftDeletedFalse(idUbicacion)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Ubicacion with id {0} not found",idUbicacion)));

    	ubicacion.setSoftDeleted(true);
    	ubicacionRepository.save(ubicacion);
    }

    @Override
    public UbicacionResponseDTO restoreById(Integer idUbicacion) {
    	Ubicacion ubicacion = ubicacionRepository.findById(idUbicacion)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Ubicacion with id {0} not found",idUbicacion)));

        if (!ubicacion.isSoftDeleted()) {
            throw new IllegalStateException(MessageFormat.format("Ubicacion with id {0} is not deleted",idUbicacion));
        }

        ubicacion.setSoftDeleted(false);
        Ubicacion restored = ubicacionRepository.save(ubicacion);
        return modelMapper.map(restored, UbicacionResponseDTO.class);
    }
}
