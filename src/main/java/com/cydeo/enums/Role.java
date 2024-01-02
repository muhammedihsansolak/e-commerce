package com.cydeo.enums;

public enum Role {
    ADMIN("Admin") , CUSTOMER("Customer");

    final String description;

    Role(String description) {
        this.description = description;
    }
}
