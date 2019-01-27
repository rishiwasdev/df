package com.ct.dealer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ct.dealer.model.Role;
import com.ct.dealer.model.RoleName;

@Repository
public interface RoleRepository extends JpaRepository< Role, Long > {
	Optional< Role > findByName( RoleName roleName );
}