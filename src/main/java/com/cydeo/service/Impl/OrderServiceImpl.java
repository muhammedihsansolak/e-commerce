package com.cydeo.service.Impl;

import com.cydeo.dto.OrderDTO;
import com.cydeo.dto.response.CurrencyLayerResponse;
import com.cydeo.entity.*;
import com.cydeo.enums.CartState;
import com.cydeo.enums.Currency;
import com.cydeo.enums.PaymentMethod;
import com.cydeo.exception.CurrencyInvalidException;
import com.cydeo.exception.CustomerNotFoundException;
import com.cydeo.exception.NoEnoughBalanceException;
import com.cydeo.exception.OrderNotFoundException;
import com.cydeo.client.CurrencyClient;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.*;
import com.cydeo.service.BalanceService;
import com.cydeo.service.CartService;
import com.cydeo.service.OrderService;
import com.cydeo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final Mapper mapperUtil;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final CartService cartService;
    private final CurrencyClient currencyClient;
    private final ProductService productService;
    private final BalanceRepository balanceRepository;
    private final BalanceService balanceService;

    @Value("${access_key}")
    private String accessKey;

    @Override
    public List<OrderDTO> getAll() {
        return orderRepository.findAll()
                .stream().map(order -> mapperUtil.convert(order, new OrderDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        Order order = mapperUtil.convert(orderDTO, new Order());
        order.setId(foundOrder.getId());
        Order updatedOrder = orderRepository.save(order);

        return mapperUtil.convert(updatedOrder, new OrderDTO());
    }

    @Override
    public OrderDTO create(OrderDTO orderDTO) {
        Order createdOrder = mapperUtil.convert(orderDTO, new Order());
        Order savedOrder = orderRepository.save(createdOrder);

        return mapperUtil.convert(savedOrder, new OrderDTO());
    }

    @Override
    public List<OrderDTO> getOrdersByPaymentMethod(PaymentMethod paymentMethod) {
        return orderRepository
                .findAllByPayment_PaymentMethod(paymentMethod)
                .stream().map(order -> mapperUtil.convert(order, new OrderDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByEmail(String email) {
        return orderRepository.findAllByUser_Email(email)
                .stream().map(order -> mapperUtil.convert(order, new OrderDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO findById(Long id, String currency) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        if (currency != null) {
            BigDecimal rate = currencyClient.getCurrency(accessKey, currency).getBody().getQuotes().get("USD" + currency);
            order.setTotalPrice(order.getTotalPrice().multiply(rate));
            order.setPaidPrice(order.getPaidPrice().multiply(rate));

        }
        return mapperUtil.convert(order, new OrderDTO());
    }

    @Override
    public OrderDTO findByIdAndCurrency(Long id, Optional<String> currency) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        OrderDTO orderToReturn = mapperUtil.convert(order, new OrderDTO());

        BigDecimal currencyRate = getCurrencyRate(currency);

        orderToReturn.setPaidPrice(convertCurrency(order.getPaidPrice(), currencyRate));
        orderToReturn.setTotalPrice(convertCurrency(order.getTotalPrice(), currencyRate));

        return orderToReturn;
    }

    private BigDecimal convertCurrency(BigDecimal source, BigDecimal currencyRate) {
        return source.multiply(currencyRate).setScale(2, RoundingMode.CEILING);
    }

    private BigDecimal getCurrencyRate(Optional<String> currency) throws CurrencyInvalidException {

        if (currency.isPresent() && !currency.get().equalsIgnoreCase("USD")) {
            validateCurrency(currency.get());
            CurrencyLayerResponse response = currencyClient
                    .getCurrency(accessKey, currency.get()).getBody();
            BigDecimal rate = response.getQuotes().get("USD" + currency.get().toUpperCase());
            return rate;
        } else return BigDecimal.ONE;
    }

    private void validateCurrency(String currency) throws CurrencyInvalidException {
        boolean anyMatch = Stream.of(Currency.values())
                .map(eachCurrency -> eachCurrency.value)
                .anyMatch(each -> each.equals(currency));

        if (!anyMatch) {
            throw new CurrencyInvalidException("Invalid currency!");
        }
    }

    //accepting the order
    @Override
    public BigDecimal placeOrder(String paymentMethod, String discountCode) {
        String currentCustomerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.retrieveByCustomerEmail(currentCustomerEmail)
                .orElseThrow(() -> new CustomerNotFoundException("Customer couldn't find with e-mail: " + currentCustomerEmail));

        //retrieve customer cart, which has CREATED status, based on customer id
        List<Cart> cartList = cartRepository.findAllByUserIdAndCartState(user.getId(), CartState.CREATED);
        if (cartList == null || cartList.size() == 0) {
            throw new RuntimeException("Cart couldn't find");
        }

        //there always be 1 cart with CREATED state
        Cart cart = cartList.get(0);

        //retrieve all cart items belong to cart
        List<CartItem> cartItemList = cartItemRepository.findAllByCart(cart);

        // if there is no item in the cart we are returning ZERO because this method should be return paid price
        if (cartItemList.size() == 0) {
            return BigDecimal.ZERO;
        }

        // if there is an item quantity that exceeds product remaining quantity, remove it from cart item list
        cartItemList.removeIf(cartItem ->
                cartItem.getQuantity() > cartItem.getProduct().getRemainingQuantity()
        );

        // Before placing order discount must have been applied to cart.
        BigDecimal discountAmount = cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount(discountCode, cart.getId());

        BigDecimal totalCartAmount = cartService.calculateTotalCartAmount(cartItemList);

        Order order = new Order();
        order.setCart(cart);
        order.setUser(user);
        order.setTotalPrice(totalCartAmount);

        // calculating cart total amount after discount
        BigDecimal paidPrice = totalCartAmount.subtract(discountAmount);
        paidPrice = paidPrice.setScale(2, RoundingMode.HALF_UP); //rounding
        order.setPaidPrice(paidPrice);

        //find payment method
        PaymentMethod foundPaymentMethod = Arrays.stream(PaymentMethod.values())
                .filter(payments -> payments.getPaymentMethod().equals(paymentMethod)).findFirst()
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentMethod +
                        ". Available payment methods are: " +
                        Arrays.stream(PaymentMethod.values())
                                .map(PaymentMethod::getPaymentMethod)
                                .collect(Collectors.joining(","))
                ));

        // additional discount (10 $) for specific payment method for "credit card" payment method
        if (foundPaymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            order.setPaidPrice(order.getPaidPrice().subtract(BigDecimal.TEN));
        }

        // additional discount (20 $) for specific payment method for "balance" payment method
        if (foundPaymentMethod.equals(PaymentMethod.BALANCE)) {
            order.setPaidPrice(order.getPaidPrice().subtract(BigDecimal.valueOf(20)));

            //if there is no enough balance app throws exception
            BigDecimal balance = balanceRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Customer " + user.getEmail() + " has no balance in the system!"))
                    .getAmount();

            if (balance.compareTo(order.getPaidPrice()) < 0) {
                throw new NoEnoughBalanceException("Customer balance (" + balance + ") is not enough for pay " + order.getPaidPrice() + " !");
            }
        }


        Payment payment = new Payment();
        payment.setPaidPrice(order.getPaidPrice());
        payment.setPaymentMethod(foundPaymentMethod);
        payment = paymentRepository.save(payment);

        order.setPayment(payment);

        //if payment method is "balance", decrease customers balance
        if (foundPaymentMethod.equals(PaymentMethod.BALANCE)) {
            balanceService.decreaseCustomerBalance(user.getId(), order.getPaidPrice());
        }

        // decrease product remaining quantity
//        cartItemList.forEach(cartItem -> {
//            cartItem.getProduct().setRemainingQuantity(
//                    cartItem.getProduct().getRemainingQuantity() - cartItem.getQuantity());
//            cartItemRepository.save(cartItem);
//        });
        cartItemList.forEach(cartItem -> productService.decreaseProductRemainingQuantity(cartItem.getProduct(), cartItem.getQuantity()));

        //change cart state to sold
        cart.setCartState(CartState.SOLD);
        cartRepository.save(cart);

        orderRepository.save(order);
        return order.getPaidPrice();
    }

    @Override
    public List<OrderDTO> getAllCustomerOrders() {
        String currentCustomerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Order> allByCustomerEmail = orderRepository.findAllByUser_Email(currentCustomerEmail);

        return allByCustomerEmail.stream()
                .map(order -> mapperUtil.convert(order, new OrderDTO()))
                .collect(Collectors.toList());
    }

}














