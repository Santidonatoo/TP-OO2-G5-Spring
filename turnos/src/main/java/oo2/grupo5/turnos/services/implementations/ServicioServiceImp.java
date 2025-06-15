package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import jakarta.persistence.EntityNotFoundException;
import oo2.grupo5.turnos.dtos.requests.DisponibilidadRequestDTO;
import oo2.grupo5.turnos.dtos.requests.ServicioApiRequestDTO;
import oo2.grupo5.turnos.dtos.requests.ServicioRequestDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioApiResponseDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;
import oo2.grupo5.turnos.entities.Disponibilidad;
import oo2.grupo5.turnos.entities.Empleado;
import oo2.grupo5.turnos.entities.Servicio;
import oo2.grupo5.turnos.entities.Ubicacion;
import oo2.grupo5.turnos.exceptions.HorarioDisponibilidadInvalidoException;
import oo2.grupo5.turnos.exceptions.ServicioNotFoundException;
import oo2.grupo5.turnos.exceptions.UbicacionNotFoundException;
import oo2.grupo5.turnos.repositories.IDisponibilidadRepository;
import oo2.grupo5.turnos.repositories.IEmpleadoRepository;
import oo2.grupo5.turnos.repositories.IServicioRepository;
import oo2.grupo5.turnos.repositories.IUbicacionRepository;
import oo2.grupo5.turnos.services.interfaces.IServicioService;

@Service
public class ServicioServiceImp implements IServicioService {

	private final IServicioRepository servicioRepository;
	private final IUbicacionRepository ubicacionRepository;
	private final IEmpleadoRepository empleadoRepository;
	private final IDisponibilidadRepository disponibilidadRepository;

    private final ModelMapper modelMapper;
    
    public ServicioServiceImp(IServicioRepository servicioRepository, IUbicacionRepository ubicacionRepository,
    		IEmpleadoRepository empleadoRepository, IDisponibilidadRepository disponibilidadRepository, ModelMapper modelMapper) {
        this.servicioRepository = servicioRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.empleadoRepository = empleadoRepository;
        this.disponibilidadRepository = disponibilidadRepository;
        this.modelMapper = modelMapper;
    }
    
    @Override
    public ServicioResponseDTO save(ServicioRequestDTO servicioRequestDTO) {

    	if (servicioRepository.existsByNombre(servicioRequestDTO.getNombre())) {
    	        throw new IllegalArgumentException("Ya existe un servicio con el mismo nombre.");
    	}
    	if (!servicioRequestDTO.isRequiereEmpleado() && 
    			    (servicioRequestDTO.getIdEmpleados() != null && !servicioRequestDTO.getIdEmpleados().isEmpty())) {
    			    throw new IllegalArgumentException("El servicio no puede tener empleados asignados si no requiere empleados.");
    	}
    	Ubicacion ubicacion = ubicacionRepository.findById(servicioRequestDTO.getIdUbicacion())
                .orElseThrow(() -> new UbicacionNotFoundException(servicioRequestDTO.getIdUbicacion()));

        Servicio servicio = modelMapper.map(servicioRequestDTO, Servicio.class);
        servicio.setUbicacion(ubicacion);
        
        if (servicioRequestDTO.getIdEmpleados() != null && !servicioRequestDTO.getIdEmpleados().isEmpty()) {
            Set<Empleado> empleados = new HashSet<>(empleadoRepository.findAllById(servicioRequestDTO.getIdEmpleados()));
            servicio.setListaEmpleados(empleados);
        }
        
        // Manejar disponibilidades
        //la lista esta vacia?
        if (servicioRequestDTO.getDisponibilidades() != null && !servicioRequestDTO.getDisponibilidades().isEmpty()) {
            Set<Disponibilidad> disponibilidadesAsociadas = new HashSet<>();

            for (DisponibilidadRequestDTO disponibilidadRequestDTO : servicioRequestDTO.getDisponibilidades()) {
            	
            	 // Valida que horaInicio sea menor que horaFin
                if (disponibilidadRequestDTO.getHoraInicio().isAfter(disponibilidadRequestDTO.getHoraFin()) ||
                		disponibilidadRequestDTO.getHoraInicio().equals(disponibilidadRequestDTO.getHoraFin())) {
                    throw new HorarioDisponibilidadInvalidoException(
                        String.format("La hora de inicio (%s) debe ser anterior a la hora de fin (%s) para el día %s.",
                        		disponibilidadRequestDTO.getHoraInicio(),
                        		disponibilidadRequestDTO.getHoraFin(),
                        		disponibilidadRequestDTO.getDiaSemana()
                        )
                    );
                }
            	
            	//busca si la disponibilidad ya existe en la base de datos
                Optional<Disponibilidad> existente = disponibilidadRepository.findByDiaSemanaAndHoraInicioAndHoraFin(
                		disponibilidadRequestDTO.getDiaSemana(), disponibilidadRequestDTO.getHoraInicio(), disponibilidadRequestDTO.getHoraFin());
                
                //si la encuentra la agrega, sino la crea para luego agregarla
                Disponibilidad disponibilidad = existente.orElseGet(() -> {
                    Disponibilidad nuevaDisponibilidad = Disponibilidad.builder()
                            .diaSemana(disponibilidadRequestDTO.getDiaSemana())
                            .horaInicio(disponibilidadRequestDTO.getHoraInicio())
                            .horaFin(disponibilidadRequestDTO.getHoraFin())
                            .softDeleted(false)
                            .build();
                    return disponibilidadRepository.save(nuevaDisponibilidad);
                });

                disponibilidadesAsociadas.add(disponibilidad);
            }

            servicio.setListaDisponibilidades(disponibilidadesAsociadas);
        }
        
        //Se guarda el servicio
        Servicio saved = servicioRepository.save(servicio);
        return modelMapper.map(saved, ServicioResponseDTO.class);
    }
    
    @Override
    public ServicioResponseDTO findById(Integer idServicio) {
        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new ServicioNotFoundException(idServicio));
        return modelMapper.map(servicio, ServicioResponseDTO.class);
    }
    
    @Override
	public ServicioResponseDTO findByIdNotDeleted(Integer idServicio) {
		Servicio servicio = servicioRepository.findByIdServicioAndSoftDeletedFalse(idServicio)
                .orElseThrow(() -> new ServicioNotFoundException(idServicio));
        return modelMapper.map(servicio, ServicioResponseDTO.class);
	}
    

    @Override
    public Page<ServicioResponseDTO> findAll(Pageable pageable, String sortBy) {
    	Sort sort = switch (sortBy.toLowerCase()) {
        case "nombre" -> Sort.by("nombre").ascending();
        default -> Sort.by("idServicio").ascending();
    	};
    Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    return servicioRepository.findAll(sortedPageable)
    	.map(entity -> modelMapper.map(entity, ServicioResponseDTO.class));
    }
    
    @Override
    public Page<ServicioResponseDTO> findAllNotDeleted(Pageable pageable) {
        return servicioRepository.findAllBySoftDeletedFalse(pageable)
                .map(entity -> modelMapper.map(entity, ServicioResponseDTO.class));

    }
    
    @Override
    public Page<ServicioResponseDTO> findAllByNotDeletedAndRequiereEmpleadoTrue(Pageable pageable) {
    	return servicioRepository.findAllBySoftDeletedFalseAndRequiereEmpleadoTrue(pageable)
                .map(entity -> modelMapper.map(entity, ServicioResponseDTO.class));
    }
    public Page<ServicioResponseDTO> findAllServiciosbyEmpleado(Integer idPersona, Pageable pageable){
    	return servicioRepository.findAllByListaEmpleados_IdPersonaAndSoftDeletedFalse(idPersona, pageable)
                .map(entity -> modelMapper.map(entity, ServicioResponseDTO.class));
    }

    
    @Override
    public ServicioResponseDTO update(Integer idServicio, ServicioRequestDTO servicioRequestDTO, List<Integer> eliminarDisponibilidades) {
        Servicio servicio = servicioRepository.findByIdServicioAndSoftDeletedFalse(idServicio)
                .orElseThrow(() -> new ServicioNotFoundException(idServicio));

        // Buscar la nueva ubicación si se proporciona
        Ubicacion nuevaUbicacion = ubicacionRepository.findById(servicioRequestDTO.getIdUbicacion())
                .orElseThrow(() -> new UbicacionNotFoundException(servicioRequestDTO.getIdUbicacion()));
        if (servicioRepository.existsByNombre(servicioRequestDTO.getNombre())&&
    		    !servicioRepository.findById(idServicio).get().getNombre().equals(servicioRequestDTO.getNombre())) {
	        throw new IllegalArgumentException("Ya existe un servicio con el mismo nombre.");
	    }
        if (!servicioRequestDTO.isRequiereEmpleado() && 
			    (servicioRequestDTO.getIdEmpleados() != null && !servicioRequestDTO.getIdEmpleados().isEmpty())) {
			    throw new IllegalArgumentException("El servicio no puede tener empleados asignados si no requiere empleados.");
        }
        servicio.setNombre(servicioRequestDTO.getNombre());
        servicio.setDuracion(servicioRequestDTO.getDuracion());
        servicio.setRequiereEmpleado(servicioRequestDTO.isRequiereEmpleado());
        servicio.setUbicacion(nuevaUbicacion); 

        // Actualizar empleados si se envían nuevos IDs
        if (servicioRequestDTO.getIdEmpleados() != null) {
            Set<Empleado> empleados = new HashSet<>(empleadoRepository.findAllById(servicioRequestDTO.getIdEmpleados()));
            servicio.setListaEmpleados(empleados);
        }
        
        //Actualizar disponibilidades
        if (servicioRequestDTO.getDisponibilidades() != null) {
            Set<Disponibilidad> nuevasDisponibilidades = new HashSet<>();

            for (DisponibilidadRequestDTO disponibilidadDTO : servicioRequestDTO.getDisponibilidades()) {
            	
            	if (disponibilidadDTO.getHoraInicio().isAfter(disponibilidadDTO.getHoraFin()) ||
            	        disponibilidadDTO.getHoraInicio().equals(disponibilidadDTO.getHoraFin())) {
            	        throw new HorarioDisponibilidadInvalidoException(
            	            String.format("Error al actualizar el servicio: La hora de inicio (%s) debe ser anterior a la hora de fin (%s) para el día %s.",
            	                disponibilidadDTO.getHoraInicio(),
            	                disponibilidadDTO.getHoraFin(),
            	                disponibilidadDTO.getDiaSemana()
            	            )
            	        );
            	    }
            	
                Optional<Disponibilidad> existente = disponibilidadRepository.findByDiaSemanaAndHoraInicioAndHoraFin(
                        disponibilidadDTO.getDiaSemana(),
                        disponibilidadDTO.getHoraInicio(),
                        disponibilidadDTO.getHoraFin()
                );

                Disponibilidad disponibilidad = existente.orElseGet(() -> {
                    Disponibilidad nueva = Disponibilidad.builder()
                            .diaSemana(disponibilidadDTO.getDiaSemana())
                            .horaInicio(disponibilidadDTO.getHoraInicio())
                            .horaFin(disponibilidadDTO.getHoraFin())
                            .softDeleted(false)
                            .build();
                    return disponibilidadRepository.save(nueva);
                });

                nuevasDisponibilidades.add(disponibilidad);
            }
            
           
            if (servicio.getListaDisponibilidades() == null) {
                servicio.setListaDisponibilidades(nuevasDisponibilidades);
            } else {
                servicio.getListaDisponibilidades().addAll(nuevasDisponibilidades);
            }
        }
        
        //Procesar la eliminacion de las disponibilidades en el caso de que hayan sido seleccionadas
        if (eliminarDisponibilidades != null && !eliminarDisponibilidades.isEmpty()) {
            Set<Disponibilidad> updatedDisponibilidades = servicio.getListaDisponibilidades()
                    .stream()
                    .filter(d -> !eliminarDisponibilidades.contains(d.getIdDisponibilidad()))
                    .collect(Collectors.toSet());
            servicio.setListaDisponibilidades(updatedDisponibilidades);
        }

        
        Servicio updated = servicioRepository.save(servicio);
        return modelMapper.map(updated, ServicioResponseDTO.class);
    }

    @Override
    public void deleteById(Integer idServicio) {
    	Servicio servicio = servicioRepository.findByIdServicioAndSoftDeletedFalse(idServicio)
                .orElseThrow(() -> new ServicioNotFoundException(idServicio));

    	servicio.setSoftDeleted(true);
    	servicioRepository.save(servicio);
    }

    @Override
    public ServicioResponseDTO restoreById(Integer idServicio) {
    	Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new ServicioNotFoundException(idServicio));

        if (!servicio.isSoftDeleted()) {
            throw new IllegalStateException(MessageFormat.format("Servicio with id {0} is not deleted",idServicio));
        }

        servicio.setSoftDeleted(false);
        Servicio restored = servicioRepository.save(servicio);
        return modelMapper.map(restored, ServicioResponseDTO.class);
    }
    
  //RestAdd commentMore actions
    public ServicioApiResponseDTO findByIdApi(Integer id) {
        Servicio servicio = servicioRepository.findById(id)
            .orElseThrow(() -> new ServicioNotFoundException(id));
        List<String> empleados = servicio.getListaEmpleados().stream()
                .map(e -> e.getNombre() + " " + e.getApellido())
                .toList();
        return new ServicioApiResponseDTO( 
        		servicio.getIdServicio(),
        		servicio.getNombre(),
        		servicio.getDuracion(),
        		servicio.isRequiereEmpleado(),
        		servicio.getUbicacion().getLocalidad() + ", " + servicio.getUbicacion().getCalle(),
        		empleados
        		);
    
    }
    
    public List<ServicioApiResponseDTO> findAllApi(String sortBy) {
    	Sort sort = switch (sortBy.toLowerCase()) {
        	case "nombre" -> Sort.by("nombre").ascending();
        	default -> Sort.by("idServicio").ascending();
    	};
        List<Servicio> servicios = servicioRepository.findAll(sort);

        return servicios.stream()
                .map(servicio -> {
                	List<String> empleados = servicio.getListaEmpleados().stream()
                            .map(e -> e.getNombre() + " " + e.getApellido())
                            .toList();

                    return new ServicioApiResponseDTO(
                    		servicio.getIdServicio(),
                    		servicio.getNombre(),
                    		servicio.getDuracion(),
                    		servicio.isRequiereEmpleado(),
                    		servicio.getUbicacion().getLocalidad() + ", " + servicio.getUbicacion().getCalle(),
                    		empleados
                    		);
                })
                .toList();
    }

    public ServicioApiResponseDTO crearServicioDesdeApi(ServicioApiRequestDTO dto) {
        Servicio servicio = new Servicio();
        servicio.setNombre(dto.nombre());
        servicio.setDuracion(dto.duracion());
        servicio.setRequiereEmpleado(dto.requiereEmpleado());

        Ubicacion ubicacion = ubicacionRepository.findById(dto.idUbicacion())
            .orElseThrow(() -> new UbicacionNotFoundException(dto.idUbicacion()));
        servicio.setUbicacion(ubicacion);
        
        servicioRepository.save(servicio);
        List<String> empleados = new ArrayList<String>();

        return new ServicioApiResponseDTO(
            servicio.getIdServicio(),
            servicio.getNombre(),
            servicio.getDuracion(),
    		servicio.isRequiereEmpleado(),
            ubicacion.getLocalidad() + ", " + ubicacion.getCalle(),
            empleados
        );
    }
}
