package com.cydeo.service.Impl;

import com.cydeo.entity.Balance;
import com.cydeo.repository.BalanceRepository;
import com.cydeo.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    @Override
    public void decreaseCustomerBalance(Long customerId, BigDecimal paidPrice) {

        Balance balance = balanceRepository.findByUser_Id(customerId);

        balance.setAmount( balance.getAmount().subtract(paidPrice) );

        balanceRepository.save(balance);

    }

}
