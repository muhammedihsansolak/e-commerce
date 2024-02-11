package com.cydeo.repository;


import com.cydeo.entity.Balance;
import com.cydeo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    boolean existsBalanceByCustomer(Customer customer);
    boolean existsByCustomerId(Long id);

    Optional<Balance> findByCustomer(Customer customer);
    Balance findByCustomer_Id(Long id);

    @Query(value = "SELECT * FROM balance ORDER BY amount DESC LIMIT 5", nativeQuery = true)
    List<Balance> retrieveTop5Amount();

    List<Balance> findAllByAmountGreaterThanEqual(BigDecimal amount);

    @Query(value = "SELECT * FROM balance WHERE amount <?1", nativeQuery = true)
    List<Balance> retrieveBalanceLessThanAmount(@Param("amount") BigDecimal amount);

}
