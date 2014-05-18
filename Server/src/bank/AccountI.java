package bank;

import Bank.IncorrectAccountNumber;
import Bank.IncorrectAmount;
import Bank._AccountDisp;
import Ice.Current;

public class AccountI extends _AccountDisp {

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
