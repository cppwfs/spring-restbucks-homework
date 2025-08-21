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
	private final StatisticsConverter statisticsConverter;
	private final MonetaryAmountFormat compactFormat = MonetaryFormats.getAmountFormat(Locale.ROOT);

	public Dashboard(StatisticsConverter statisticsConverter) {
		this.statisticsConverter = statisticsConverter;
	}

	@GetMapping("/statistics")
	Map<String, Object> statistics() {
		String formattedAmount = statisticsConverter.formatAmount(revenue);
		return Map.of("Dashboard", formattedAmount);
	}

	@RabbitListener(queues = "orderpaid", messageConverter = "jsonMessageConverter")
	public void on(OrderPaid event) {
		this.revenue = revenue.add(Money.parse(event.total(), compactFormat));
	}
}
