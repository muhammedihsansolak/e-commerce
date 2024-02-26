package com.cydeo.repository;


import com.cydeo.entity.Address;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    List<Address> findAllByUser(User user);

    Optional<Address> findByStreet(String street);

    List<Address> findTop3ByUser_Email(String email);

    List<Address> findAllByUserAndName (User user, String name);

    List<Address> findAllByStreetStartingWith(String keyword);

    @Query("SELECT a FROM Address a WHERE a.user.id = ?1")
    List<Address> retrieveByUserId(Long id);
}
