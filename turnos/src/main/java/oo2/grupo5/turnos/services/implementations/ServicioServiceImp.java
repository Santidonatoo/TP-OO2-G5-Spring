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
import oo2.grupo5.turnos.entities.Ubicacion;
import oo2.grupo5.turnos.repositories.IServicioRepository;
import oo2.grupo5.turnos.repositories.IUbicacionRepository;
import oo2.grupo5.turnos.services.interfaces.IServicioService;

@Service
public class ServicioServiceImp implements IServicioService {

	private final IServicioRepository servicioRepository;
	private final IUbicacionRepository ubicacionRepository;
    private final ModelMapper modelMapper;
    
    public ServicioServiceImp(IServicioRepository servicioRepository, IUbicacionRepository ubicacionRepository, ModelMapper modelMapper) {
        this.servicioRepository = servicioRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.modelMapper = modelMapper;
    }
    
    @Override
    public ServicioResponseDTO save(ServicioRequestDTO servicioRequestDTO) {

    	Ubicacion ubicacion = ubicacionRepository.findById(servicioRequestDTO.getIdUbicacion())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Ubicación con id {0} no encontrada", servicioRequestDTO.getIdUbicacion())));

        // Mapear y asignar ubicación
        Servicio servicio = modelMapper.map(servicioRequestDTO, Servicio.class);
        servicio.setUbicacion(ubicacion);

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
		Servicio servicio = servicioRepository.findByIdServicioAndSoftDeletedFalse(idServicio)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Servicio with id {0} not found",idServicio)));
        return modelMapper.map(servicio, ServicioResponseDTO.class);
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
    
    /*@Override
    public ServicioResponseDTO update(Integer idServicio, ServicioRequestDTO servicioRequestDTO) {
    	Servicio servicio = servicioRepository.findByIdServicioAndSoftDeletedFalse(idServicio)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Servicio with id {0} not found",idServicio)));

        servicio.setNombre(servicioRequestDTO.getNombre());
        servicio.setDuracion(servicioRequestDTO.getDuracion());
        servicio.setRequiereEmpleado(servicioRequestDTO.isRequiereEmpleado());

        Servicio updated = servicioRepository.save(servicio);
        return modelMapper.map(updated, ServicioResponseDTO.class);
    }*/
    
    public ServicioResponseDTO update(Integer idServicio, ServicioRequestDTO servicioRequestDTO) {
        Servicio servicio = servicioRepository.findByIdServicioAndSoftDeletedFalse(idServicio)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Servicio con id {0} no encontrado", idServicio)));

        // Buscar la nueva ubicación si se proporciona
        Ubicacion nuevaUbicacion = ubicacionRepository.findById(servicioRequestDTO.getIdUbicacion())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Ubicación con id {0} no encontrada", servicioRequestDTO.getIdUbicacion())));

        servicio.setNombre(servicioRequestDTO.getNombre());
        servicio.setDuracion(servicioRequestDTO.getDuracion());
        servicio.setRequiereEmpleado(servicioRequestDTO.isRequiereEmpleado());
        servicio.setUbicacion(nuevaUbicacion); // Actualizar ubicación

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
