package br.com.verso.caixa.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FinancialCalculator Tests")
class FinancialCalculatorTest {

    @Test
    @DisplayName("Should calculate monthly interest correctly")
    void shouldCalculateMonthlyInterestCorrectly() {
        BigDecimal balance = new BigDecimal("1000.00");
        BigDecimal rate = new BigDecimal("1.5");

        BigDecimal interest = FinancialCalculator.calculateMonthlyInterest(balance, rate);

        assertEquals(new BigDecimal("15.00"), interest);
    }

    @Test
    @DisplayName("Should add values correctly with proper scaling")
    void shouldAddValuesCorrectly() {
        BigDecimal value1 = new BigDecimal("1000.00");
        BigDecimal value2 = new BigDecimal("15.00");

        BigDecimal result = FinancialCalculator.addValues(value1, value2);

        assertEquals(new BigDecimal("1015.00"), result);
    }

    @Test
    @DisplayName("Should set scale to 2 decimal places")
    void shouldSetScaleCorrectly() {
        BigDecimal value = new BigDecimal("1000.555");

        BigDecimal result = FinancialCalculator.setScale(value);

        assertEquals(2, result.scale());
        assertEquals(new BigDecimal("1000.56"), result);
    }

    @Test
    @DisplayName("Should calculate interest with high rate")
    void shouldCalculateInterestWithHighRate() {
        BigDecimal balance = new BigDecimal("1000.00");
        BigDecimal rate = new BigDecimal("10.00");

        BigDecimal interest = FinancialCalculator.calculateMonthlyInterest(balance, rate);

        assertEquals(new BigDecimal("100.00"), interest);
    }

    @Test
    @DisplayName("Should calculate interest with low rate")
    void shouldCalculateInterestWithLowRate() {
        BigDecimal balance = new BigDecimal("1000.00");
        BigDecimal rate = new BigDecimal("0.50");

        BigDecimal interest = FinancialCalculator.calculateMonthlyInterest(balance, rate);

        assertEquals(new BigDecimal("5.00"), interest);
    }

    @Test
    @DisplayName("Should handle very small values")
    void shouldHandleVerySmallValues() {
        BigDecimal balance = new BigDecimal("0.01");
        BigDecimal rate = new BigDecimal("1.5");

        BigDecimal interest = FinancialCalculator.calculateMonthlyInterest(balance, rate);

        assertEquals(new BigDecimal("0.00"), interest);
    }
}

