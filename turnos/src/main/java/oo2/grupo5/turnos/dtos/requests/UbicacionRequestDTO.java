package oo2.grupo5.turnos.dtos.requests;

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
public class UbicacionRequestDTO {

	private Integer idUbicacion;
	
    @NotBlank(message = "The localidad cannot be empty")
    private String localidad;
    
    @NotBlank(message = "The calle cannot be empty")
    private String calle;
	
    private int numero;
    
    

}
