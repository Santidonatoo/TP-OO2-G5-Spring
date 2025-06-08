package oo2.grupo5.turnos.configurations.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import oo2.grupo5.turnos.security.CustomAuthenticationSuccessHandler;
import oo2.grupo5.turnos.services.implementations.UserServiceImp;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

	private final UserServiceImp userServiceImp;
	private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	public SecurityConfiguration(UserServiceImp userServiceImp, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
		this.userServiceImp = userServiceImp;
		this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
	}
	
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                        "/css/*", "/imgs/*", "/js/*", "/vendor/bootstrap/css/*",
                        "/vendor/jquery/*", "/vendor/bootstrap/js/*", "/api/v1/**"
                    ).permitAll();
                    
                    auth.requestMatchers(
                        "/auth/login", "/auth/loginProcess", "/auth/loginSuccess", "/auth/logout",
                        "/auth/registro", "/auth/registro-cliente"
                    ).permitAll();
                    
                    auth.requestMatchers("/empleado/save").hasRole("ADMIN");
                    
                    auth.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    login.loginPage("/auth/login");
                    login.loginProcessingUrl("/auth/loginProcess");//POST
                    login.usernameParameter("username");
                    login.passwordParameter("password");
                    login.successHandler(customAuthenticationSuccessHandler);
                    login.permitAll();
                })
                .logout(logout -> {
                    logout.logoutUrl("/auth/logout");//POST
                    logout.logoutSuccessUrl("/auth/login");
                    logout.permitAll();
                })
                .build();
    }
	
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userServiceImp);
        return provider;
    }
	
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
