package com.cydeo.repository;

import com.cydeo.entity.Cart;
import com.cydeo.entity.Order;
import com.cydeo.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findTop5ByOrderByTotalPriceDesc();

    List<Order> findAllByUser_Email(String email);

    List<Order> findAllByPayment_PaymentMethod(PaymentMethod paymentMethod);

    boolean existsByUser_email(String email);

    @Query(value = "SELECT * FROM orders o JOIN cart c ON o.cart_id = c.id " +
                    "JOIN cart_item ci ON ci.cart_id = c.id " +
                    "JOIN product p ON ci.product_id= p.id WHERE p.name = ?1", nativeQuery = true)
    List<Order> retrieveAllOrdersByProductName(@Param("name") String name);

    @Query(value = "SELECT * FROM orders o JOIN cart c ON o.cart_id = c.id " +
            "JOIN cart_item ci ON ci.cart_id = c.id " +
            "JOIN product p ON ci.product_id = p.id " +
            "JOIN product_category_rel pcr ON pcr.p_id = p.id " +
            "JOIN category ca ON ca.id = pcr.c_id WHERE ca.id = ?1", nativeQuery = true)
    List<Order> retrieveAllOrdersByCategoryId(@Param("id") Long id);

    @Query("SELECT o FROM Order o WHERE o.paidPrice = o.totalPrice")
    List<Order> retrieveAllOrdersBetweenTotalPriceAndPaidPriceIsSame();


    @Query("SELECT o FROM Order o WHERE o.paidPrice<>o.totalPrice AND o.cart.discount IS NOT NULL")
    List<Order> findAllByPaidPriceAndTotalPriceAEqualsAndCartDiscountIdIsNull();

    Order findOrderByCart(Cart cart);

}
