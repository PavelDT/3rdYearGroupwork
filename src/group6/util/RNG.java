package group6.util;

import group6.controller.AircraftManagementDatabase;
import group6.model.FlightDescriptor;
import group6.model.Itinerary;
import group6.model.PassengerDetails;
import group6.model.PassengerList;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class RNG {

    private static SecureRandom rng = new SecureRandom();

    public static FlightDescriptor generateFlightDescriptor() {
        // All interfaces are initialised.
        // Lets simulate adding a flight

        Itinerary itinerary = generateItinerary();
        // will follow the IATA standard
        String flightCode = getFlightCode(itinerary.getFrom());
        // list of passengers
        PassengerList passengerList = generatePassengers();
        FlightDescriptor flightDescriptor = new FlightDescriptor(flightCode, itinerary, passengerList);

        // if we wanted to flight to the system
        // AircraftManagementDatabase.getInstance().radarDetect(flightDescriptor);

        return flightDescriptor;
    }

    /**
     * Matches a flight's origin to an operator that flies from that airport
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

    private static Itinerary generateItinerary() {
        String[] from = new String[]{"ATL", "PEK", "LAX", "HND", "LHR"};
        String[] to = new String[]{"FRA", "SIN", "MAD", "SFO", "YYZ"};
        String[] next = new String[]{"DME", "SVX", "LCY", "BBU", "FCO"};
        Itinerary itinerary = new Itinerary(from[rng.nextInt(5)], to[rng.nextInt(5)], next[rng.nextInt(5)]);

        return itinerary;
    }

    private static PassengerList generatePassengers() {
        String[] names = new String[]{"Martha", "Timmy", "The b0$$", "Jack", "Lilly"};
        PassengerList passengerList = new PassengerList();

        // generate between 5 and 20 passengers
        int bound = rng.nextInt(16) + 5;
        for (int i = 0; i < bound; i++) {
            // randomly pick the name from the list above
            passengerList.addPassenger(new PassengerDetails(names[rng.nextInt(5)]));
        }

        return passengerList;
    }
    
    public static int generateRandomStatus()
    {
    	int ran = (rng.nextInt(100) + 1);
    	
    	return ran;
    }
}
