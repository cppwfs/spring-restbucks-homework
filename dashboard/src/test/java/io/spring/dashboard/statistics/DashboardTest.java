package io.spring.dashboard.statistics;

import io.spring.dashboard.order.OrderPaid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DashboardTest {

    private Dashboard dashboard;

    @BeforeEach
    void setUp() {
        dashboard = new Dashboard();
    }

    @Test
    void statistics_shouldReturnInitialZeroRevenue() {
        // When
        Map<String, Object> statistics = dashboard.statistics();

        // Then
        assertNotNull(statistics);
        assertTrue(statistics.containsKey("revenue"));
        assertEquals("EUR 0.00", statistics.get("revenue"));
    }

    @Test
    void on_shouldAddOrderPaidAmountToRevenue() {
        // Given
        OrderPaid orderPaid = new OrderPaid("order-123", "EUR25.50");

        // When
        dashboard.on(orderPaid);

        // Then
        Map<String, Object> statistics = dashboard.statistics();
        assertEquals("EUR 25.50", statistics.get("revenue"));
    }

    @Test
    void on_shouldAccumulateMultipleOrderPaidAmounts() {
        // Given
        OrderPaid firstOrder = new OrderPaid("order-123", "EUR15.75");
        OrderPaid secondOrder = new OrderPaid("order-456", "EUR32.25");

        // When
        dashboard.on(firstOrder);
        dashboard.on(secondOrder);

        // Then
        Map<String, Object> statistics = dashboard.statistics();
        assertEquals("EUR 48.00", statistics.get("revenue"));
    }

    @Test
    void on_shouldHandleDecimalAmounts() {
        // Given
        OrderPaid orderPaid = new OrderPaid("order-789", "EUR123.99");

        // When
        dashboard.on(orderPaid);

        // Then
        Map<String, Object> statistics = dashboard.statistics();
        assertEquals("EUR 123.99", statistics.get("revenue"));
    }

    @Test
    void on_shouldHandleZeroAmount() {
        // Given
        OrderPaid orderPaid = new OrderPaid("order-000", "EUR0.00");

        // When
        dashboard.on(orderPaid);

        // Then
        Map<String, Object> statistics = dashboard.statistics();
        assertEquals("EUR 0.00", statistics.get("revenue"));
    }

    @Test
    void statistics_shouldFormatAmountInUSLocale() {
        // Given - set up some revenue
        OrderPaid orderPaid = new OrderPaid("order-123", "EUR1234.56");
        dashboard.on(orderPaid);

        // When
        Map<String, Object> statistics = dashboard.statistics();

        // Then
        assertEquals("EUR 1,234.56", statistics.get("revenue"));
    }

    @Test
    void statistics_shouldReturnMapWithRevenueKey() {
        // When
        Map<String, Object> statistics = dashboard.statistics();

        // Then
        assertNotNull(statistics);
        assertEquals(1, statistics.size());
        assertTrue(statistics.containsKey("revenue"));
    }

    @Test
    void on_shouldHandleMultipleOrdersWithSameIdentifier() {
        // Given
        OrderPaid firstOrder = new OrderPaid("order-123", "EUR10.00");
        OrderPaid secondOrder = new OrderPaid("order-123", "EUR20.00");

        // When
        dashboard.on(firstOrder);
        dashboard.on(secondOrder);

        // Then
        Map<String, Object> statistics = dashboard.statistics();
        assertEquals("EUR 30.00", statistics.get("revenue"));
    }
}