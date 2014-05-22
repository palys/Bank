package bank;

import Bank.IncorrectAccountNumber;
import Bank.IncorrectAmount;
import Bank.IncorrectData;
import Bank._PremiumAccountDisp;
import Bank.currency;
import FinancialNews.Currency;
import FinancialNews.CurrencyHolder;
import Ice.Current;
import Ice.FloatHolder;
import Ice.IntHolder;

public class PremiumAccountI extends _PremiumAccountDisp {
	
	private int balance;
	
	private String accountNumber;
	
	private BankManagerI bankManager;
	
	PremiumAccountI(int balance, String accountNumber, BankManagerI bankManager) {
		this.balance = balance;
		this.accountNumber = accountNumber;
		this.bankManager = bankManager;
	}
	
	private Currency convert(currency curr) {
		return Currency.valueOf(curr.value());
	}

	@Override
	public void calculateLoan(int amount, currency curr, int period,
			IntHolder totalCost, FloatHolder interestRate, Current __current)
			throws IncorrectData {
		
		NewsReceiverI loanData = this.bankManager.getNewsReceiver();
		
		interestRate.value = loanData.getInterestRate(convert(curr));
		totalCost.value = (int) (loanData.getExchangeRate(Currency.PLN, convert(curr)) * period * amount + amount);

	}
	
	private void refreshData() {
		try {
			this.balance = this.bankManager.getBalance(this.accountNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getBalance(Current __current) {
		
		refreshData();
		return balance;
	}

	@Override
	public String getAccountNumber(Current __current) {
		
		return accountNumber;
	}

	@Override
	public void transfer(String accountNumber, int amount, Current __current)
			throws IncorrectAccountNumber, IncorrectAmount {
		
		bankManager.doTransfer(this.accountNumber, accountNumber, amount, __current);

	}

}
