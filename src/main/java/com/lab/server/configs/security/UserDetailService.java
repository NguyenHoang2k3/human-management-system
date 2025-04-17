package com.lab.server.configs.security;

import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lab.server.entities.User;
import com.lab.server.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService{
	
	private final UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByFields(Map.of("username", username));
		if(user != null) {
			final var authorizations = user.getRoleId().getPermissions()
					.stream().map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
					.toList();
	
			return org.springframework.security.core.userdetails.User
					.withUsername(username)
					.password(user.getPassword())
					.authorities(authorizations)
					.build();
		}
		return null;
	}

}
