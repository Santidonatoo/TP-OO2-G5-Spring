package oo2.grupo5.turnos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import oo2.grupo5.turnos.entities.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer>{

	Optional<User> findById(Integer integer);
	
	Optional<User> findByUsername(String username);
	
	@Query(value = "from User u order by u.id")
	List<User> findAll();
	
	List<User> findAllByActive(boolean active);
	
	boolean existsByUsername(String username);
}
