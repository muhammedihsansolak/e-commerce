package com.cydeo.repository;

import com.cydeo.entity.Cart;
import com.cydeo.enums.CartState;
import com.cydeo.enums.DiscountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    List<Cart> findAllByDiscountDiscountType(DiscountType discountType);

    @Query("SELECT c FROM Cart c WHERE c.user.id = ?1")
    List<Cart> retrieveCartListByCustomer(Long id);

     List<Cart> findAllByUserIdAndCartState(Long id, CartState cartState);

    List<Cart> findAllByUserIdAndCartStateAndDiscountIsNull(Long id, CartState cartState);

    @Query(value= "SELECT * FROM carts c JOIN users cu ON c.customer_id = cu.id WHERE c.cart_state = ?1 AND cu.id = ?2 AND c.discount.id is not null", nativeQuery = true)
    List<Cart> findAllByCustomerIdAndCartStateAndDiscountIsNotNull(String cartState, Long id);

    List<Cart> findByUser_EmailAndCartState(String email, CartState cartState);

}
