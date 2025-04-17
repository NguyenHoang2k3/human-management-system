package com.lab.server.services;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.apache.coyote.BadRequestException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lab.server.caches.ICacheData;
import com.lab.lib.constants.Constants;
import com.lab.lib.enumerated.Language;
import com.lab.server.configs.language.MessageSourceHelper;
import com.lab.server.configs.security.JwtProvider;
import com.lab.server.configs.security.SecurityHelper;
import com.lab.server.entities.User;
import com.lab.server.payload.auth.LoginRequest;
import com.lab.server.payload.auth.LoginResponse;
import com.lab.server.payload.auth.TokenResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class AuthService {
	
	private final UserService userService;
	private final SecurityHelper securityHelper;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final MessageSourceHelper messageSourceHelper;
	private final ICacheData<String> caches;
	
	public LoginResponse login(LoginRequest request) throws Exception {
		User user;
		if(!isMail(request.getIdentifier())) {
			user = userService.findByFields(Map.of("username", request.getIdentifier()));
		} else {
			user = userService.findByFields(Map.of("email", request.getIdentifier()));
		}
	
		try {
			Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword()));
		
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	  
	        final String accessToken = jwtProvider.generateToken(user.getUsername());
	        final String refreshToken = UUID.randomUUID().toString();
	  
	        return LoginResponse.builder()
	        		.id(user.getUserId())
	        		.username(user.getUsername())
	        		.email(user.getEmail())
	        		.token(TokenResponse.builder()
	        				.accessToken(accessToken)
	        				.refreshToken(refreshToken)
	        				.build())
	        		.build();
	        
		} catch(Exception e) {
			log.error("{} - {}", e.getClass().getSimpleName(), e.getMessage());
			throw new Exception(messageSourceHelper.getMessage("error.notFound"));
		}
	}
	
	public void logout(HttpServletRequest request) throws BadRequestException {
		String username = securityHelper.getCurrentUserLogin();
		SecurityContextHolder.clearContext();
		String jwt = jwtProvider.getJwtFromRequest(request);
	
		caches.save(jwt, username, jwtProvider.getExpirationTime());
	}
		
	
	private boolean isMail(String identifier) {
		return identifier.contains(Constants.MailFormat.VTI_MAIL) || identifier.contains(Constants.MailFormat.FPT_EDU_MAIL)
				|| identifier.contains(Constants.MailFormat.FPT_MAIL) || identifier.contains(Constants.MailFormat.STARDARD_MAIL);
	}
	
	@Deprecated
	private Language getLegion() {
		final Locale userLocale = LocaleContextHolder.getLocale();
		switch(userLocale.getLanguage()) {
		case "en":
			return Language.EN;
		case "ja":
			return Language.JA;
		case "vi":
			return Language.JA;
		default:
			return Language.DEFAULT;
		}
	}
	
}
