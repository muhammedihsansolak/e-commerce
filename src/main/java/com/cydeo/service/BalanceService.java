package com.cydeo.service;

import java.math.BigDecimal;

public interface BalanceService {
    void decreaseCustomerBalance(Long customerId, BigDecimal paidPrice);
}
