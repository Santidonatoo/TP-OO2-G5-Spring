package oo2.grupo5.turnos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import oo2.grupo5.turnos.entities.Contacto;


public interface IContactoRepository extends JpaRepository<Contacto, Integer>{

	Optional<Contacto> findByIdContacto(Integer idContacto);
}
