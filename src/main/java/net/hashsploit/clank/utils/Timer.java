package net.hashsploit.clank.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InvalidNameException;

public class Timer {
	
	private static Map<String, Profile> profiles = new HashMap<String, Profile>();
	private static String currentKey;
	
	// Prevent instantiation
	private Timer() {}
	
	/**
	 * Start a timer with this profile name, automatically ends previous profile timer
	 * @param key
	 */
	public static synchronized void time(String key) {
		if (currentKey != null && currentKey.equals(key)) {
			throw new IllegalStateException("Cannot start timer '" + key + "' as it has already been started.");
		}
		
		if (currentKey != null) {
			profiles.get(currentKey).stop();
		}
		
		currentKey = key;
		profiles.put(currentKey, new Profile(currentKey));
	}
	
	/**
	 * Get the total time of a profile with the name key
	 * @param key
	 * @return
	 * @throws InvalidNameException
	 */
	public static long getTime(String key) throws InvalidNameException {
		if (!profiles.containsKey(key)) {
			throw new InvalidNameException("Key '" + key + "' not found in timer.");
		}
		return profiles.get(key).getDelta();
	}
	
	/**
	 * Get the total time in milliseconds of the profile named key
	 * @param key
	 * @return
	 * @throws InvalidNameException
	 */
	public static float getTimeInMillis(String key) throws InvalidNameException {
		return (float) (getTime(key) / 1e6);
	}
	
	/**
	 * Get the start-time of the profile named key
	 * @param key
	 * @return
	 * @throws InvalidNameException
	 */
	public static long getStartTime(String key) throws InvalidNameException {
		if (!profiles.containsKey(key)) {
			throw new InvalidNameException("Key '" + key + "' not found in timer.");
		}
		return profiles.get(key).getStartTime();
	}
	
	/**
	 * Get the end-time of the profile named key
	 * @param key
	 * @return
	 * @throws InvalidNameException
	 */
	public static long getEndTime(String key) throws InvalidNameException {
		if (!profiles.containsKey(key)) {
			throw new InvalidNameException("Key '" + key + "' not found in timer.");
		}
		return profiles.get(key).getEndTime();
	}
	
	/**
	 * Get all the profiles sorted by start time 
	 * @return
	 */
	public static List<Profile> getProfiles() {
		List<Profile> sortedProfiles = new ArrayList<Profile>();
		for (Profile p : profiles.values()) {
			sortedProfiles.add(p);
		}
		Profile.setSortType(Profile.SORT_BY_START_TIME);
		Collections.sort(sortedProfiles);
		return sortedProfiles;
	}
	
	/**
	 * Get all the profiles sorted by max delta first
	 * @return
	 */
	public static List<Profile> getProfilesByDelta() {
		List<Profile> sortedProfiles = new ArrayList<Profile>();
		for (Profile p : sortedProfiles) {
			sortedProfiles.add(p);
		}
		Profile.setSortType(Profile.SORT_BY_DELTA);
		Collections.sort(sortedProfiles);
		return sortedProfiles;
	}
	
	/**
	 * Get all the profiles sorted by end time
	 * @return
	 */
	public static List<Profile> getProfilesByEndTime() {
		List<Profile> sortedProfiles = new ArrayList<Profile>();
		for (Profile p : sortedProfiles) {
			sortedProfiles.add(p);
		}
		Profile.setSortType(Profile.SORT_BY_END_TIME);
		Collections.sort(sortedProfiles);
		
		return sortedProfiles;
	}
	
	/**
	 * Representation of a timer profile
	 */
	public static class Profile implements Comparable<Profile> {
		
		public static final int SORT_BY_START_TIME = 0;
		public static final int SORT_BY_END_TIME = 1;
		public static final int SORT_BY_DELTA = 2;
		private static int sortType = SORT_BY_START_TIME;
		
		private final String name;
		private long startTime;
		private long endTime;
		
		/**
		 * Create a new timer profile that will be identified by name
		 * @param name
		 */
		public Profile(String name) {
			this.name = name;
			startTime = System.nanoTime();
			endTime = -1;
		}
		
		/**
		 * Set the comparator's sort type.
		 * Can be set to the values of SORT_BY_START_TIME, SORT_BY_END_TIME, or SORT_BY_DELTA.
		 * @param sortBy
		 */
		public static void setSortType(int sortBy) {
			sortType = sortBy;
		}
		
		/**
		 * Get the comparator's current sort type
		 * @return
		 */
		public static int getSortType() {
			return sortType;
		}
		
		/**
		 * Get the profile name
		 * @return
		 */
		public final String getName() {
			return name;
		}
		
		/**
		 * Stop the profile timer
		 */
		public final void stop() {
			endTime = System.nanoTime();
		}
		
		/**
		 * Get the profile's delta time, returns -1 if stop was never called
		 * @return
		 */
		public final long getDelta() {
			return (endTime == -1 ? -1 : endTime - startTime);
		}
		
		/**
		 * Get the starting time of this profile
		 * @return
		 */
		public final long getStartTime() {
			return startTime;
		}
		
		/**
		 * Get the ending time of this profile, returns -1 if stop was never called
		 * @return
		 */
		public final long getEndTime() {
			return endTime;
		}
		
		@Override
		public int compareTo(Profile other) {
			if (sortType == SORT_BY_START_TIME) {
				return Long.compare(startTime, other.startTime);
			} else if (sortType == SORT_BY_END_TIME) {
				return Long.compare(endTime, other.endTime);
			} else if (sortType == SORT_BY_DELTA){
				return Long.compare(getDelta(), other.getDelta());
			}
			return 0;
		}
	}
	
}
