package com.bptn.feedApp.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired; 
import com.bptn.feedApp.jpa.User;
import com.bptn.feedApp.repository.UserRepository; 
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public List<User> listUsers() {
		return this.userRepository.findAll();
	}
	
	public  Optional<User>  findByUsername(String username) {
		return this.userRepository.findByUsername (username);
	}
	
	public void createUser(User user) {
		this.userRepository.save(user);
	}
}