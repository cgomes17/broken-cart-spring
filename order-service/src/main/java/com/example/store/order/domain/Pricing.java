package com.example.store.order.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class Pricing {

    private Pricing() {
    }

    public static BigDecimal total(String customerType, BigDecimal subtotal) {
        BigDecimal total;
        if (customerType.equals("VIP")) {
            total = subtotal.multiply(new BigDecimal("0.90"));
        } else if (customerType.equals("EMPLOYEE")) {
            total = subtotal.multiply(new BigDecimal("0.80"));
        } else {
            total = subtotal;
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
