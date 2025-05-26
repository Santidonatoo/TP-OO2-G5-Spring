package oo2.grupo5.turnos.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oo2.grupo5.turnos.entities.Ubicacion;

@Repository
public interface IUbicacionRepository extends JpaRepository<Ubicacion, Integer> {
    
    Page<Ubicacion> findAllBySoftDeletedFalse(Pageable pageable);

	Optional<Ubicacion> findByIdUbicacionAndSoftDeletedFalse(Integer idUbicacion);


}
