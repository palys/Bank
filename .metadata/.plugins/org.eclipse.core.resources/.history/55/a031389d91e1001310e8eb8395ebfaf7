package bank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Ice.Current;
import Ice.LocalObjectHolder;
import Ice.Object;
import Ice.ServantLocator;
import Ice.UserException;

public class AccountEvictor2 implements ServantLocator {
	
	private int maxSize;
	
	private List<AccountI> servants;
	
	private BankManagerI manager;
	
	public AccountEvictor2(int size) {
		this.maxSize = size;
		this.servants = new LinkedList<AccountI>();
	}
	
	void setBankManager(BankManagerI manager) {
		this.manager = manager;
	}
	
	private AccountI getFromCache(String id) {
		for (AccountI account : servants) {
			if (account.getAccountNumber().equals(id)) {
				return account;
			}
		}
		return null;
	}

	@Override
	public synchronized Object locate(Current curr, LocalObjectHolder cookie)
			throws UserException {
		String id = curr.id.name;
		
		AccountI acc = getFromCache(id);
		
		if (acc != null) {
			servants.remove(acc);
			return acc;
		} else {
			try {
				acc = new AccountI(manager.getBalance(id), id, manager);
				if (servants.size() < maxSize) {
					servants.add(acc);
				} else {
					servants.remove(servants.size() - 1);
					servants.add(acc);
				}
				return acc;
			} catch (Exception e) {
				return null;
			}
		}
	}

	@Override
	public synchronized void finished(Current curr, Object servant, java.lang.Object cookie)
			throws UserException {
		
		if (servants.size() < maxSize) {
			servants.add((AccountI) servant);
		} else {
			servants.remove(servants.size() - 1);
			servants.add((AccountI) servant);
		}
	}

	@Override
	public synchronized void deactivate(String category) {
		// TODO Auto-generated method stub

	}

}
