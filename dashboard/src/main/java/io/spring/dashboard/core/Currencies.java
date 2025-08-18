package io.spring.dashboard.core;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

/**
 * Interface to contain {@link CurrencyUnit} constants.
 *
 * @author Oliver Gierke
 */
public interface Currencies {

	public static final CurrencyUnit EURO = Monetary.getCurrency("EUR");
}
