package com.foodbridge.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.foodbridge.user.entity.User;
import com.foodbridge.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository=userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user=userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
		return org.springframework.security.core.userdetails.User
		        .withUsername(user.getEmail())
		        .password(user.getPassword())
		        .roles(user.getRole().name())
		        .build();
	}

}