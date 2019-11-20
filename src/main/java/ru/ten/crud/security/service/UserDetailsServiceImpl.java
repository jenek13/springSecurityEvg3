package ru.ten.crud.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ten.crud.model.User;
import ru.ten.crud.model.Role;
import ru.ten.crud.service.RoleService;
import ru.ten.crud.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {	//сервис, отвечающий за получение аутентификации пользователя

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		User user = userService.getUserByLogin(login);
		if (user == null) {
			throw new UsernameNotFoundException("Username " + login + " not found");
		}

		return user;
	}


}
