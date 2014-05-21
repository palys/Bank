package bank;

import Bank.BankManagerPrx;
import Bank.BankManagerPrxHelper;
import Bank.PersonalData;
import Bank.PremiumAccountPrx;
import Bank.PremiumAccountPrxHelper;
import Bank.accountType;

public class Client {

	public static void main(String[] args) {
		int status = 0;
		Ice.Communicator ic = null;
		
		try {
			ic = Ice.Util.initialize(args);
			
			Ice.ObjectPrx managerBase = ic.propertyToProxy("SR.Manager");
			BankManagerPrx manager = BankManagerPrxHelper.checkedCast(managerBase);
			Ice.StringHolder idHolder = new Ice.StringHolder();
			manager.createAccount(new PersonalData("Tomasz", "Palys", "1234"), accountType.PREMIUM, idHolder);
			
			System.out.println(idHolder.value);
			
			Ice.ObjectPrx accountBase = ic.stringToProxy("premium" + idHolder.value + "::tcp -p 10000:ssl -p 12001:udp -p 10000");
			PremiumAccountPrx account = PremiumAccountPrxHelper.checkedCast(accountBase);
			
			account.transfer("1", 2000);
			System.out.println(account.getBalance());
			System.out.println(account.getAccountNumber());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
