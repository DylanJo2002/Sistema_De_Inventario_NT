package com.nt.Backend_NT.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nt.Backend_NT.entities.UserEntity;
import com.nt.Backend_NT.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Value("${adminPassword}")
	private String password;
	
	public UserEntity createUserEntity(UserEntity user, String adminPassword) throws Exception {
		
		if(passwordEncoder.matches(adminPassword, password)) {
			UserEntity userInDB = userRepository.findUsuarioEntityByUsuario(user.getUsuario());
			
			if(userInDB == null) {
				user.setClave(passwordEncoder.encode(user.getClave()));
				return userRepository.save(user);
			}
		}else {
			throw new Exception("Contraseña de administrador incorrecta");
		}
		
		throw new Exception(String.format("El usuario %s ya está en uso", user.getUsuario()));
	}

}
