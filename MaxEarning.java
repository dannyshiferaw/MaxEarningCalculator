import java.io.*;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class calculates the max earning. Beginning from the 
 * start city, it checks neighbouring cities and choooses
 * one with higher sum income (CurrentMoneyAvailable - Cost of Travel
 * + EarningInDestinationCity. It does the same for the rest of cities
 * until destination is met.
 */
public class MaxEarning {

    private static class Pair implements Comparable<Pair> {

        int money;
        String city;

        public Pair (int money, String city) {
            this.money = money;
            this.city = city;
        }
        
        /** returns value greater than 0 if the other pair
         * is greater, value less than 0 otherwise.
         * if both are equal, it breaks tie with alphabetical
         * order of the names of the cities.
         */
        @Override
        public int compareTo(Pair other) {
            int diff = other.money - money;
            if (diff == 0) {
                return other.city.compareTo(city);
            }
            return diff;
        }
        
        /** returns true if both pairs have the same name, 
         * false otherwise. 
         */
        @Override
        public boolean equals(Object other) {
            if (this.getClass() == other.getClass()) {
                Pair temp  = (Pair) other;
                return city.compareTo(temp.city) == 0;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return city.hashCode() * 31 + money;
        }

        @Override
        public String toString() {
            return city;
        }
    }

    //Num of cities
    private int numOfCities = 0;

    //Num of edges between cities
    private int numOfEdges = 0;

    //List of cities
    private ArrayList<String> cities;

    //List of earning in each cities following the order in cities
    private ArrayList<Integer> earning;

    //Mapping of cities and corresponding earning
    private HashMap<String, Integer> cityEarning;

    //List of mappings of cities with their neighbours
    private HashMap<String, HashMap<String, Integer>> neigbours;

    //Set of cities visited
    private HashSet<Pair> visited;

    //Sorts neighbours
    private Queue<Pair> notVisited;


    public MaxEarning(String inputFile) {
        cities = new ArrayList<>();
        earning = new ArrayList<>();
        cityEarning = new HashMap<>();
        neigbours = new HashMap<>();
        visited = new HashSet<>();
        notVisited = new PriorityQueue<>();
        readInput(inputFile);
    }
    
    /** reads from file */
    private void readInput(String inputFile) {
        BufferedReader bufferReader;
        FileReader fileReader;

        try {
            File file = new File(inputFile).getAbsoluteFile();
            fileReader = new FileReader(file);
            bufferReader = new BufferedReader(fileReader);

            numOfCities = Integer.parseInt(bufferReader.readLine());
            numOfEdges = Integer.parseInt(bufferReader.readLine());

            //make list of cities
            for (String city: bufferReader.readLine().split(" ")) {
                cities.add(city);
            }
            //make list of earnings of each cities
            for (String money : bufferReader.readLine().split(" ")) {
                earning.add(Integer.parseInt(money));
            }
            //map each cities with coresponding earnings
            for (int i = 0; i < numOfCities; i++) {
                cityEarning.put(cities.get(i), earning.get(i));
            }

            //initialize
            for (int i = 0; i < numOfCities; i++) {
                neigbours.put(cities.get(i), new HashMap<>());
            }
            //Read edges
            String line;
            while((line = bufferReader.readLine())!= null) {
                String[] temp = line.split(" ");
                String city = temp[0];
                String neighbour = temp[1];
                int cost = Integer.parseInt(temp[2]);
                neigbours.get(city).put(neighbour, cost);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** calculates the possible max money that can be earned */
    public int calcMaxEarning() {

        String start = cities.get(0);
        String dest = cities.get(cities.size() - 1);

        //Start
        Pair currentCity = new Pair(5, start);

        //Destination
        Pair destination = new Pair(0, dest);
        
        //first to be visited
        notVisited.add(currentCity);
        
        //available money
        int currentMoney = currentCity.money;

        while (!notVisited.isEmpty()) {
            Pair current = notVisited.poll();
            visited.add(current);

            for (String city: neigbours.get(current.city).keySet()) {
                int tempMoney = cityEarning.get(current.city) + neigbours.get(current.city).get(city) +
                        cityEarning.get(city);
                Pair tempCity = new Pair(tempMoney, city);
                if (visited.contains(tempCity)) {
                    continue;
                }
                if (tempCity.equals(destination)) {
                    return tempMoney;
                }
                int cmp = tempCity.compareTo(current);
                if (cmp > 0) {
                    continue;
                }
                if (tempCity.money > currentMoney) {
                    //update currentMoney
                    //update individual earning of a city
                    //add to set of not visited cities
                    currentMoney = tempCity.money;
                    cityEarning.put(tempCity.city, currentMoney);
                    notVisited.add(tempCity);
                }

            }
        }
        return currentMoney;
    }
}
