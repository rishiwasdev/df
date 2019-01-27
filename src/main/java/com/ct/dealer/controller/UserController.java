package com.ct.dealer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ct.dealer.exception.ResourceNotFoundException;
import com.ct.dealer.model.User;
import com.ct.dealer.repository.UserRepository;

@RestController
@RequestMapping( "/api" )
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping( "/users" )
	@PreAuthorize( "hasRole('USER')" ) // or hasRole('ADMIN')
	public List< User > userAccess() {
		return userRepository.findAll();
	}

	@Transactional
	@PostMapping( "/users" )
	@PreAuthorize( "hasRole('USER')" ) // or hasRole('ADMIN')
	public User createUser( @Valid @RequestBody User user ) {
		return userRepository.save(user);
	}

	// @GetMapping( "/pm" ) @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
	// public String projectManagementAccess() { return ">>> Board Management Project"; }
	// @GetMapping("/admin") @PreAuthorize("hasRole('ADMIN')") public String adminAccess() {return ">>> Admin Contents";}

	@Cacheable( value = "cacheUserId" )
	@GetMapping( "/users/{id}" )
	@PreAuthorize( "hasRole('USER')" ) // or hasRole('ADMIN')
	public ResponseEntity< User > getUserById( @PathVariable( value = "id" ) Long userId ) throws ResourceNotFoundException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		return ResponseEntity.ok().body(user);
	}

	@Transactional
	@PutMapping( "/users/{id}" )
	@PreAuthorize( "hasRole('USER')" ) // or hasRole('ADMIN')
	public User updateUser( @PathVariable( value = "id" ) Long userId, @Valid @RequestBody User user )
			throws ResourceNotFoundException {
		User u = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));
		u.setEmail(user.getEmail());
		u.setName(user.getName());
		u.setPassword(user.getPassword());
		u.setRoles(user.getRoles());
		u.setUsername(user.getUsername());
		final User updatedUser = userRepository.save(u);
		return updatedUser;
	}

	@Transactional
	@DeleteMapping( "/users/{id}" )
	@PreAuthorize( "hasRole('USER')" ) // or hasRole('ADMIN')
	public Map< String, Boolean > deleteUser( @PathVariable( value = "id" ) Long userId ) throws ResourceNotFoundException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		userRepository.delete(user);
		Map< String, Boolean > response = new HashMap<>();
		response.put("User Deleted", Boolean.TRUE);
		return response;
	}
}