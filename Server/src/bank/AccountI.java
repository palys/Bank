package bank;

import java.io.IOException;

import Bank.IncorrectAccountNumber;
import Bank.IncorrectAmount;
import Bank._AccountDisp;
import Ice.Current;

public class AccountI extends _AccountDisp {
	
	private int balance;
	
	private String accountNumber;
	
	private BankManagerI bankManager;
	
	AccountI(int balance, String accountNumber, BankManagerI bankManager) {
		this.balance = balance;
		this.accountNumber = accountNumber;
		this.bankManager = bankManager;
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
