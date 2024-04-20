
package acme.utils;

import java.util.Collection;

import acme.client.data.datatypes.Money;

public class MoneyUtils {

	public static Collection<Money> exchangeCollectionToCurrency(final Collection<Money> collection, final String currency) {
		//TODO hacer intercambio de monedas
		Collection<Money> result;

		result = collection;
		return result;
	}

	public static Money getAvg(final Collection<Money> collection, final String systemCurrency) {

		Money result;
		Collection<Money> exchangedCollection;

		exchangedCollection = MoneyUtils.exchangeCollectionToCurrency(collection, systemCurrency);

		result = new Money();
		result.setCurrency(systemCurrency);
		result.setAmount(exchangedCollection.stream().mapToDouble(Money::getAmount).average().getAsDouble());

		return result;
	}

	public static Money getStd(final Collection<Money> collection, final String systemCurrency) {

		Money result;
		Collection<Money> exchangedCollection;

		exchangedCollection = MoneyUtils.exchangeCollectionToCurrency(collection, systemCurrency);

		result = new Money();
		result.setCurrency(systemCurrency);

		Integer n = exchangedCollection.size();
		Double media = exchangedCollection.stream().mapToDouble(Money::getAmount).average().getAsDouble();

		Double varianza = exchangedCollection.stream().mapToDouble(Money::getAmount).map(x -> Math.pow(x - media, 2) / n).sum();

		result.setAmount(Math.sqrt(varianza));

		return result;
	}

	public static Money getMin(final Collection<Money> collection, final String systemCurrency) {

		Money result;
		Collection<Money> exchangedCollection;

		exchangedCollection = MoneyUtils.exchangeCollectionToCurrency(collection, systemCurrency);

		result = new Money();
		result.setCurrency(systemCurrency);
		result.setAmount(exchangedCollection.stream().mapToDouble(Money::getAmount).min().orElse(0.));

		return result;
	}

	public static Money getMax(final Collection<Money> collection, final String systemCurrency) {

		Money result;
		Collection<Money> exchangedCollection;

		exchangedCollection = MoneyUtils.exchangeCollectionToCurrency(collection, systemCurrency);

		result = new Money();
		result.setCurrency(systemCurrency);
		result.setAmount(exchangedCollection.stream().mapToDouble(Money::getAmount).max().orElse(0.));

		return result;
	}

}
