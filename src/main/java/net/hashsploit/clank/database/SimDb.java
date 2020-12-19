package net.hashsploit.clank.database;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SimDb implements IDatabase {
	
	public static final Logger logger = Logger.getLogger(SimDb.class.getName());
	
	private static final ArrayList<Triplet<Integer, String, String>> users = new ArrayList<Triplet<Integer, String, String>>();

	public class Triplet<T, U, V> {
	    private final T first;
	    private final U second;
	    private final V third;
	    public Triplet(T first, U second, V third) {
	        this.first = first;
	        this.second = second;
	        this.third = third;
	    }
	    public T getFirst() { return first; }
	    public U getSecond() { return second; }
	    public V getThird() { return third; }
	}

	public SimDb() {
		users.add(new Triplet(50, "Smily", "11111111111111111111111111111111"));
		users.add(new Triplet(51, "hashsploit", "22222222222222222222222222222222"));
		users.add(new Triplet(52, "FourBolt", "33333333333333333333333333333333"));
		users.add(new Triplet(53, "aequs", "44444444444444444444444444444444"));
		users.add(new Triplet(54, "trop", "55555555555555555555555555555555"));
		users.add(new Triplet(55, "Badger41", "66666666666666666666666666666666"));
		users.add(new Triplet(56, "Dnawrkshp", "77777777777777777777777777777777"));
		
		logger.info("Simulated DB initialized ...");
	}

	@Override
	public boolean accountExists(String username) {
		logger.info(String.format("Checking if account with the username '%s' exists ...", username));
		return true;
	}

	@Override
	public boolean validateAccount(String username, String password) {
		logger.info(String.format("Validating account '%s' ...", username));
		return true;
	}
	
	@Override
	public int getAccountId(String username) {
		for (Triplet user: users) {
			if (username.toLowerCase().equals(((String) user.getSecond()).toLowerCase())) {
				return (int) user.getFirst();
			}
		}
		return 0;
	}
	
	@Override
	public String getMlsToken(Integer accountId) {
		for (Triplet user: users) {
			if (accountId == (int) user.getFirst()) {
				return (String) user.getThird();
			}
		}
		return null;
	}
	
	@Override
	public int getAccountIdFromMlsToken(String mlsToken) {
		for (Triplet user: users) {
			if (mlsToken.equals((String) user.getThird())) {
				return (int) user.getFirst();
			}
		}
		return -1;
	}

	@Override
	public String getUsername(int accountId) {
		for (Triplet user: users) {
			if (accountId == (int) user.getFirst()) {
				return (String) user.getSecond();
			}
		}
		return null;
	}

	
}
