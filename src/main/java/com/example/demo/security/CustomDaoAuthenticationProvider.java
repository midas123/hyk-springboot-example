package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	@Qualifier("userSecurityService")
	private UserDetailsService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String inputUsername = authentication.getName();
		String inputPassword = authentication.getCredentials().toString();
		UserDetails user = userDetailsService.loadUserByUsername(inputUsername);
		
		if(user == null) {
			throw new BadCredentialsException("인증실패");
		}
		
		if(!passwordEncoder.matches(inputPassword, user.getPassword())){
			throw new BadCredentialsException("인증실패");
		}
		
		//Authentication 인자를 그대로 리턴하면 인증 실패함, 아래처럼 UsernamePasswordAuthenticationToken 객체를 리턴해야함
		return new UsernamePasswordAuthenticationToken(user, user.getPassword(), authentication.getAuthorities());
	}
	
}
