package oo2.grupo5.turnos.dtos.requests;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import oo2.grupo5.turnos.enums.EstadoTurno;

public record CrearTurnoApiRequestDTO(
	    @JsonProperty("hora")
	    @JsonFormat(pattern = "HH:mm:ss")
	    @NotNull(message = "La hora es obligatoria")
	    LocalTime hora,
	    
	    @JsonProperty("estado")
	    EstadoTurno estado,
	    
	    @JsonProperty("fecha")
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    @NotNull(message = "La fecha es obligatoria")
	    LocalDate fecha,

	    @JsonProperty("idCliente")
	    @NotNull(message = "El ID del cliente es obligatorio")
	    Integer idCliente,

	    @JsonProperty("idEmpleado")
	    @NotNull(message = "El ID del empleado es obligatorio")
	    Integer idEmpleado,

	    @JsonProperty("idServicio")
	    @NotNull(message = "El ID del servicio es obligatorio")
	    Integer idServicio
	) {}
