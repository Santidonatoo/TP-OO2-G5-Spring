package oo2.grupo5.turnos.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oo2.grupo5.turnos.entities.Empleado;

@Repository
public interface IEmpleadoRepository extends JpaRepository<Empleado, Integer> {
	
	//Search Empleado with softDeleted = False
	Page<Empleado> findAllBySoftDeletedFalse(Pageable pageable);
	
	//Search Empleado with softDeleted = False by ID
	Optional<Empleado> findByIdPersonaAndSoftDeletedFalse(Integer idPersona);
    
	//Traer lista de empleados notdeleted de un servicio
    Page<Empleado> findAllByListaServicios_IdServicioAndSoftDeletedFalse(Integer idServicio, Pageable pageable);

	boolean existsByDni(int dni);
	
	//TraerEmpleadosDeUnServicio
}
