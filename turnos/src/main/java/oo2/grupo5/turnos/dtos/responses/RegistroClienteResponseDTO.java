package oo2.grupo5.turnos.dtos.responses;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

public class RegistroClienteResponseDTO {
	
	//Datos User
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	//Datos Cliente(Entity Cliente)
	@NotBlank
	private String nombre;
	
	@NotBlank
	private String apellido;
	
	@NotNull
	private int dni;
	
	@NotNull
	private LocalDate fechaDeNacimiento;
	
	//Agregar los datos de contacto que se quieran enviar
}
