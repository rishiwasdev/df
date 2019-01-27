package com.ct.dealer.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ct.dealer.exception.ResourceNotFoundException;
import com.ct.dealer.model.Dealer;
import com.ct.dealer.repository.DealerRepository;

@RestController
@RequestMapping( "/api" )
public class DealerController {
	@Autowired
	private DealerRepository dealerRepository;

	@GetMapping( "/dealers" )
	@PreAuthorize( "hasRole('USER')" ) // or hasRole('ADMIN')
	public List< Dealer > getDealers() {
		return dealerRepository.findAll();
	}

	@Transactional
	@PostMapping( "/dealers" )
	@PreAuthorize( "hasRole('USER')" ) // or hasRole('ADMIN')
	public @Valid Dealer createDealer( @Valid @RequestBody Dealer dealer ) {
		return dealerRepository.save(dealer);
	}

	@Cacheable( value = "cacheDealerId" )
	@GetMapping( "/dealers/{id}" )
	@PreAuthorize( "hasRole('USER')" ) // or hasRole('ADMIN')
	public ResponseEntity< Dealer > getDealerById( @PathVariable( value = "id" ) Long dealerId )
			throws ResourceNotFoundException {
		Dealer dealer = dealerRepository.findById(dealerId)
				.orElseThrow(() -> new ResourceNotFoundException("Dealer not found for this id: " + dealerId));
		return ResponseEntity.ok().body(dealer);
	}

	@Transactional
	@PutMapping( "/dealers/{id}" )
	@PreAuthorize( "hasRole('USER')" ) // or hasRole('ADMIN')
	public @ResponseBody ResponseEntity< Dealer > updateDealer( @PathVariable( value = "id" ) Long dealerId,
			@Valid @RequestBody Dealer dealer ) throws ResourceNotFoundException {
		Dealer dlr = dealerRepository.findById(dealerId)
				.orElseThrow(() -> new ResourceNotFoundException("Dealer not found for this id: " + dealerId));
		dlr.setEmail(dealer.getEmail());
		dlr.setName(dealer.getName());
		dlr.setCity(dealer.getCity());
		dlr.setActive(dlr.getActive());
		final Dealer updatedDealer = dealerRepository.save(dlr);
		return ResponseEntity.ok().body(updatedDealer);
	}

	@Transactional
	@DeleteMapping( "/dealers/{dealerId}" )
	@PreAuthorize( "hasRole('USER')" ) // or hasRole('ADMIN')
	public @ResponseBody ResponseEntity< Dealer > deleteDealer( @PathVariable( value = "dealerId" ) Long dealerId )
			throws ResourceNotFoundException {
		Dealer dealer = dealerRepository.findById(dealerId)
				.orElseThrow(() -> new ResourceNotFoundException("Dealer not found for this id: " + dealerId));
		dealerRepository.deleteById(dealerId);
		return ResponseEntity.ok().body(dealer);
	}
}