package com.cydeo.repository;

import com.cydeo.entity.Discount;
import com.cydeo.enums.DiscountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByDiscountCode(String discountCode);

    List<Discount> findAllByDiscountAmountGreaterThan(BigDecimal amount);

    List<Discount> findAllByDiscountType(DiscountType discountType);

    @Query("SELECT d FROM Discount d WHERE d.discountAmount BETWEEN ?1 AND ?2")
    List<Discount> findAllByRangeBetweenAmount(BigDecimal startAmount, BigDecimal endAmount);


}
