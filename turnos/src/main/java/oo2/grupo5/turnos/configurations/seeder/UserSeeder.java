package oo2.grupo5.turnos.configurations.seeder;

import java.util.Set;
import oo2.grupo5.turnos.services.implementations.PersonaServiceImp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import oo2.grupo5.turnos.entities.Role;
import oo2.grupo5.turnos.entities.User;
import oo2.grupo5.turnos.enums.RoleType;
import oo2.grupo5.turnos.repositories.IRoleRepository;
import oo2.grupo5.turnos.repositories.IUserRepository;

@Component
public class UserSeeder implements CommandLineRunner {

	private static final String passwordGeneric = "abc1234";
	
	private final IUserRepository userRepository;
	
	private final IRoleRepository roleRepository;
	
	public UserSeeder(IUserRepository userRepository, IRoleRepository roleRepository, PersonaServiceImp personaServiceImp) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	@Override
	public void run(String... args) throws Exception {
		loadRoles();
		loadUsers();
	}
	
	
	//Carga los usuarios con sus roles a la base de datos
	private void loadUsers() {
		loadUserAdmin();
		loadUserEmployee();
		loadUserClient();
	}
	
	//Carga los roles existentes a la Base de datos
	private void loadRoles() {
		for (RoleType roleType : RoleType.values()) {
			if (roleRepository.findByType(roleType).isEmpty()) {
				roleRepository.save(buildRole(roleType));
			}
		}
	}
	
	private void loadUserClient() {
		if (userRepository.findByUsername("client").isEmpty()) {
			userRepository.save(buildUserClient("client", passwordGeneric));
		}
	}
	
	private User buildUserClient(String username, String password) {
		return User.builder()
				.username(username)
				.active(true)
				.password(encryptPassword(password))
				.roleEntities(Set.of(roleRepository.findByType(RoleType.CLIENT).get()))
				.build();
	}
	
	private void loadUserEmployee() {
		if (userRepository.findByUsername("employee").isEmpty()) {
			userRepository.save(buildUserEmployee("employee", passwordGeneric));
		}
	}
	
	private User buildUserEmployee(String username, String password) {
		return User.builder()
				.username(username)
				.active(true)
				.password(encryptPassword(password))
				.roleEntities(Set.of(roleRepository.findByType(RoleType.EMPLOYEE).get()))
				.build();
	}
	
	private void loadUserAdmin() {
		if (userRepository.findByUsername("admin").isEmpty()) {
			userRepository.save(buildUserAdmin("admin", passwordGeneric));
		}
	}
	
	private User buildUserAdmin(String username, String password) {
		return User.builder()
				.username(username)
				.active(true)
				.password(encryptPassword(password))
				.roleEntities(Set.of(roleRepository.findByType(RoleType.ADMIN).get()))
				.build();
	}
	
	/*
	private void loadRoles() {
		if(roleRepository.count() == 0) {
			roleRepository.save(buildRole(RoleType.CLIENT));
			roleRepository.save(buildRole(RoleType.ADMIN));
			roleRepository.save(buildRole(RoleType.EMPLOYEE));
		}
	}
	*/
	
	private Role buildRole(RoleType roleType) {
		return Role.builder()
				.type(roleType)
				.build();
	}
	
	private String encryptPassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(7);
		return passwordEncoder.encode(password);
	}
	
}
