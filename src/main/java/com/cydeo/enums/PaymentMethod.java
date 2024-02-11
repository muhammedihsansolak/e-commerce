package com.cydeo.enums;

public enum PaymentMethod {
    CREDIT_CARD("Credit card"), TRANSFER("Transfer"), BUY_NOW_PAY_LATER("Buy now pay later"), BALANCE("Balance");

    final String paymentMethod;

    PaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
