package oo2.grupo5.turnos.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import oo2.grupo5.turnos.dtos.requests.DatosTurnoRequestDTO;
import oo2.grupo5.turnos.dtos.requests.TurnoRequestDTO;
import oo2.grupo5.turnos.dtos.responses.DatosTurnoResponseDTO;
import oo2.grupo5.turnos.dtos.responses.EmpleadoResponseDTO;
import oo2.grupo5.turnos.dtos.responses.ServicioResponseDTO;
import oo2.grupo5.turnos.dtos.responses.TurnoResponseDTO;
import oo2.grupo5.turnos.entities.User;
import oo2.grupo5.turnos.enums.EstadoTurno;
import oo2.grupo5.turnos.helpers.ViewRouteHelper;
import oo2.grupo5.turnos.repositories.IServicioRepository;
import oo2.grupo5.turnos.services.interfaces.IClienteService;
import oo2.grupo5.turnos.services.interfaces.IDatosTurnoService;
import oo2.grupo5.turnos.services.interfaces.IEmpleadoService;
import oo2.grupo5.turnos.services.interfaces.IServicioService;
import oo2.grupo5.turnos.services.interfaces.ITurnoService;

@Controller
@RequestMapping("/turno")
@SessionAttributes({"DatosTurnoRequestDTO", "TurnoRequestDTO"})

public class TurnoController {
	
	private final ITurnoService turnoService;
	private final IDatosTurnoService datosTurnoService;
	private final IClienteService clienteService;
	private final IEmpleadoService empleadoService;
	private final IServicioService servicioService;
	
	 public TurnoController(ITurnoService turnoService, IDatosTurnoService datosTurnoService, IClienteService clienteService, 
			 IEmpleadoService empleadoService, IServicioService servicioService) {
		 	this.turnoService = turnoService;
	        this.datosTurnoService = datosTurnoService;
	        this.clienteService = clienteService;
	        this.empleadoService = empleadoService;
	        this.servicioService = servicioService;
	    }
	 
	@ModelAttribute("DatosTurnoRequestDTO")
	   public DatosTurnoRequestDTO createDatosTurno() {
	       return new DatosTurnoRequestDTO();
	   }
	@ModelAttribute("TurnoRequestDTO")
	   public TurnoRequestDTO createTurno() {
	       return new TurnoRequestDTO();
	   }
	
	@GetMapping("/elegir-servicio")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'EMPLOYEE')")
    public String mostrarServicios(Model model,  @PageableDefault(size = 5) Pageable pageable) {
	        Page<ServicioResponseDTO> servicios = servicioService.findAllNotDeleted(pageable);
	        model.addAttribute("servicios", servicios);
        return ViewRouteHelper.TURNO_SERVICIO;
    }
	
	@PostMapping("/guardar-servicio")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'EMPLOYEE')")
    public String guardarServicio(@RequestParam("idServicio") Integer idServicio, @ModelAttribute("DatosTurnoRequestDTO") DatosTurnoRequestDTO datosTurno) {
        datosTurno.setIdServicio(idServicio);
        
        if (servicioService.findById(idServicio).isRequiereEmpleado()) {
            return "redirect:/turno/elegir-empleado"; // Redirige a la vista de selección de empleados
        } else {
            return "redirect:/turno/elegir-fecha"; // Si no requiere empleado, pasa directamente a elegir fecha
        }
    }
	
	@GetMapping("/elegir-empleado")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'EMPLOYEE')")
    public String mostrarEmpleados(Model model, @ModelAttribute("DatosTurnoRequestDTO") DatosTurnoRequestDTO datosTurno, @PageableDefault(size = 5) Pageable pageable) {
        
        Page<EmpleadoResponseDTO> empleados = empleadoService.findAllEmpleadosbyServicio(datosTurno.getIdServicio(), pageable);
        model.addAttribute("empleados", empleados);
        
        return ViewRouteHelper.TURNO_EMPLEADO; // Vista para elegir empleado.
    }
	
	@PostMapping("/guardar-empleado")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'EMPLOYEE')")
	public String guardarEmpleado(@RequestParam("idEmpleado") Integer idEmpleado, @ModelAttribute("DatosTurnoRequestDTO") DatosTurnoRequestDTO datosTurno) {
		
		datosTurno.setIdEmpleado(idEmpleado);
		return "redirect:/turno/elegir-fecha";
	}
	
	@GetMapping("/elegir-fecha")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'EMPLOYEE')")
	public String mostrarFechas(Model model) {
		return ViewRouteHelper.TURNO_FECHA; // Vista para elegir fecha.
	}
	
	@PostMapping("/guardar-fecha")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'EMPLOYEE')")
	public String guardarFecha(@RequestParam("fecha") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha, @ModelAttribute("DatosTurnoRequestDTO") DatosTurnoRequestDTO datosTurno) {

	    datosTurno.setFecha(fecha);
	    return "redirect:/turno/elegir-horario";
	}
	
	@GetMapping("/elegir-horario")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'EMPLOYEE')")
	public String mostrarHorarios(Model model, @ModelAttribute("DatosTurnoRequestDTO") DatosTurnoRequestDTO datosTurno, @PageableDefault(size = 5) Pageable pageable) {
	    ServicioResponseDTO servicio = servicioService.findById(datosTurno.getIdServicio());
	    int frecuencia = servicio.getDuracion();
	    
	    // Obtener los turnos ocupados
	    Page<TurnoResponseDTO> turnos;
	    
	    if (servicio.isRequiereEmpleado()) {
	        turnos = turnoService.findTurnosByEmpleadoFecha(pageable, datosTurno.getIdEmpleado(), datosTurno.getFecha());
	    } else {
	        turnos = turnoService.findTurnosByServicioFecha(pageable, datosTurno.getIdServicio(), datosTurno.getFecha());
	    }

	    // Convertimos los turnos ocupados en una lista de LocalTime para facilitar la comparación
	    List<LocalTime> turnosBloqueados = new ArrayList<>();
	    for (TurnoResponseDTO turno : turnos.getContent()) {
	        LocalTime inicioTurno = turno.getHora();
	        for (int minutos = 0; minutos < turno.getDatosTurno().getServicio().getDuracion(); minutos += 15) {
	            turnosBloqueados.add(inicioTurno.plusMinutes(minutos));
	        }
	    }

	    // Obtener la disponibilidad del servicio para el día de la semana correspondiente
	    DayOfWeek diaSeleccionado = datosTurno.getFecha().getDayOfWeek();
	    List<LocalTime> horariosDisponibles = new ArrayList<>();

	    servicio.getListaDisponibilidades().stream()
	        .filter(disponibilidad -> disponibilidad.getDiaSemana().equals(diaSeleccionado))
	        .forEach(disponibilidad -> {
	            LocalTime horaInicio = disponibilidad.getHoraInicio();
	            LocalTime horaFin = disponibilidad.getHoraFin();
	            if (frecuencia > 0) {
	            	while (horaInicio.isBefore(horaFin) || horaInicio.equals(horaFin)) {
		                if (!turnosBloqueados.contains(horaInicio)) { // Agrega solo si no está ocupado
		                    horariosDisponibles.add(horaInicio);
		                }
		                horaInicio = horaInicio.plusMinutes(frecuencia);
		            }
	            }
	        });

	    model.addAttribute("horarios", horariosDisponibles);
	    return ViewRouteHelper.TURNO_HORARIO; // Vista para elegir fecha.
	}
	
	@PostMapping("/guardar-horario")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'EMPLOYEE')")
	public String guardarHorario(@RequestParam("horario") String horarioStr, @ModelAttribute("TurnoRequestDTO") TurnoRequestDTO turno) {
		LocalTime horario = LocalTime.parse(horarioStr);
	    turno.setHora(horario);
	    return "redirect:/turno/confirmar-turno";
	}
	
	@GetMapping("/confirmar-turno")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'EMPLOYEE')")
	public String confirmarTurno(Model model, @ModelAttribute("TurnoRequestDTO") TurnoRequestDTO turno, @ModelAttribute("DatosTurnoRequestDTO") DatosTurnoRequestDTO datosTurno) {
		model.addAttribute("datosTurno", datosTurno);
		model.addAttribute("turno", turno);
		return ViewRouteHelper.TURNO_CONFIRMAR;
	}
	
	@PostMapping("/turno-exitoso")
	@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'EMPLOYEE')")
	public String finalizarTurno(@ModelAttribute("TurnoRequestDTO") TurnoRequestDTO turno, @ModelAttribute("DatosTurnoRequestDTO") DatosTurnoRequestDTO datosTurno) {
		Integer idCliente = null;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            idCliente = user.getPersona().getIdPersona();
        }
        
        datosTurno.setIdCliente(idCliente);
        
        DatosTurnoResponseDTO datosTurnoSaved = datosTurnoService.save(datosTurno);
        turno.setIdDatosTurno(datosTurnoSaved.getIdDatosTurno());
        turno.setEstado(EstadoTurno.ACEPTADO);
        turnoService.save(turno);
        
	    return "redirect:/index";
	}
	
	@GetMapping("/lista-turnos")
	@PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE')")
	public String listaTurnosPersona(Model model, @PageableDefault(size = 5) Pageable pageable) {
		Integer idPersona = null;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            idPersona = user.getPersona().getIdPersona();
        }
        
        Page<TurnoResponseDTO> turnos = turnoService.findTurnosByPersona(pageable, idPersona);
        model.addAttribute("turnos", turnos);
		return ViewRouteHelper.TURNO_LISTA;
	}
	
	@GetMapping("/lista-turnos-admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String listaTurnos(Model model, @PageableDefault(size = 5) Pageable pageable) {

        Page<TurnoResponseDTO> turnos = turnoService.findAll(pageable);
        model.addAttribute("turnos", turnos);
		return ViewRouteHelper.TURNO_LISTA;
	}
}
