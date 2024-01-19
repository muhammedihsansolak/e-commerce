package com.cydeo.service.Impl;

import com.cydeo.dto.CartDTO;
import com.cydeo.dto.CartItemDTO;
import com.cydeo.entity.Cart;
import com.cydeo.entity.CartItem;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.CartItemRepository;
import com.cydeo.service.CartItemService;
import com.cydeo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final Mapper mapper;
    private final CartService cartService;

    /**
     * Returns cart items belongs to the current (logged-in) customer's cart
     * @return List<CartItemDTO>
     */
    @Override
    public List<CartItemDTO> findAll() {
        CartDTO customersCart = cartService.getCustomersCart();
        Cart convertedCart = mapper.convert(customersCart, new Cart());

        List<CartItem> cartItemList = cartItemRepository.findAllByCart(convertedCart);

        return cartItemList.stream()
                .map(cartItem -> mapper.convert(cartItem, new CartItemDTO()))
                .collect(Collectors.toList());
    }
}
