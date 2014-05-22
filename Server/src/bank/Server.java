package bank;

import Ice.Identity;

public class Server {
	
	public void t1(String[] args) {
		
		int status = 0;
		Ice.Communicator ic = null;
		Ice.ObjectAdapter objectAdapter = null;
		
		try {
			
			ic = Ice.Util.initialize(args);
			objectAdapter = ic.createObjectAdapter("Bank");
			
			NewsReceiverI newsReceiver = new NewsReceiverI();
			AccountEvictor2 silverAccountEvictor = new AccountEvictor2(2);
			BankManagerI bankManager = new BankManagerI(objectAdapter, silverAccountEvictor, newsReceiver);
			silverAccountEvictor.setBankManager(bankManager);
			Ice.Object manager = bankManager;
			
			objectAdapter.add(manager, new Identity("1", "manager"));
			objectAdapter.addServantLocator(silverAccountEvictor, "silver");
			
			// TODO set callback news receiver
			
			objectAdapter.activate();
			
			ic.waitForShutdown();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			status = 1;
			
		} finally {
			
			if (ic != null) {
				
				try {
					ic.destroy();
				} catch (Exception e) {
					e.printStackTrace();
					status = 1;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
		Server server = new Server();
		server.t1(args);
	}
}
