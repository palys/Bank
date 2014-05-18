package bank;

import Bank.IncorrectData;
import Bank.NoSuchAccount;
import Bank.PersonalData;
import Bank.RequestRejected;
import Bank._BankManagerDisp;
import Bank.accountType;
import Ice.Current;
import Ice.StringHolder;

public class BankManagerI extends _BankManagerDisp {
	
	private String generateAccountID(PersonalData data) {
		return data.firstName + data.lastName + data.NationalIDNumber;
	}

	@Override
	public void createAccount(PersonalData data, accountType type,
			StringHolder accountID, Current __current) throws IncorrectData,
			RequestRejected {
		
		
		
	}

	@Override
	public void removeAccount(String accountID, Current __current)
			throws IncorrectData, NoSuchAccount {
		// TODO Auto-generated method stub

	}

}
