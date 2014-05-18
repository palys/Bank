package bank;

import Bank.IncorrectAccountNumber;
import Bank.IncorrectAmount;
import Bank.IncorrectData;
import Bank._PremiumAccountDisp;
import Bank.currency;
import Ice.Current;
import Ice.FloatHolder;
import Ice.IntHolder;

public class PremiumAccountI extends _PremiumAccountDisp {

	@Override
	public void calculateLoan(int amount, currency curr, int period,
			IntHolder totalCost, FloatHolder interestRate, Current __current)
			throws IncorrectData {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBalance(Current __current) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAccountNumber(Current __current) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void transfer(String accountNumber, int amount, Current __current)
			throws IncorrectAccountNumber, IncorrectAmount {
		// TODO Auto-generated method stub

	}

}
