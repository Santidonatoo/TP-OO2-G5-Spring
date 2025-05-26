package oo2.grupo5.turnos.dtos.responses;

import jakarta.validation.constraints.NotBlank;
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
public class UbicacionResponseDTO {

	private Integer idUbicacion;
	
    private String localidad;
    
    private String calle;
	
    private int numero;
    
    private boolean softDeleted;
}
