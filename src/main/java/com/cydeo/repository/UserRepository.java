package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT c FROM User c WHERE c.email = ?1")
    Optional<User> retrieveByCustomerEmail(String email);

    Optional<User> findByUserName(String username);
}
