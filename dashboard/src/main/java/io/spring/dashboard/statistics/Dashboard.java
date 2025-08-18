package io.spring.dashboard.statistics;

import java.util.Locale;
import java.util.Map;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;

import io.spring.dashboard.core.Currencies;
import io.spring.dashboard.order.OrderPaid;
import org.javamoney.moneta.Money;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Dashboard {

	private MonetaryAmount revenue = Money.zero(Currencies.EURO);

	@GetMapping("/statistics")
	Map<String, Object> statistics() {
		MonetaryAmountFormat formatter = MonetaryFormats.getAmountFormat(Locale.US);
		String formattedAmount = formatter.format(revenue);
		return Map.of("revenue", formattedAmount);
	}

	@RabbitListener(queues = "orderpaid", messageConverter = "jsonMessageConverter")
	public void on(OrderPaid event) {
		this.revenue = revenue.add(convertToRevenue(event));
	}

	private MonetaryAmount convertToRevenue(OrderPaid event) {
		return Money.of(Double.valueOf(event.total().substring(3)), Currencies.EURO);
	}
}
