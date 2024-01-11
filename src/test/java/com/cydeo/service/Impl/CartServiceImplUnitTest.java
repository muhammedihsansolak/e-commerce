package com.cydeo.service.Impl;

import com.cydeo.entity.*;
import com.cydeo.enums.CartState;
import com.cydeo.enums.DiscountType;
import com.cydeo.exception.CustomerNotFoundException;
import com.cydeo.exception.DiscountNotFoundException;
import com.cydeo.exception.NotEnoughStockException;
import com.cydeo.exception.ProductNotFoundException;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
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
    //              addToCart()
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

    //**************************************************************************************************
    //              applyDiscountToCartIfApplicableAndCalculateDiscountAmount()
    //**************************************************************************************************

    @Test
    void should_throw_exception_when_discount_cannot_found_with_discount_name(){

        when(discountRepository.findByName(anyString())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount("discountName", 1L));

        assertEquals("Discount not found with discount name: "+"discountName", throwable.getMessage());
        assertThat(throwable).isInstanceOf(DiscountNotFoundException.class);
    }

    @Test
    void should_throw_exception_when_discount_amount_is_null(){
        Discount discount = new Discount();
        discount.setDiscount(null);

        when(discountRepository.findByName(anyString())).thenReturn(Optional.of(discount));

        Throwable throwable = catchThrowable(() -> cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount("discountName", 1L));
        assertEquals("Discount amount can not be null or smaller than 1", throwable.getMessage());
    }

    @Test
    void should_throw_exception_when_discount_amount_is_smaller_than_one(){
        Discount discount = new Discount();
        discount.setDiscount(BigDecimal.ZERO);

        when(discountRepository.findByName(anyString())).thenReturn(Optional.of(discount));

        Throwable throwable = catchThrowable(() -> cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount("discountName", 1L));
        assertEquals("Discount amount can not be null or smaller than 1", throwable.getMessage());
    }

    @Test
    void should_throw_exception_when_discount_minimum_amount_is_null(){
        Discount discount = new Discount();
        discount.setDiscount(BigDecimal.TEN);
        discount.setMinimumAmount(null);

        when(discountRepository.findByName(anyString())).thenReturn(Optional.of(discount));

        Throwable throwable = catchThrowable(() -> cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount("discountName", 1L));
        assertEquals("Discount minimum amount can not be null or smaller than 1", throwable.getMessage());
    }

    @Test
    void should_throw_exception_when_discount_minimum_amount_is_smaller_than_one(){
        Discount discount = new Discount();
        discount.setDiscount(BigDecimal.TEN);
        discount.setMinimumAmount(BigDecimal.ZERO);

        when(discountRepository.findByName(anyString())).thenReturn(Optional.of(discount));

        Throwable throwable = catchThrowable(() -> cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount("discountName", 1L));
        assertEquals("Discount minimum amount can not be null or smaller than 1", throwable.getMessage());
    }

    @Test
    void should_throw_exception_when_cart_cannot_found_with_id(){
        Discount discount = new Discount();
        discount.setDiscount(BigDecimal.TEN);
        discount.setMinimumAmount(BigDecimal.TEN);

        Long cartId = 1L;

        when(discountRepository.findByName(anyString())).thenReturn(Optional.of(discount));
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount("discountName", 1L));
        assertEquals("Cart not found with id : " + cartId , throwable.getMessage());
    }

    @Test
    void should_throw_exception_when_cart_item_list_is_null(){
        Discount discount = new Discount();
        discount.setDiscount(BigDecimal.TEN);
        discount.setMinimumAmount(BigDecimal.TEN);

        Cart cart = new Cart();

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList = null;

        when(discountRepository.findByName(anyString())).thenReturn(Optional.of(discount));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCart(cart)).thenReturn(cartItemList);

        Throwable throwable = catchThrowable(() -> cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount("discountName", 1L));
        assertEquals("There is no item in the cart. Hence discount cannot be applied", throwable.getMessage());
    }

    @Test
    void should_throw_exception_when_cart_item_list_has_no_item(){
        Discount discount = new Discount();
        discount.setDiscount(BigDecimal.TEN);
        discount.setMinimumAmount(BigDecimal.TEN);

        Cart cart = new Cart();

        List<CartItem> cartItemList = new ArrayList<>();

        when(discountRepository.findByName(anyString())).thenReturn(Optional.of(discount));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCart(cart)).thenReturn(cartItemList);

        Throwable throwable = catchThrowable(() -> cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount("discountName", 1L));
        assertEquals("There is no item in the cart. Hence discount cannot be applied", throwable.getMessage());
    }

    @Test
    void should_return_zero_if_cart_total_amount_less_than_discount_minimum_amount(){
        Discount discount = new Discount();
        discount.setDiscount(BigDecimal.TEN);
        discount.setMinimumAmount(BigDecimal.valueOf(500));

        Cart cart = new Cart();

        Product product1 = new Product();
        product1.setPrice(BigDecimal.valueOf(30));

        Product product2 = new Product();
        product2.setPrice(BigDecimal.valueOf(15));

        CartItem cartItem1 = new CartItem();
        cartItem1.setCart(cart);
        cartItem1.setQuantity(5);
        cartItem1.setProduct(product1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setCart(cart);
        cartItem2.setQuantity(10);
        cartItem2.setProduct(product2);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem1);
        cartItemList.add(cartItem2);

        //cartTotalAmount -> $ 300 but discount min amount -> $ 500

        when(discountRepository.findByName(anyString())).thenReturn(Optional.of(discount));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCart(cart)).thenReturn(cartItemList);

        assertThat(cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount("discountName", 1L)).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void should_apply_amount_based_discount_to_cart_and_save_database(){
        Discount discount = new Discount();
        discount.setDiscount(BigDecimal.TEN);
        discount.setMinimumAmount(BigDecimal.valueOf(150));
        discount.setDiscountType(DiscountType.AMOUNT_BASED);

        Cart cart = new Cart();

        Product product1 = new Product();
        product1.setPrice(BigDecimal.valueOf(30));

        Product product2 = new Product();
        product2.setPrice(BigDecimal.valueOf(15));

        CartItem cartItem1 = new CartItem();
        cartItem1.setCart(cart);
        cartItem1.setQuantity(5);
        cartItem1.setProduct(product1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setCart(cart);
        cartItem2.setQuantity(10);
        cartItem2.setProduct(product2);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem1);
        cartItemList.add(cartItem2);

        //cartTotalAmount -> $ 300 discount min amount -> $ 150. discount amount should be applied

        when(discountRepository.findByName("discountName")).thenReturn(Optional.of(discount));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCart(cart)).thenReturn(cartItemList);

        assertThat(cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount("discountName", 1L)).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void should_apply_rate_based_discount_to_cart_and_save_database(){
        Discount discount = new Discount();
        discount.setDiscount(BigDecimal.TEN);
        discount.setMinimumAmount(BigDecimal.valueOf(150));
        discount.setDiscountType(DiscountType.RATE_BASED);

        Cart cart = new Cart();

        Product product1 = new Product();
        product1.setPrice(BigDecimal.valueOf(30));

        Product product2 = new Product();
        product2.setPrice(BigDecimal.valueOf(15));

        CartItem cartItem1 = new CartItem();
        cartItem1.setCart(cart);
        cartItem1.setQuantity(5);
        cartItem1.setProduct(product1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setCart(cart);
        cartItem2.setQuantity(10);
        cartItem2.setProduct(product2);

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem1);
        cartItemList.add(cartItem2);

        //cartTotalAmount -> $ 300 discount min amount -> $ 150. discount amount should be applied

        when(discountRepository.findByName("discountName")).thenReturn(Optional.of(discount));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCart(cart)).thenReturn(cartItemList);

        assertThat(cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount("discountName", 1L)).isEqualTo(BigDecimal.valueOf(30));
    }



}