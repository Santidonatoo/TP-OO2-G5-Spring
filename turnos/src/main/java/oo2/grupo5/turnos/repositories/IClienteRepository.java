package oo2.grupo5.turnos.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import oo2.grupo5.turnos.entities.Cliente;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Integer>{
	
	//Search Cliente with softDeleted = False
	Page<Cliente> findAllBySoftDeletedFalse(Pageable pageable);
	
	//Search Cliente with softDeleted = False by ID
	Optional<Cliente> findByIdPersonaAndSoftDeletedFalse(Integer idPersona);
	
	boolean existsByDni(int dni);
	
	//Método para actualizar último inicio de sesión (solo para clientes)
	@Modifying
	@Query("UPDATE Cliente c SET c.ultimoInicioSesion = :fechaHora WHERE c.idPersona = :idPersona AND c.softDeleted = false")
	int actualizarUltimoInicioSesion(@Param("idPersona") Integer idPersona, @Param("fechaHora") LocalDateTime fechaHora);

    //Encontrar cliente por username del user asociado (solo clientes activos)
    @Query("SELECT c FROM Cliente c " +
           "JOIN User u ON u.persona.idPersona = c.idPersona " +
           "WHERE u.username = :username AND c.softDeleted = false")
    Optional<Cliente> findByUsername(@Param("username") String username);

    // Query adicional para verificar si un username pertenece a un cliente
    @Query("SELECT COUNT(c) > 0 FROM Cliente c " +
           "JOIN User u ON u.persona.idPersona = c.idPersona " +
           "WHERE u.username = :username AND c.softDeleted = false")
    boolean existsClienteByUsername(@Param("username") String username);

}
