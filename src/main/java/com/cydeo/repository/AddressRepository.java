package com.cydeo.repository;


import com.cydeo.entity.Address;
import com.cydeo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    List<Address> findAllByCustomer(Customer customer);

    Optional<Address> findByStreet(String street);

    List<Address> findTop3ByCustomer_Email(String email);

    List<Address> findAllByCustomerAndName (Customer customer, String name);

    List<Address> findAllByStreetStartingWith(String keyword);

    @Query("SELECT a FROM Address a WHERE a.customer.id = ?1")
    List<Address> retrieveByCustomerId(Long id);
}
