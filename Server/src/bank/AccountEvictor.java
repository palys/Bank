package bank;

import java.io.IOException;

import Bank.accountType;
import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.ServantLocator;
import Ice.UserException;

public class AccountEvictor extends Evictor.EvictorBase {
	
	private BankManagerI bankManager;
	
	AccountEvictor(int pool) {
		super(pool);
	}
	
	void setBankManager(BankManagerI bankManager) {
		this.bankManager = bankManager;
	}

	@Override
	public Object add(Current c, LocalObjectHolder cookie) {
		
		String accountID = c.id.name;
		try {
			int amount = bankManager.getBalance(accountID);
			
			if (bankManager.getType(accountID) != accountType.SILVER) {
				return null;
			}
			
			return new AccountI(amount, accountID, bankManager);

		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public void evict(Object servant, java.lang.Object cookie) {
		// TODO Auto-generated method stub
		
	}

	

}
