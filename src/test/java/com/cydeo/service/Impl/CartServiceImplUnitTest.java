package com.cydeo.service.Impl;

import com.cydeo.entity.Cart;
import com.cydeo.entity.CartItem;
import com.cydeo.entity.Customer;
import com.cydeo.entity.Product;
import com.cydeo.entity.principal.UserPrincipal;
import com.cydeo.enums.CartState;
import com.cydeo.exception.CustomerNotFoundException;
import com.cydeo.exception.NotEnoughStockException;
import com.cydeo.exception.ProductNotFoundException;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.*;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplUnitTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private Mapper mapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private DiscountRepository discountRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    //******************************************
    //              add to cart
    //******************************************

    @Test
    void should_throw_exception_when_product_could_not_found_with_product_code(){
        when(productRepository.findByProductCode(anyString())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> cartService.addToCart("productCode", 5));
        assertThat(throwable).isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void should_throw_exception_when_product_remaining_quantity_is_less_than_quantity(){
        Product product = new Product();
        product.setRemainingQuantity(10);

        when(productRepository.findByProductCode(anyString())).thenReturn(Optional.of(product));

        Throwable throwable = catchThrowable(() -> cartService.addToCart("productCode", product.getRemainingQuantity() + 1));

        assertEquals( "Not enough stock" ,throwable.getMessage());
        assertThat(throwable).isInstanceOf(NotEnoughStockException.class);
    }

    @Test
    void should_throw_exception_when_current_customer_not_found() {
        Product product = new Product();
        product.setRemainingQuantity(10);

        String nonExistingEmail = "abc@email.com";

        Customer customer = new Customer();
        customer.setEmail(nonExistingEmail);

        // Mocking the current authentication context with a custom Authentication object
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(productRepository.findByProductCode(anyString())).thenReturn(Optional.of(product));
        when(authentication.getName()).thenReturn(nonExistingEmail);
        when(customerRepository.retrieveByCustomerEmail(nonExistingEmail)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(CustomerNotFoundException.class, () -> cartService.addToCart("productCode", 1));
    }

    @Test
    void should_add_item_to_cart_when_cart_is_exists_and_cart_item_exists_in_the_cart(){
        Product product = new Product();
        product.setRemainingQuantity(10);

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String existingEmail = "abc@email.com";

        Customer customer = new Customer();
        customer.setEmail(existingEmail);
        customer.setId(1L);

        Cart cart = new Cart();
        cart.setCartState(CartState.CREATED);
        cart.setCustomer(customer);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setQuantity(5);
        cartItem.setProduct(product);

        when(productRepository.findByProductCode(anyString())).thenReturn(Optional.of(product));
        when(authentication.getName()).thenReturn(existingEmail);
        when(customerRepository.retrieveByCustomerEmail(existingEmail)).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED)).thenReturn(cartList);
        when(cartItemRepository.findAllByCartAndProduct(cart, product)).thenReturn(cartItem);

        assertTrue(cartService.addToCart("productCode", 5));
    }

    @Test
    void should_add_item_to_cart_when_cart_is_not_exists(){
        Product product = new Product();
        product.setRemainingQuantity(10);

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String existingEmail = "abc@email.com";

        Customer customer = new Customer();
        customer.setEmail(existingEmail);
        customer.setId(1L);

        Cart cart = new Cart();
        cart.setCartState(CartState.CREATED);
        cart.setCustomer(customer);

        List<Cart> cartList = new ArrayList<>();

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setQuantity(5);
        cartItem.setProduct(product);

        when(productRepository.findByProductCode(anyString())).thenReturn(Optional.of(product));
        when(authentication.getName()).thenReturn(existingEmail);
        when(customerRepository.retrieveByCustomerEmail(existingEmail)).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED)).thenReturn(cartList);
        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartItemRepository.findAllByCartAndProduct(cart, product)).thenReturn(cartItem);

        assertTrue(cartService.addToCart("productCode", 5));
    }

    @Test
    void should_throw_exception_when_cart_list_size_is_more_than_one(){
        Product product = new Product();
        product.setRemainingQuantity(10);

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String existingEmail = "abc@email.com";

        Customer customer = new Customer();
        customer.setEmail(existingEmail);
        customer.setId(1L);

        Cart cart1 = new Cart();
        cart1.setCartState(CartState.CREATED);
        cart1.setCustomer(customer);

        Cart cart2 = new Cart();
        cart1.setCartState(CartState.CREATED);
        cart1.setCustomer(customer);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart1);
        cartList.add(cart2);

        when(productRepository.findByProductCode(anyString())).thenReturn(Optional.of(product));
        when(authentication.getName()).thenReturn(existingEmail);
        when(customerRepository.retrieveByCustomerEmail(existingEmail)).thenReturn(Optional.of(customer));
        when(cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED)).thenReturn(cartList);

        Throwable throwable = catchThrowable(() -> cartService.addToCart("productCode", product.getRemainingQuantity() - 1));
        assertEquals( "Duplicate cart count. Check values on database" , throwable.getMessage());
    }

}