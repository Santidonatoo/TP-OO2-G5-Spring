package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.DatosTurnoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.DatosTurnoResponseDTO;
import oo2.grupo5.turnos.entities.Cliente;
import oo2.grupo5.turnos.entities.Servicio;
import oo2.grupo5.turnos.entities.DatosTurno;
import oo2.grupo5.turnos.entities.Empleado;
import oo2.grupo5.turnos.repositories.IClienteRepository;
import oo2.grupo5.turnos.repositories.IDatosTurnoRepository;
import oo2.grupo5.turnos.repositories.IEmpleadoRepository;
import oo2.grupo5.turnos.repositories.IServicioRepository;
import oo2.grupo5.turnos.services.interfaces.IDatosTurnoService;

@Service
public class DatosTurnoServiceImp implements IDatosTurnoService {
	
	private final IDatosTurnoRepository datosTurnoRepository;
	private final IClienteRepository clienteRepository;
	private final IEmpleadoRepository empleadoRepository;
	private final IServicioRepository servicioRepository;
    private final ModelMapper modelMapper;
	
    public DatosTurnoServiceImp(IDatosTurnoRepository datosTurnoRepository, ModelMapper modelMapper, IEmpleadoRepository empleadoRepository, IServicioRepository servicioRepository, IClienteRepository clienteRepository) {
        this.datosTurnoRepository = datosTurnoRepository;
		this.clienteRepository = clienteRepository;
		this.empleadoRepository = empleadoRepository;
		this.servicioRepository = servicioRepository;
        this.modelMapper = modelMapper;
    }
    
	@Override
	public DatosTurnoResponseDTO save(DatosTurnoRequestDTO datosTurnoRequestDTO) {
		
		Cliente cliente = clienteRepository.findById(datosTurnoRequestDTO.getIdCliente())
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cliente con id {0} no encontrado", datosTurnoRequestDTO.getIdCliente())));
		Empleado empleado = empleadoRepository.findById(datosTurnoRequestDTO.getIdEmpleado())
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Empleado con id {0} no encontrado", datosTurnoRequestDTO.getIdEmpleado())));
		Servicio servicio = servicioRepository.findById(datosTurnoRequestDTO.getIdServicio())
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Servicio con id {0} no encontrado", datosTurnoRequestDTO.getIdServicio())));
		
		DatosTurno datosTurno = modelMapper.map(datosTurnoRequestDTO, DatosTurno.class);
		datosTurno.setCliente(cliente);
		datosTurno.setEmpleado(empleado);
		datosTurno.setServicio(servicio);
		
		DatosTurno saved = datosTurnoRepository.save(datosTurno);
		return modelMapper.map(saved, DatosTurnoResponseDTO.class);
	}

	@Override
	public DatosTurnoResponseDTO findById(Integer idDatosTurno) {
		DatosTurno datosTurno = datosTurnoRepository.findById(idDatosTurno)
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Los datos del turno que tienen id {0} no han sido encontrados", idDatosTurno)));
		
		return modelMapper.map(datosTurno, DatosTurnoResponseDTO.class);
	}

	@Override
	public Page<DatosTurnoResponseDTO> findAll(Pageable pageable) {
		 return datosTurnoRepository.findAll(pageable)
	                .map(entity -> modelMapper.map(entity, DatosTurnoResponseDTO.class));
	}

	@Override
	public DatosTurnoResponseDTO update(Integer idDatosTurno, DatosTurnoRequestDTO datosTurnoRequestDTO) {
		
		DatosTurno datosTurno = datosTurnoRepository.findById(idDatosTurno)
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Los datos del turno que tienen id {0} no han sido encontrados", idDatosTurno)));
		
		Cliente cliente = clienteRepository.findById(datosTurnoRequestDTO.getIdCliente())
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cliente con id {0} no encontrado", datosTurnoRequestDTO.getIdCliente())));
		Empleado empleado = empleadoRepository.findById(datosTurnoRequestDTO.getIdEmpleado())
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Empleado con id {0} no encontrado", datosTurnoRequestDTO.getIdEmpleado())));
		Servicio servicio = servicioRepository.findById(datosTurnoRequestDTO.getIdServicio())
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Servicio con id {0} no encontrado", datosTurnoRequestDTO.getIdServicio())));
		
		datosTurno.setFecha(datosTurnoRequestDTO.getFecha());
		datosTurno.setCliente(cliente);
		datosTurno.setEmpleado(empleado);
		datosTurno.setServicio(servicio);
		
		DatosTurno updated = datosTurnoRepository.save(datosTurno);
		return modelMapper.map(updated, DatosTurnoResponseDTO.class);
	}

	@Override
	public void deleteById(Integer idDatosTurno) {
		
		datosTurnoRepository.deleteById(idDatosTurno);
		
	}

}
