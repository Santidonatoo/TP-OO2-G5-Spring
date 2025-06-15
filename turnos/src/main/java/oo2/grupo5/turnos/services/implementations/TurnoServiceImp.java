package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;		
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.DatosTurnoApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.TurnoApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.TurnoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ClienteResponseDTO;
import oo2.grupo5.turnos.dtos.responses.DatosTurnoApiResponseDTO;
import oo2.grupo5.turnos.dtos.responses.DatosTurnoResponseDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoResponseDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;
import oo2.grupo5.turnos.dtos.responses.TurnoApiResponseDTO;
import oo2.grupo5.turnos.dtos.responses.TurnoResponseDTO;
import oo2.grupo5.turnos.entities.DatosTurno;
import oo2.grupo5.turnos.entities.Empleado;
import oo2.grupo5.turnos.entities.Servicio;
import oo2.grupo5.turnos.entities.Turno;
import oo2.grupo5.turnos.enums.EstadoTurno;
import oo2.grupo5.turnos.repositories.IDatosTurnoRepository;
import oo2.grupo5.turnos.repositories.IEmpleadoRepository;
import oo2.grupo5.turnos.repositories.IServicioRepository;
import oo2.grupo5.turnos.repositories.ITurnoRepository;
import oo2.grupo5.turnos.services.interfaces.IDatosTurnoService;
import oo2.grupo5.turnos.services.interfaces.ITurnoService;

@Service
public class TurnoServiceImp implements ITurnoService {

	private final IDatosTurnoRepository datosTurnoRepository;
	private final ITurnoRepository turnoRepository;
	private final IServicioRepository servicioRepository;
	private final IEmpleadoRepository empleadoRepository;
	private final IDatosTurnoService datosTurnoService;
	private final ModelMapper modelMapper;
	
	public TurnoServiceImp(IDatosTurnoService datosTurnoService, ITurnoRepository turnoRepository, IDatosTurnoRepository datosTurnoRepository, IServicioRepository servicioRepository, IEmpleadoRepository empleadoRepository, ModelMapper modelMapper) {
        
		this.turnoRepository = turnoRepository;
		this.datosTurnoRepository = datosTurnoRepository;
		this.servicioRepository = servicioRepository;
		this.empleadoRepository = empleadoRepository;
		this.datosTurnoService = datosTurnoService;
        this.modelMapper = modelMapper;
    }
	
	@Override
	public TurnoResponseDTO save(TurnoRequestDTO turnoRequestDTO) {
		
		DatosTurno datosTurno = datosTurnoRepository.findById(turnoRequestDTO.getIdDatosTurno())
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Los datos del turno que tienen id {0} no han sido encontrados", turnoRequestDTO.getIdDatosTurno())));
		
		Turno turno = modelMapper.map(turnoRequestDTO, Turno.class);
		turno.setDatosTurno(datosTurno);
		
		Turno saved = turnoRepository.save(turno);
		return modelMapper.map(saved, TurnoResponseDTO.class);
	}

	@Override
	public TurnoResponseDTO findById(Integer idTurno) {
		Turno turno = turnoRepository.findById(idTurno)
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("El turno con id {0} no ha sido encontrado", idTurno)));
		
		return modelMapper.map(turno, TurnoResponseDTO.class);
	}

	@Override
	public Page<TurnoResponseDTO> findAll(Pageable pageable) {
		
		return turnoRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, TurnoResponseDTO.class));
	}

	@Override
	public TurnoResponseDTO update(Integer idTurno, TurnoRequestDTO turnoRequestDTO) {
		Turno turno = turnoRepository.findById(idTurno)
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("El turno con id {0} no ha sido encontrado", idTurno)));
		
		DatosTurno datosTurno = datosTurnoRepository.findById(turnoRequestDTO.getIdDatosTurno())
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Los datos del turno que tienen id {0} no han sido encontrados", turnoRequestDTO.getIdDatosTurno())));
		
		turno = modelMapper.map(turnoRequestDTO, Turno.class);
		turno.setDatosTurno(datosTurno);
		turno.setHora(turnoRequestDTO.getHora());
		
		Turno updated = turnoRepository.save(turno);
		return modelMapper.map(updated, TurnoResponseDTO.class);
	}

	@Override
	public void cancelById(Integer idTurno) {
		Turno turno = turnoRepository.findById(idTurno)
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("El turno con id {0} no ha sido encontrado", idTurno)));
		
		turno.setEstado(EstadoTurno.CANCELADO);
	}

	@Override
	public void deleteById(Integer idTurno) {
		turnoRepository.deleteById(idTurno);
	}

	@Override
	public Page<TurnoResponseDTO> findTurnosByEmpleadoFecha(Pageable pageable, Integer idEmpleado, LocalDate fecha){
		
		Empleado empleado = empleadoRepository.findById(idEmpleado).orElse(null);
		
		return turnoRepository.findByDatosTurno_EmpleadoAndDatosTurno_Fecha(empleado, fecha, pageable)
				.map(entity -> modelMapper.map(entity, TurnoResponseDTO.class));
	}

	@Override
	public Page<TurnoResponseDTO> findTurnosByServicioFecha(Pageable pageable, Integer idServicio, LocalDate fecha) {
		Servicio servicio = servicioRepository.findById(idServicio).orElse(null);
		
		return turnoRepository.findByDatosTurno_ServicioAndDatosTurno_Fecha(servicio, fecha, pageable)
				.map(entity -> modelMapper.map(entity, TurnoResponseDTO.class));
	}

	@Override
	public Page<TurnoResponseDTO> findTurnosByPersona(Pageable pageable, Integer idPersona) {
		 Page<Turno> turnosCliente = turnoRepository.findByDatosTurno_Cliente_IdPersona(idPersona, pageable);
		 Page<Turno> turnosEmpleado = turnoRepository.findByDatosTurno_Empleado_IdPersona(idPersona, pageable);
		 
		 List<TurnoResponseDTO> turnos = Stream.concat(turnosCliente.stream(), turnosEmpleado.stream())
			        .map(turno -> modelMapper.map(turno, TurnoResponseDTO.class))
			        .collect(Collectors.toList());

		 return new PageImpl<>(turnos, pageable, turnos.size());
	}
	

	@Override
	public TurnoApiResponseDTO saveApi(TurnoApiRequestDTO turnoApiRequestDTO, 
            DatosTurnoApiRequestDTO datosTurnoRequestDTO) {

		DatosTurnoApiResponseDTO datosTurnoResponse = datosTurnoService.saveDatosTurnoApi(datosTurnoRequestDTO);

		// Obtenemos la entidad DatosTurno recién creada para asociarla
		DatosTurno datosTurno = datosTurnoRepository.findById(datosTurnoResponse.idDatosTurno())
				.orElseThrow(() -> new EntityNotFoundException("Error al recuperar los datos del turno creados"));

		// Creamos la entidad Turno manualmente desde el record
		Turno turno = new Turno();
		turno.setHora(turnoApiRequestDTO.hora());
		turno.setEstado(turnoApiRequestDTO.estado());
		turno.setDatosTurno(datosTurno);  // Asociamos los datos recién creados

		// Persistimos la entidad Turno
		Turno saved = turnoRepository.save(turno);

		// Mapeo manual inline para la respuesta
		return new TurnoApiResponseDTO(
				saved.getIdTurno(),
				saved.getHora(),
				saved.getEstado(),
				saved.getDatosTurno() != null ?
						new DatosTurnoApiResponseDTO(
				                turno.getDatosTurno().getIdDatosTurno(),
				                turno.getDatosTurno().getFecha(),
				                turno.getDatosTurno().getCliente() != null ? 
				                		turno.getDatosTurno().getCliente().getNombre() : null,
				                turno.getDatosTurno().getEmpleado() != null ? 
				                		turno.getDatosTurno().getEmpleado().getNombre() : null,
				                turno.getDatosTurno().getServicio() != null ? 
				                		turno.getDatosTurno().getServicio().getNombre() : null
				) : null
			);
	}
	
	@Override
	public TurnoApiResponseDTO findByIdApi(Integer idTurno) {

	    Turno turno = turnoRepository.findById(idTurno)
	        .orElseThrow(() -> new EntityNotFoundException(
	            MessageFormat.format("El turno con id {0} no ha sido encontrado", idTurno)));
	    
	    return new TurnoApiResponseDTO(
	        turno.getIdTurno(),              
	        turno.getHora(),                  
	        turno.getEstado(),                
	        turno.getDatosTurno() != null ?
	            new DatosTurnoApiResponseDTO(
	                turno.getDatosTurno().getIdDatosTurno(),
	                turno.getDatosTurno().getFecha(),
	                turno.getDatosTurno().getCliente() != null ? 
	                		turno.getDatosTurno().getCliente().getNombre() : null,
	                turno.getDatosTurno().getEmpleado() != null ? 
	                		turno.getDatosTurno().getEmpleado().getNombre() : null,
	                turno.getDatosTurno().getServicio() != null ? 
	                		turno.getDatosTurno().getServicio().getNombre() : null
	            ) : null
	    );
	}
}
