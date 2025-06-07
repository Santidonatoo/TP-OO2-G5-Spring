package oo2.grupo5.turnos.dtos.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServicioRequestDTO {

	private Integer idServicio;
	
    @NotBlank(message = "The name cannot be empty")
    private String nombre;
	
    private int duracion;
    
    private Integer idUbicacion;

	private boolean requiereEmpleado;

	private Set<Integer> idEmpleados;
	
	@Builder.Default
	private List<DisponibilidadRequestDTO> disponibilidades = new ArrayList<>();
}
