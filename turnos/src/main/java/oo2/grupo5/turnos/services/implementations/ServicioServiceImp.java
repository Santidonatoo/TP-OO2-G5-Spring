package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.ServicioRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;
import oo2.grupo5.turnos.entities.Servicio;
import oo2.grupo5.turnos.repositories.IServicioRepository;
import oo2.grupo5.turnos.services.interfaces.IServicioService;

@Service
public class ServicioServiceImp implements IServicioService {

	private final IServicioRepository servicioRepository;
    private final ModelMapper modelMapper;
    
    public ServicioServiceImp(IServicioRepository exampleRepository, ModelMapper modelMapper) {
        this.servicioRepository = exampleRepository;
        this.modelMapper = modelMapper;
    }
    
    @Override
    public ServicioResponseDTO save(ServicioRequestDTO exampleRequestDTO) {
    	Servicio servicio = modelMapper.map(exampleRequestDTO, Servicio.class);
    	Servicio saved = servicioRepository.save(servicio);
        return modelMapper.map(saved, ServicioResponseDTO.class);
    }
    
    @Override
    public ServicioResponseDTO findById(Integer idServicio) {
        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Servicio with id {0} not found",idServicio)));
        return modelMapper.map(servicio, ServicioResponseDTO.class);
    }
    
    @Override
	public ServicioResponseDTO findByIdNotDeleted(Integer idServicio) {
		Servicio example = servicioRepository.findByIdServicioAndSoftDeletedFalse(idServicio)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Servicio with id {0} not found",idServicio)));
        return modelMapper.map(example, ServicioResponseDTO.class);
	}

    @Override
    public Page<ServicioResponseDTO> findAll(Pageable pageable) {
        return servicioRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, ServicioResponseDTO.class));

    }
    @Override
    public Page<ServicioResponseDTO> findAllNotDeleted(Pageable pageable) {
        return servicioRepository.findAllBySoftDeletedFalse(pageable)
                .map(entity -> modelMapper.map(entity, ServicioResponseDTO.class));

    }
    
    
    

    @Override
    public ServicioResponseDTO update(Integer idServicio, ServicioRequestDTO servicioRequestDTO) {
    	Servicio servicio = servicioRepository.findByIdServicioAndSoftDeletedFalse(idServicio)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Servicio with id {0} not found",idServicio)));

        servicio.setNombre(servicioRequestDTO.getNombre());
        servicio.setDuracion(servicioRequestDTO.getDuracion());
        servicio.setRequiereEmpleado(servicioRequestDTO.isRequiereEmpleado());

        Servicio updated = servicioRepository.save(servicio);
        return modelMapper.map(updated, ServicioResponseDTO.class);
    }

    @Override
    public void deleteById(Integer idServicio) {
    	Servicio servicio = servicioRepository.findByIdServicioAndSoftDeletedFalse(idServicio)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Servicio with id {0} not found",idServicio)));

    	servicio.setSoftDeleted(true);
    	servicioRepository.save(servicio);
    }

    @Override
    public ServicioResponseDTO restoreById(Integer idServicio) {
    	Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Servicio with id {0} not found",idServicio)));

        if (!servicio.isSoftDeleted()) {
            throw new IllegalStateException(MessageFormat.format("Servicio with id {0} is not deleted",idServicio));
        }

        servicio.setSoftDeleted(false);
        Servicio restored = servicioRepository.save(servicio);
        return modelMapper.map(restored, ServicioResponseDTO.class);
    }
}
