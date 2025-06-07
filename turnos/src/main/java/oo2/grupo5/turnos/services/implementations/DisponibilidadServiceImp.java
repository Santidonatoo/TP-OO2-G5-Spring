package oo2.grupo5.turnos.services.implementations;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.DisponibilidadRequestDTO;
import oo2.grupo5.turnos.dtos.responses.DisponibilidadResponseDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;
import oo2.grupo5.turnos.entities.Disponibilidad;
import oo2.grupo5.turnos.entities.Servicio;
import oo2.grupo5.turnos.repositories.IDisponibilidadRepository;
import oo2.grupo5.turnos.repositories.IServicioRepository;
import oo2.grupo5.turnos.services.interfaces.IDisponibilidadService;

@Service
public class DisponibilidadServiceImp implements IDisponibilidadService{

	
	private final IDisponibilidadRepository disponibilidadRepository;
	private final IServicioRepository servicioRepository;
	
	private final ModelMapper modelMapper;
	
	public DisponibilidadServiceImp(IDisponibilidadRepository disponibilidadRepository, 
			IServicioRepository servicioRepository, ModelMapper modelMapper) {
		
		this.disponibilidadRepository = disponibilidadRepository;
		this.servicioRepository = servicioRepository;
		this.modelMapper = modelMapper;	
	}
	
	@Override
	public Page <DisponibilidadResponseDTO> findAllNotDeleted(Pageable pageable){
		return disponibilidadRepository.findAllBySoftDeletedFalse(pageable)
				.map(entity -> modelMapper.map(entity, DisponibilidadResponseDTO.class));
	}
	
	public void deleteById(Integer idDisponibilidad) {
		
		Disponibilidad disponibilidad = disponibilidadRepository.findByIdDisponibilidadAndSoftDeletedFalse(idDisponibilidad)
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Servicio with id {0} not found",idDisponibilidad)));
		
		disponibilidad.setSoftDeleted(true);
		disponibilidadRepository.save(disponibilidad);
	}
}
