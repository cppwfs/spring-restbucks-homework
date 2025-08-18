package io.spring.dashboard.order;

public record OrderPaid(
		String orderIdentifier,
		String total)  {}
