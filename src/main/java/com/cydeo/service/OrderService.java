package com.cydeo.service;

import com.cydeo.dto.OrderDTO;
import com.cydeo.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDTO> getAll();

    OrderDTO updateOrder(Long orderId,OrderDTO orderDTO);

    OrderDTO create(OrderDTO orderDTO);

    List<OrderDTO> getOrdersByPaymentMethod(PaymentMethod paymentMethod);

    List<OrderDTO> getOrdersByEmail(String email);

    OrderDTO findById(Long id, String currency);

    OrderDTO findByIdAndCurrency(Long id, Optional<String> currency);

    BigDecimal placeOrder(String paymentMethod, String discountName);
}
