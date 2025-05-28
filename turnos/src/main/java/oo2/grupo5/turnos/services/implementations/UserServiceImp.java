package oo2.grupo5.turnos.services.implementations;

import java.text.MessageFormat;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oo2.grupo5.turnos.repositories.IUserRepository;

@Service("userService")
public class UserServiceImp implements UserDetailsService{
	
	private final IUserRepository userRepository;
	
	public UserServiceImp(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException(MessageFormat.format("Username with username {0} not found", username))
		);
	}
	
}
