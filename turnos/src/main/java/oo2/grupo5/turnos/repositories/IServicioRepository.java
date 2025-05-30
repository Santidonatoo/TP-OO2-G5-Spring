package oo2.grupo5.turnos.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oo2.grupo5.turnos.entities.Servicio;

@Repository
public interface IServicioRepository extends JpaRepository<Servicio, Integer>{

	
	//Search Servicio with softDeleted = False
    Page<Servicio> findAllBySoftDeletedFalse(Pageable pageable);
    
    //Traer Lista de servicios notdeleted y que requieran empleado
    Page<Servicio> findAllBySoftDeletedFalseAndRequiereEmpleadoTrue(Pageable pageable);
    
    //Traer lista de servicios notdeleted de un empleado
    Page<Servicio> findAllByListaEmpleados_IdPersonaAndSoftDeletedFalse(Integer idPersona, Pageable pageable);

    //Search Servicio with softDeleted = False by ID
    Optional<Servicio> findByIdServicioAndSoftDeletedFalse(Integer idServicio);
    
    boolean existsByNombre(String nombre);
    
}
