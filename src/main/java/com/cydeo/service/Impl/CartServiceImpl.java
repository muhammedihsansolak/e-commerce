package com.cydeo.service.Impl;

import com.cydeo.dto.CartDTO;
import com.cydeo.entity.*;
import com.cydeo.enums.CartState;
import com.cydeo.enums.DiscountType;
import com.cydeo.exception.*;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.*;
import com.cydeo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final Mapper mapper;
    private final CartItemRepository cartItemRepository;
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;


    @Override
    public CartDTO findById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + id));

        return mapper.convert(cart, new CartDTO());
    }

    public boolean existById(Long id) {
        return cartRepository.existsById(id);
    }

    @Override
    public boolean addToCart( String productCode, Integer quantity) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException("Product couldn't find with product code: "+ productCode));

        // quantity, that customer would like to buy, needs to be bigger than product's remaining quantity
        if (product.getRemainingQuantity() < quantity) {
            throw new NotEnoughStockException("Not enough stock");
        }

        String currentCustomerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.retrieveByCustomerEmail(currentCustomerEmail)
                .orElseThrow(() -> new CustomerNotFoundException("Customer couldn't found with email: "+ currentCustomerEmail));

        // we retrieve customer's cart, if there is no cart that belongs to the customer we create one
        // we are checking, is there any cart, duplication, size
        List<Cart> cartList = cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED);
        Cart cart = checkCartCount(cartList, customer);

        // we retrieve cart item related with the product to decide product is already there or will be added new
        CartItem cartItem = cartItemRepository.findAllByCartAndProduct(cart, product);

        // if product is already added to cart before, we will update the quantity, adding to existing quantity value
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            // if product is not added before, we will create cart item with quantity
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
            cartItem.setProduct(product);
            cartItemRepository.save(cartItem);
        }
        return true;
    }


    @Override
    public BigDecimal applyDiscountToCartIfApplicableAndCalculateDiscountAmount(String discountName, Long cartId) {
        // we retrieve discount by name and if there is no discount with the name, we need to throw exception
        Discount discount = discountRepository.findByName(discountName)
                .orElseThrow(() -> new DiscountNotFoundException("Discount not found with discount name: "+discountName));

        if (discount.getDiscount() == null || discount.getDiscount().compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new RuntimeException("Discount amount can not be null or smaller than 1");
        }

        if (discount.getMinimumAmount() == null || discount.getMinimumAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Discount minimum amount can not be null or smaller than 1");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id : "+ cartId));

        // without having any item in cart, if customer try to add discount to cart we need to throw exception
        List<CartItem> cartItemList = cartItemRepository.findAllByCart(cart);
        if (cartItemList == null || cartItemList.size() == 0) {
            throw new RuntimeException("There is no item in the cart. Hence discount cannot be applied");
        }

        BigDecimal totalCartAmount = calculateTotalCartAmount(cartItemList);

        // if cart total amount less than discount minimum amount, no discount will be added to cart
        if (discount.getMinimumAmount().compareTo(totalCartAmount) > 0) {
            return BigDecimal.ZERO;
        }

        cart.setDiscount(discount);
        cartRepository.save(cart);

        // if discount is RATE_BASED
        if (discount.getDiscountType().equals(DiscountType.RATE_BASED)) {
            return totalCartAmount
                    .multiply(discount.getDiscount())
                    .divide(new BigDecimal(100), RoundingMode.FLOOR);
        }
        // if discount is AMOUNT_BASED
        else {
            return discount.getDiscount();
        }
    }

    @Override
    public BigDecimal calculateTotalCartAmount(List<CartItem> cartItemList) {
        Function<CartItem, BigDecimal> totalMapper = cartItem -> cartItem.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return cartItemList.stream()
                .map(totalMapper)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Cart createCartForCustomer(Customer customer) {
        Cart cart = new Cart();
        cart.setCartState(CartState.CREATED);
        cart.setCustomer(customer);
        return cartRepository.save(cart);
    }

    private Cart checkCartCount(List<Cart> cartList, Customer customer) {
        if (cartList == null || cartList.size() == 0) {
            cartList = new ArrayList<>();
            Cart cart = createCartForCustomer(customer);
            cartList.add(cart);
        }
        // if customer has multiple cart as CREATED status, means that there is a problem with our values in DB
        // we shouldn't allow to customer put any item into cart, we have to fix duplication first.
        if (cartList.size() > 1) {
            throw new RuntimeException("Duplicate cart count. Check values on database");
        }

        return cartList.get(0);
    }
}
