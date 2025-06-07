package oo2.grupo5.turnos.dtos.responses;

import java.util.Set;

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
public class ServicioResponseDTO {
	private Integer idServicio;
	
    private String nombre;

	private boolean requiereEmpleado;

	private int duracion;
	
	private UbicacionResponseDTO ubicacion;
	
    private boolean softDeleted;
    
    private Set<EmpleadoResponseDTO> listaEmpleados; 
    
    private Set<DisponibilidadResponseDTO> listaDisponibilidades;
}
