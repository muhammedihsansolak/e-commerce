package com.cydeo.service;

import com.cydeo.dto.CartDTO;
import com.cydeo.entity.CartItem;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    boolean existById(Long id);

    CartDTO findById(Long id);

    boolean addToCart(String productCode, Integer quantity);

    BigDecimal applyDiscountToCartIfApplicableAndCalculateDiscountAmount(String discountCode, Long cartId);

    BigDecimal calculateTotalCartAmount(List<CartItem> cartItemList);

    CartDTO getCustomersCart();

}
