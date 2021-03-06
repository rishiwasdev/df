package com.ct.dealer.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ct.dealer.message.request.LoginForm;
import com.ct.dealer.message.request.SignUpForm;
import com.ct.dealer.message.response.JwtResponse;
import com.ct.dealer.model.Role;
import com.ct.dealer.model.RoleName;
import com.ct.dealer.model.User;
import com.ct.dealer.repository.RoleRepository;
import com.ct.dealer.repository.UserRepository;
import com.ct.dealer.security.jwt.JwtProvider;

@CrossOrigin( origins = "*", maxAge = 3600 )
@RestController
@RequestMapping( "/api/auth" )
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtProvider jwtProvider;

	@PostMapping( "/signin" )
	public ResponseEntity< ? > authenticateUser( @Valid @RequestBody LoginForm loginRequest ) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateJwtToken(authentication);
		return ResponseEntity.ok(new JwtResponse(jwt));
	}

	@PostMapping( "/signup" )
	public ResponseEntity< String > registerUser( @Valid @RequestBody SignUpForm signUpRequest ) {
		if( userRepository.existsByUsername(signUpRequest.getUsername()) )
			return new ResponseEntity< String >("Fail -> Username is already taken!", HttpStatus.BAD_REQUEST);
		if( userRepository.existsByEmail(signUpRequest.getEmail()) )
			return new ResponseEntity< String >("Fail -> Email is already in use!", HttpStatus.BAD_REQUEST);
		// Creating user's account
		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));
		Set< String > strRoles = signUpRequest.getRole();
		Set< Role > roles = new HashSet<>();
		strRoles.forEach(role -> {
			switch( role ) {
				case "user":
					Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Fail! -> User Role not found."));
					roles.add(userRole);
					// case "admin":
					// Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
					// .orElseThrow(() -> new RuntimeException("Fail! -> User Role not found."));
					// roles.add(adminRole);
					// break;
					// case "pm":
					// Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
					// .orElseThrow(() -> new RuntimeException("Fail! -> User Role not found."));
					// roles.add(pmRole);
					// break;
			}
		});
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok().body("User registered successfully!");
	}
}