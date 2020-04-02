package group6.util;

import group6.model.FlightDescriptor;
import group6.model.Itinerary;
import group6.model.PassengerDetails;
import group6.model.PassengerList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This class is used to for any random generation of values and objects
 *
 */
public class RNG {

	/**
	 * Instance of SecureRandom
	 */
	private static SecureRandom rng = new SecureRandom();

	/**
	 * Static method that generates a flight descriptor
	 * 
	 * @return
	 */
	public static FlightDescriptor generateFlightDescriptor() {
		// All interfaces are initialised.
		// Lets simulate adding a flight
		Itinerary itinerary = generateItinerary();
		// will follow the IATA standard
		String flightCode = getFlightCode(itinerary.getFrom());
		// list of passengers
		PassengerList passengerList = generatePassengers();
		FlightDescriptor flightDescriptor = new FlightDescriptor(flightCode, itinerary, passengerList);

		return flightDescriptor;
	}

	/**
	 * Matches a flight's origin to an operator that flies from that airport
	 * 
	 * @param from
	 * @return
	 */
	private static String getFlightCode(String from) {

		Map<String, String> lookup = new Hashtable<String, String>();
		lookup.put("ATL", "AT00");
		lookup.put("PEK", "PK00");
		lookup.put("LAX", "AA00");
		lookup.put("HND", "HN00");
		lookup.put("LHR", "BA00");

		// return flight code plus a random number
		// reduces likelyhood of duplicate entries such as BA001
		return lookup.get(from) + (rng.nextInt(9) + 1);
	}

	/**
	 * Static method that generates an itinerary
	 * 
	 * @return
	 */
	private static Itinerary generateItinerary() {
		String[] from = new String[] { "ATL", "PEK", "LAX", "HND", "LHR" };
		String[] to = new String[] { "FRA", "SIN", "MAD", "SFO", "YYZ" };
		String[] next = new String[] { "DME", "SVX", "LCY", "BBU", "FCO" };
		Itinerary itinerary = new Itinerary(from[rng.nextInt(5)], to[rng.nextInt(5)], next[rng.nextInt(5)]);

		return itinerary;
	}

	/**
	 * Generates passengers from a list of 50 names
	 * 
	 * @return
	 */
	private static PassengerList generatePassengers() {
		BufferedReader reader;
		List<String> temps = new ArrayList<String>();
		PassengerList passengerList = new PassengerList();

		try {

			reader = new BufferedReader(new FileReader("resource/Names.txt"));

			String line = reader.readLine();

			while (line != null) {
				temps.add(line);
				line = reader.readLine();
			}
			reader.close();

			int numOfPassenger = rng.nextInt((20 - 5) + 1) + 5;
			for (int i = 0; i < numOfPassenger; i++) {
				int bound = rng.nextInt((49 - 1) + 1) + 1;
				passengerList.addPassenger(new PassengerDetails(temps.get(bound)));
			}

			return passengerList;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return passengerList;

	}

	/**
	 * Randomly generates a number between 1 and 100
	 * 
	 * @return
	 */
	public static int generateRandomStatus() {
		int ran = (rng.nextInt(100) + 1);

		return ran;
	}
}
