package bank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Bank.IncorrectAccountNumber;
import Bank.IncorrectAmount;
import Bank.IncorrectData;
import Bank.NoSuchAccount;
import Bank.PersonalData;
import Bank.RequestRejected;
import Bank._BankManagerDisp;
import Bank.accountType;
import Ice.Current;
import Ice.Identity;
import Ice.StringHolder;

public class BankManagerI extends _BankManagerDisp {
	
	private static long numberOfAccounts = 0;
	
	private static final String path = "data";
	
	private static final String silverPath = path + "/silver";
	
	private static final String premiumPath = path + "/premium";
	
	private static final int startingAmount = 10000;
	
	private List<String> silverAccountIDs;
	
	private List<String> premiumAccountsIDs;
	
	private Ice.ObjectAdapter objectAdapter;
	
	private Ice.ServantLocator evictor;
	
	private NewsReceiverI newsReceiver;
	
	public BankManagerI(Ice.ObjectAdapter objectAdapter, Ice.ServantLocator evictor, NewsReceiverI newsReceiver) {
		
		this.objectAdapter = objectAdapter;
		this.evictor = evictor;
		this.newsReceiver = newsReceiver;

		silverAccountIDs = new ArrayList<String>();
		premiumAccountsIDs = new ArrayList<String>();
		
		if (!isInitialized()) {
			try {
				initialize();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		} else {
			readDataOnInit();
		}
	}
	
	public static void main(String[] args) {
		BankManagerI b = new BankManagerI(null, null, new NewsReceiverI());
		System.out.println(b.premiumAccountsIDs.toString());
		try {
			b.createAccount(null, accountType.SILVER, new StringHolder());
		} catch (IncorrectData e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequestRejected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	NewsReceiverI getNewsReceiver() {
		return newsReceiver;
	}
	
	private void readDataOnInit() {
		
		silverAccountIDs.addAll(readAllFileNames(silverPath));
		premiumAccountsIDs.addAll(readAllFileNames(premiumPath));
		
	}
	
	private boolean isID(String s) {
		try {
			Long.getLong(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private List<String> readAllFileNames(String dir) {
		
		File directory = new File(dir);
		List<String> names = new ArrayList<String>();
		
		File[] files = directory.listFiles();
		
		for (File f : files) {
			String name = f.getName();
			
			if (isID(name)) {
				names.add(name);
			}
		}
		
		return names;
		
	}
	
	private boolean isInitialized() {
		
		Path silver = Paths.get(silverPath);
		Path premium = Paths.get(premiumPath);
		
		if (Files.exists(silver) && Files.exists(premium)) {
			if (!Files.isDirectory(silver) || !Files.isDirectory(premium)) {
				return false;
				// TODO
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	private void initialize() throws IOException {
		
		Path p = Paths.get(path);
		
		if (!Files.exists(p)) {
			Files.createDirectory(p);
		}

		Path silver = Paths.get(silverPath);
		Path premium = Paths.get(premiumPath);

		if (!Files.exists(silver)) {
			Files.createDirectory(silver);
		}

		if (!Files.exists(premium)) {
			Files.createDirectory(premium);
		}
	}
	
	private String generateAccountID() {
		
		
		String id = Long.toString(numberOfAccounts);
		while (silverAccountIDs.contains(id) || premiumAccountsIDs.contains(id)) {
			numberOfAccounts++;
			id = Long.toString(numberOfAccounts);
		}
		return id;
	}
	
	private boolean isNameCorrect(String name) {
		return true; // TODO
	}
	
	private boolean isNationalIDNumberCorrect(String idNumber) {
		return idNumber.matches("\\d*");
	}
	
	private boolean isDataCorrect(PersonalData data) {
		
		return isNameCorrect(data.firstName) && isNameCorrect(data.lastName) && isNationalIDNumberCorrect(data.NationalIDNumber);
	}
	
	boolean exists(String accountID) {
		return silverAccountIDs.contains(accountID) || premiumAccountsIDs.contains(accountID);
	}
	
	private String getPath(String accountID) {
		
		accountType type = null;
		try {
			type = getType(accountID);
		} catch (NoSuchAccount e) {
			e.printStackTrace();
		}
		String path = null;
		
		if (type == accountType.PREMIUM) {
			path = premiumPath + "/" + accountID;
		} else {
			path = silverPath + "/" + accountID;
		}
		
		return path;
	}
	
	int getBalance(String accountID) throws NumberFormatException, IOException {
		String path = getPath(accountID);
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		int balance = Integer.parseInt(reader.readLine());
		reader.close();
		return balance;
	}
	
	private void alterAmount(String accountID, int newAmount) throws IOException {
		FileWriter writer = new FileWriter(new File(getPath(accountID)));
		writer.write(Integer.toString(newAmount));
		writer.close();
	}
	
	void doTransfer(String fromID, String toID, int amount, Current __current) throws IncorrectAccountNumber, IncorrectAmount {
		
		int fromBalance = 0;
		try {
			fromBalance = getBalance(fromID);
		} catch (Exception e) {
			throw new IncorrectAccountNumber();
		}
		
		if (fromBalance > amount) {
			throw new IncorrectAmount();
		}
		
		int newFromBalance = fromBalance - amount;
		int newToBalance = 0;
		try {
			newToBalance = getBalance(toID) + amount;
		} catch (Exception e) {
			throw new IncorrectAccountNumber();
		}
		
		try {
			alterAmount(fromID, newFromBalance);
			alterAmount(toID, newToBalance);
		} catch (Exception e) {
			throw new IncorrectAccountNumber();
		}
		
	}
	
	accountType getType(String accountID) throws NoSuchAccount {
		if (premiumAccountsIDs.contains(accountID)) {
			return accountType.PREMIUM;
		} else if (silverAccountIDs.contains(accountID)) {
			return accountType.SILVER;
		}
		
		throw new NoSuchAccount();
	}

	@Override
	public void createAccount(PersonalData data, accountType type,
			StringHolder accountID, Current __current) throws IncorrectData,
			RequestRejected {
		
		if (!isDataCorrect(data)) {
			throw new IncorrectData();
		}
		
		String id = generateAccountID();
		String path = null;
		
		if (type == accountType.SILVER) {
			path = silverPath + "/" + id;
		} else {
			path = premiumPath + "/" + id;
			Ice.Object servant = new PremiumAccountI(startingAmount, id, this);
			objectAdapter.add(servant, new Identity("premium", id));
		}
		
		File accountFile = new File(path);
		try {
			accountFile.createNewFile();
			FileWriter writer = new FileWriter(accountFile);
			writer.write(Integer.toString(startingAmount));
			writer.close();
		} catch (IOException e) {
			throw new RequestRejected(e);
		}
		
		
		accountID.value = id;
		System.out.println("accountId.value set to " + id);
	}

	@Override
	public void removeAccount(String accountID, Current __current)
			throws IncorrectData, NoSuchAccount {
		
		if (!exists(accountID)) {
			throw new NoSuchAccount();
		}
		
		accountType type = getType(accountID);
		String accountPath = getPath(accountID);
		
		if (type == accountType.PREMIUM) {
			premiumAccountsIDs.remove(accountID);
			objectAdapter.remove(new Identity("premium", accountID));
		} else {
			silverAccountIDs.remove(accountID);
		}
		
		new File(accountPath).delete();
		
	}

}
