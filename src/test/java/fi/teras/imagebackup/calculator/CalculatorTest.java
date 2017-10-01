package fi.teras.imagebackup.calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @Test
    void test_countSuccessPercent_shouldSuccess_oneFailed() {
        assertEquals(new Double(90.0), Calculator.countSuccessPercent(9, 1));
    }

    @Test
    void test_countSuccessPercent_shouldSuccess_allSucceeded() {
        assertEquals(new Double(100.0), Calculator.countSuccessPercent(9, 0));
    }

    @Test
    void test_countSuccessPercent_shouldSuccess_zeroNumbers() {
        assertEquals(new Double(0.0), Calculator.countSuccessPercent(0, 0));
    }

    @Test
    void test_countSuccessPercent_shouldSuccess_onlyFailures() {
        assertEquals(new Double(0.0), Calculator.countSuccessPercent(0, 4));
    }
}
