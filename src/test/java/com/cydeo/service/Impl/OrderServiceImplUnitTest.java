package com.cydeo.service.Impl;

import com.cydeo.client.CurrencyClient;
import com.cydeo.entity.Cart;
import com.cydeo.entity.CartItem;
import com.cydeo.entity.Customer;
import com.cydeo.entity.Product;
import com.cydeo.enums.CartState;
import com.cydeo.exception.CustomerNotFoundException;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.*;
import com.cydeo.service.CartService;
import com.cydeo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplUnitTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private Mapper mapperUtil;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CartService cartService;
    @Mock
    private CurrencyClient currencyClient;
    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderServiceImpl orderService;

    //******************************************
    //              placeOrder()
    //******************************************

    @Test
    void should_place_order_and_apply_discount_when_payment_method_is_transfer_and_discount_code_valid(){
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer customer = new Customer();

        Cart cart = new Cart();

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);

        Product product = new Product();
        product.setRemainingQuantity(50);
        product.setQuantity(50);
        product.setPrice(BigDecimal.valueOf(25));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(10);
        cartItem.setCart(cart);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        BigDecimal cartTotalAmount = BigDecimal.valueOf(250);

        when(authentication.getName()).thenReturn(customer.getEmail());
        when(customerRepository.retrieveByCustomerEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED)).thenReturn(cartList);
        when(cartItemRepository.findAllByCart(cart)).thenReturn(cartItemList);
        when(cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount("ORMFRG",cart.getId())).thenReturn(BigDecimal.valueOf(50));
        when(cartService.calculateTotalCartAmount(cartItemList)).thenReturn(cartTotalAmount);

        BigDecimal paidPrice = BigDecimal.valueOf(200);

        assertThat(orderService.placeOrder("Transfer" , "ORMFRG")).isEqualTo(paidPrice);
    }

    @Test
    void should_throw_exception_when_customer_cannot_found(){
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String nonExistingEmail = "";

        when(authentication.getName()).thenReturn(nonExistingEmail);
        when(customerRepository.retrieveByCustomerEmail(nonExistingEmail)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> orderService.placeOrder("Transfer", "ORMFRG"));

        assertThat(throwable).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void should_throw_exception_when_cart_list_is_zero(){
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer customer = new Customer();

        List<Cart> cartList = new ArrayList<>();

        when(authentication.getName()).thenReturn(customer.getEmail());
        when(customerRepository.retrieveByCustomerEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED)).thenReturn(cartList);

        Throwable throwable = catchThrowable(() -> orderService.placeOrder("Transfer", "ORMFRG"));

        assertThat(throwable).isInstanceOf(RuntimeException.class);
        assertThat(throwable).hasMessage("Cart couldn't find");
    }

    @Test
    void should_throw_exception_when_cart_list_is_null(){
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer customer = new Customer();

        List<Cart> cartList = new ArrayList<>();
        cartList = null;

        when(authentication.getName()).thenReturn(customer.getEmail());
        when(customerRepository.retrieveByCustomerEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED)).thenReturn(cartList);

        Throwable throwable = catchThrowable(() -> orderService.placeOrder("Transfer", "ORMFRG"));

        assertThat(throwable).isInstanceOf(RuntimeException.class);
        assertThat(throwable).hasMessage("Cart couldn't find");
    }

    @Test
    void should_return_zero_when_cart_item_list_has_no_item(){
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer customer = new Customer();

        Cart cart = new Cart();

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);

        List<CartItem> cartItemList = new ArrayList<>();

        when(authentication.getName()).thenReturn(customer.getEmail());
        when(customerRepository.retrieveByCustomerEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED)).thenReturn(cartList);
        when(cartItemRepository.findAllByCart(cart)).thenReturn(cartItemList);

        assertThat(orderService.placeOrder("Transfer", "ORMFRG")).isEqualTo(BigDecimal.ZERO);
    }



}