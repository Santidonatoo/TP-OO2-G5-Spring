package oo2.grupo5.turnos.dtos.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatosTurnoResponseDTO {
	
	private Integer idDatosTurno;
	
	private LocalDate fecha;
	
	private ClienteResponseDTO cliente;
	
	private EmpleadoResponseDTO empleado;
	
	private ServicioResponseDTO servicio;
}
