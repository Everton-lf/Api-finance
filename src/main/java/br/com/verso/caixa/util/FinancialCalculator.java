package br.com.verso.caixa.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FinancialCalculator {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    public static BigDecimal setScale(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE);
        }
        return value.setScale(SCALE, ROUNDING_MODE);
    }

    public static BigDecimal calculateMonthlyInterest(BigDecimal balance, BigDecimal monthlyRate) {
        if (balance == null || monthlyRate == null) {
            return BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE);
        }
        return balance.multiply(monthlyRate)
                .divide(BigDecimal.valueOf(100), SCALE, ROUNDING_MODE);
    }

    public static BigDecimal addValues(BigDecimal value1, BigDecimal value2) {
        BigDecimal v1 = value1 != null ? value1 : BigDecimal.ZERO;
        BigDecimal v2 = value2 != null ? value2 : BigDecimal.ZERO;
        return v1.add(v2).setScale(SCALE, ROUNDING_MODE);
    }
}