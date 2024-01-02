package com.cydeo.service.Impl;

import com.cydeo.dto.OrderDTO;
import com.cydeo.dto.response.CurrencyLayerResponse;
import com.cydeo.entity.*;
import com.cydeo.enums.CartState;
import com.cydeo.enums.Currency;
import com.cydeo.enums.PaymentMethod;
import com.cydeo.exception.CurrencyInvalidException;
import com.cydeo.exception.OrderNotFoundException;
import com.cydeo.client.CurrencyClient;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.*;
import com.cydeo.service.CartService;
import com.cydeo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final Mapper mapperUtil;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final CartService cartService;
    private final CurrencyClient currencyClient;

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
                .orElseThrow( () -> new OrderNotFoundException("Order not found with id: "+orderId) );
        Order entity = mapperUtil.convert(orderDTO, new Order());
        entity.setId(foundOrder.getId());
        Order updatedOrder = orderRepository.save(entity);
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
        return orderRepository.findAllByCustomer_Email(email)
                .stream().map(order -> mapperUtil.convert(order, new OrderDTO()))
                .collect(Collectors.toList());
    }


    @Override
    public OrderDTO findById(Long id, String currency) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        if (currency != null){
            BigDecimal rate = currencyClient.getCurrency(accessKey, currency).getBody().getQuotes().get("USD" + currency);
            order.setTotalPrice( order.getTotalPrice().multiply(rate) );
            order.setPaidPrice( order.getPaidPrice().multiply(rate) );

        }
        return mapperUtil.convert(order, new OrderDTO());
    }

    @Override
    public OrderDTO findByIdAndCurrency(Long id, Optional<String> currency) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        OrderDTO orderToReturn = mapperUtil.convert(order, new OrderDTO());

        BigDecimal currencyRate = getCurrencyRate(currency);

        orderToReturn.setPaidPrice( convertCurrency(order.getPaidPrice(), currencyRate) );
        orderToReturn.setTotalPrice( convertCurrency(order.getTotalPrice(), currencyRate) );

        return orderToReturn;
    }

    private BigDecimal convertCurrency(BigDecimal source, BigDecimal currencyRate) {
        return source.multiply(currencyRate).setScale(2, RoundingMode.CEILING);
    }

    private BigDecimal getCurrencyRate(Optional<String> currency) throws CurrencyInvalidException {

        if (currency.isPresent() && ! currency.get().equalsIgnoreCase("USD")){
            validateCurrency(currency.get());
            CurrencyLayerResponse response = currencyClient
                    .getCurrency(accessKey, currency.get()).getBody();
            BigDecimal rate = response.getQuotes().get("USD" + currency.get().toUpperCase());
            return rate;
        }else return BigDecimal.ONE;
    }

    private void validateCurrency(String currency) throws CurrencyInvalidException {
        boolean anyMatch = Stream.of(Currency.values())
                .map(eachCurrency -> eachCurrency.value)
                .anyMatch(each -> each.equals(currency));

        if (! anyMatch){
            throw new CurrencyInvalidException("Invalid currency!");
        }

    }

    // Now, it's time to pay, show us you really want it, Also you get your dream discount. Well deserved.
    // Calm down... You will have it. There is only one click left...
    // You have selected desired product. We will have customer's money and place the order.
    // This method responsibility is to place the shiny order.
    // After that you can have a fresh air and refresh the page to see your order is shipped.
    // But first we have to accept the order. This method does that.
    @Override
    public BigDecimal placeOrder(PaymentMethod paymentMethod, Long cartId, Long customerId) {
        // we retrieve customer from DB, if not exists we need to throw exception
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer couldn't find"));

        // if a customer would like to place an order, cart should be created before.
        List<Cart> cartList = cartRepository.findAllByCustomerIdAndCartState(customer.getId(), CartState.CREATED);
        // if there is no cart in DB, we need to throw exception
        // Once customer place order cart status will be SOLD and after that if customer would like to buy something
        // again a new cart will be created. That's why a customer can have multiple carts but only one cart with CREATE status
        // can be exist run time. All other carts should be SOLD status
        if (cartList == null || cartList.size() == 0) {
            throw new RuntimeException("Cart couldn't find or cart is empty");
        }

        // according to business requirement there always be 1 cart with created state
        // That's why we are retrieving first index of cart list
        Cart cart = cartList.get(0);

        // We retrieve cart items in the cart
        List<CartItem> cartItemList = cartItemRepository.findAllByCart(cartList.get(0));

        // if there is an item quantity that exceeds product remains quantity, we have to remove it from cart item list;
        cartItemList.removeIf(cartItem ->
                cartItem.getQuantity() > cartItem.getProduct().getRemainingQuantity());
        // if there is no item in the cart we are returning ZERO because this method should be returned paid price.
        // No item means you can not pay anything
        if (cartItemList.size() == 0){
            return BigDecimal.ZERO;
        }

        // Discounts can be applied to cart but it is not mandatory. At first discount amount will be ZERO
        // If a discount can be applicable to cart, we will have discounted amount depends on discount
        // Before placing order discount must have been applied to cart.
        BigDecimal lastDiscountAmount = BigDecimal.ZERO;
        if (cart.getDiscount() != null) {
            lastDiscountAmount = cartService.applyDiscountToCartIfApplicableAndCalculateDiscountAmount(cart.getDiscount().getName(), cart);
        }

        // we are calculating te cart total amount to have gross amount
        BigDecimal totalCartAmount = calculateTotalCartAmount(cartItemList);

        Payment payment = new Payment();
        Order order = new Order();

        order.setCart(cart);
        order.setCustomer(customer);
        order.setTotalPrice(totalCartAmount);

        // total price $600
        // after discount = $50 -> $550

        // we are calculating te cart total amount after discount
        order.setPaidPrice(totalCartAmount.subtract(lastDiscountAmount));


        // let's assume if you pay with credit card we deduct 10 $ during the campaign period (Reward!!!)
        // additional discount for specific payment method for credit card
        if (paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            order.setPaidPrice(order.getPaidPrice().subtract(BigDecimal.TEN));
        }

        // initialising payment entity
        // in the recordings, initialising payment was not inserted database directly.
        // But it caused other problems so i decided to insert it into DB first
        // After that we are setting payment value to Order Entity.
        payment.setPaidPrice(order.getPaidPrice());
        payment.setPaymentMethod(paymentMethod);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        // after successful order we have decrease product remaining quantity
        // this stream is subtracting cart item quantity from product remaining quantity
        cartItemList.forEach(cartItem -> {
            cartItem.getProduct().setRemainingQuantity(
                    cartItem.getProduct().getRemainingQuantity() - cartItem.getQuantity());
            cartItemRepository.save(cartItem);
        });

        // stock is 50
        // customer bought 18
        // new stock will be 32
        orderRepository.save(order);
        return order.getPaidPrice();
    }

    private BigDecimal calculateTotalCartAmount(List<CartItem> cartItemList) {
        // this stream basically calculates the cart total amount depends on how many product is added to cart and theirs quantity
        // there is also another same method that calculate cart total amount in CartService we should use it for readability
        // but I paste it here to be able to show you more test cases.
        Function<CartItem, BigDecimal> totalMapper = cartItem -> cartItem.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return cartItemList.stream()
                .map(totalMapper)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}














