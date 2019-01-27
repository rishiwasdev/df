package com.ct.dealer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ct.dealer.model.Dealer;

@Repository
public interface DealerRepository extends JpaRepository< Dealer, Long > {
}
