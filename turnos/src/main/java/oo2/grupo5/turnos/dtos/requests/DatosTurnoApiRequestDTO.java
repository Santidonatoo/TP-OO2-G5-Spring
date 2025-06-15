package oo2.grupo5.turnos.dtos.requests;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DatosTurnoApiRequestDTO(
		@JsonProperty("fecha")
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    LocalDate fecha,
	    
	    @JsonProperty("idCliente")
	    Integer idCliente,
	    
	    @JsonProperty("idEmpleado")
	    Integer idEmpleado,
	    
	    @JsonProperty("idServicio")
	    Integer idServicio
	) {}
