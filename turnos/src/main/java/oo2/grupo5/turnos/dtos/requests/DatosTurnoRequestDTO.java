package oo2.grupo5.turnos.dtos.requests;

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
public class DatosTurnoRequestDTO {
	
	private Integer idDatosTurno;
	
	private LocalDate fecha;
	
	private Integer idCliente;
	
	private Integer idEmpleado;
	
	private Integer idServicio;
}
