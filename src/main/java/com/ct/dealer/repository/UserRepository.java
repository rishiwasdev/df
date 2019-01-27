package com.ct.dealer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ct.dealer.model.User;

@Repository
public interface UserRepository extends JpaRepository< User, Long > {
	Optional< User > findById( Long id );

	Optional< User > findByUsername( String firstName );

	Optional< User > findByEmail( String email );

	Boolean existsByUsername( String username );

	Boolean existsByEmail( String email );
}