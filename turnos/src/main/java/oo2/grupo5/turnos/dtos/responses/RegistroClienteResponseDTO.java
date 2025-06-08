package oo2.grupo5.turnos.dtos.responses;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

public class RegistroClienteResponseDTO {
	
	//Datos User
	@NotBlank(message = "El nombre de usuario no puede estar vacío")
	@Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
	private String username;
	
	@NotBlank(message = "La contraseña no puede estar vacía")
	@Size(min = 8, max = 100, message = "La contraseña debe tener al menos 8 caracteres")
	private String password;
	
	//Datos Cliente(Entity Cliente)
	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
	private String nombre;
	
	@NotBlank(message = "El apellido no puede estar vacío")
	@Size(min = 3, max = 50, message = "El apellido debe tener entre 3 y 50 caracteres")
	private String apellido;
	
	@NotNull(message = "El DNI no puede ser nulo")
	@Min(value = 1000000, message = "El DNI debe tener al menos 7 dígitos")
	@Max(value = 99999999, message = "El DNI no puede tener más de 8 dígitos")
	private int dni;
	
	@NotNull(message = "La fecha de nacimiento no puede ser nula")
	@Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
	private LocalDate fechaDeNacimiento;
	
	//Agregar los datos de contacto que se quieran enviar
	@Valid
	@NotNull(message = "El contacto no puede ser nulo")
	private ContactoResponseDTO contacto;
}
