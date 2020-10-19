package com.example.demo.security;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.UserInfo;
import com.example.demo.repository.UserRepository;

@Service
public class UserSecurityService implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userRepository.findByUserName(username);
		if (user == null) {
            throw new UsernameNotFoundException(username);
        }
		return new UserPrincipal(user,new ArrayList<SimpleGrantedAuthority>( 
	            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
	}
	
}
