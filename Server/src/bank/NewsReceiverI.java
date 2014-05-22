package bank;

import java.util.HashMap;
import java.util.Map;

import FinancialNews.Currency;
import FinancialNews._NewsReceiverDisp;
import Ice.Current;

public class NewsReceiverI extends _NewsReceiverDisp {
	
	static class Pair<U, V> {
		public final U u;
		public final V v;
		
		public Pair(U u, V v) {
			this.u = u;
			this.v = v;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((u == null) ? 0 : u.hashCode());
			result = prime * result + ((v == null) ? 0 : v.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pair other = (Pair) obj;
			if (u == null) {
				if (other.u != null)
					return false;
			} else if (!u.equals(other.u))
				return false;
			if (v == null) {
				if (other.v != null)
					return false;
			} else if (!v.equals(other.v))
				return false;
			return true;
		}
		
		
	}
	
	private Map<Currency, Float> interestRateMap;
	
	private Map<Pair<Currency, Currency>, Float> exchangeRateMap;
	
	NewsReceiverI() {
		interestRateMap = new HashMap<Currency, Float>();
		exchangeRateMap = new HashMap<NewsReceiverI.Pair<Currency,Currency>, Float>();
	}

	@Override
	public void exchangeRate(Currency curr1, Currency curr2, float rate,
			Current __current) {
		exchangeRateMap.put(new Pair<Currency, Currency>(curr1, curr2), rate);

	}

	@Override
	public void interestRate(Currency curr, float rate, Current __current) {
		
		interestRateMap.put(curr, rate);

	}
	
	float getInterestRate(Currency curr) {
		return interestRateMap.get(curr);
	}
	
	float getExchangeRate(Currency curr1, Currency curr2) {
		return exchangeRateMap.get(new Pair<Currency, Currency>(curr1, curr2));
	}

}
