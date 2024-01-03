package com.cydeo.repository;

import com.cydeo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findById(Long id);

    @Query("SELECT c FROM Customer  c WHERE c.email = ?1")
    Optional<Customer> retrieveByCustomerEmail(String email);

    Optional<Customer> findByUserName(String username);
}
