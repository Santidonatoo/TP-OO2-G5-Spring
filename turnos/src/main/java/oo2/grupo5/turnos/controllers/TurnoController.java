package oo2.grupo5.turnos.controllers;

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
	    int frecuencia = servicioService.findById(datosTurno.getIdServicio()).getDuracion();
	    
	    Page<TurnoResponseDTO> turnos;
	    
	    if(servicioService.findById(datosTurno.getIdServicio()).isRequiereEmpleado()) {
	    	turnos = turnoService.findTurnosByServicioEmpleadoFecha(pageable, datosTurno.getIdServicio(), datosTurno.getIdEmpleado(), datosTurno.getFecha());
	    } else {turnos = turnoService.findTurnosByServicioFecha(pageable, datosTurno.getIdServicio(), datosTurno.getFecha());}
	    
	    
	    
	    // Convertimos los turnos ocupados en una lista de LocalTime para facilitar la comparación
	    List<LocalTime> turnosOcupados = turnos.getContent().stream()
	                                          .map(TurnoResponseDTO::getHora) // Asumiendo que getHora devuelve un LocalTime
	                                          .collect(Collectors.toList());
	    
	    List<LocalTime> horariosDisponibles = new ArrayList<>();
	    LocalTime horaInicio = LocalTime.of(10, 0);
	    LocalTime horaFin = LocalTime.of(18, 0);

	    while (horaInicio.isBefore(horaFin) || horaInicio.equals(horaFin)) {
	        if (!turnosOcupados.contains(horaInicio)) { // Agrega solo si no está ocupado
	            horariosDisponibles.add(horaInicio);
	        }
	        horaInicio = horaInicio.plusMinutes(frecuencia);
	    }

	    System.out.println(horariosDisponibles);
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
}
