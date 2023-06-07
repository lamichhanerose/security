package com.example.security.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer ,Integer> {
    Optional<Customer> findByEmail(String email);
    List<Customer> findAll ();
}
