package TSPUsingACA;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

// Constants (unchanged)
class Constant {
    static int CITY_NUM; // Number of cities
    static Route[][] routes; // Map of routes
    static final float C = 10.0f; // Initial pheromone value

    static {
        // City coordinates
        int[][] cityPoint = {
                { 0, 0 }, { 12, 32 }, { 5, 25 }, { 8, 45 }, { 33, 17 },
                { 25, 7 }, { 15, 15 }, { 15, 25 }, { 25, 15 }, { 41, 12 }
        };

        // Determine the number of cities and create the route map
        CITY_NUM = cityPoint.length;
        routes = new Route[CITY_NUM][CITY_NUM];
        for (int i = 0; i < CITY_NUM; i++)
            for (int j = 0; j < CITY_NUM; j++)
                routes[i][j] = new Route();

        for (int i = 0; i < CITY_NUM; i++)
            for (int j = i; j < CITY_NUM; j++) {
                float dis = (float) Math.sqrt(Math.pow((cityPoint[i][0] - cityPoint[j][0]), 2) +
                        Math.pow((cityPoint[i][1] - cityPoint[j][1]), 2));

                // Insert distance
                routes[i][j].distance = dis;
                routes[j][i].distance = dis;
            }
    }
}

// Route Class (unchanged)
class Route {
    float distance; // Distance
    float pheromone; // Pheromone

    Route() {
        distance = 0.0f;
        pheromone = Constant.C;
    }
}

// Ant Class
class Ant {
    int[] passed; // List of cities already visited (tabu list)
    float passedLength = 0.0f; // Total travel distance
    int curCity; // Current city of the ant
    int curIndex; // Current index in the list of visited cities

    // Initialize ant data
    void init() {
        initPassed();
        passedLength = 0.0f;
        curIndex = 0;
        initBeginCity();
    }

    // Initialize the tabu list
    void initPassed() {
        passed = new int[Constant.CITY_NUM + 1];
        for (int i = 0; i < passed.length; i++)
            passed[i] = Integer.MIN_VALUE;
    }

    // Initialize the city where the ant starts
    void initBeginCity() {
        Random rand = new Random();
        int beginCity = rand.nextInt(Constant.CITY_NUM);
        reachNextCity(beginCity);
    }

    // Move to the next city
    void reachNextCity(int nextCity) {
        // Accumulate travel distance
        passedLength += Constant.routes[curCity][nextCity].distance;

        // Move forward
        curCity = nextCity;
        passed[curIndex++] = nextCity + 1;
    }

    // Check if the city nCity is in the tabu list
    boolean isPassedCity(int nCity) {
        for (int i = 0; passed[i] != Integer.MIN_VALUE; i++) {
            if (passed[i] == nCity) // City already visited
                return true;
        }
        return false;
    }

}

// Ant Colony Algorithm (unchanged)
class AntAlgorithm {
    private int NC = 10; // Number of iterations
    private int antNum = 10; // Number of ants
    private Ant[] ants; // Ant colony
    private float Q = 300.0f;
    private float p = 0.3f; // Evaporation rate

    float minLength = Float.MAX_VALUE; // Current shortest distance
    int[] minRoute; // Current shortest route

    AntAlgorithm() {
        ants = new Ant[antNum];
        for (int i = 0; i < antNum; i++)
            ants[i] = new Ant();

        minRoute = new int[Constant.CITY_NUM];
    }

    void run() {
        for (int nc = 1; nc <= NC; nc++) // Number of iterations
        {
            // Initialize ant data
            for (int k = 0; k < ants.length; k++)
                ants[k].init();

            // Traverse all cities
            for (int look = 1; look < Constant.CITY_NUM; look++) {
                for (int k = 0; k < ants.length; k++)// Each ant
                {
                    int nextCity = select(ants[k]); // Select the next city
                    ants[k].reachNextCity(nextCity); // Move to the next city
                }
            }

            // Return to the starting city and calculate the optimal path
            for (int k = 0; k < ants.length; k++)// Each ant
            {
                ants[k].reachNextCity(ants[k].passed[0] - 1);
                if (minLength > ants[k].passedLength) {
                    minLength = ants[k].passedLength; // Record the shortest distance
                    copyRoute(ants[k].passed); // Record the shortest route
                }
            }

            // Update pheromones in routes
            for (int i = 0; i < Constant.CITY_NUM; i++)
                for (int j = 0; j < Constant.CITY_NUM; j++) {
                    // Evaporate pheromones in all routes
                    Constant.routes[i][j].pheromone *= p;

                    for (int k = 0; k < ants.length; k++) {
                        for (int n = 0; n < Constant.CITY_NUM; n++) {
                            int curCity = ants[k].passed[n] - 1;
                            int nextCity = ants[k].passed[(n + 1) % Constant.CITY_NUM] - 1;

                            if (curCity == i && nextCity == j) {// This path has been taken
                                // Update pheromones in the route from curCity to nextCity
                                float dp = Q / ants[k].passedLength; // Pheromone increment
                                Constant.routes[i][j].pheromone += dp;
                            }
                        }
                    }
                }
            print();
        }
    }

    // Calculate selection probability + roulette wheel selection
    int select(Ant ant) {
        float totalVAP = 0.0f;
        List<CanReachCity> canSelectedCityList = new LinkedList<>();
        for (int nextCity = 0; nextCity < Constant.CITY_NUM; nextCity++) {
            if (!ant.isPassedCity(nextCity + 1))// City can be selected
            {
                double visibility = 1.0f / Constant.routes[ant.curCity][nextCity].distance; // Visibility
                visibility = Math.pow(visibility, 3);
                double pheromone = Constant.routes[ant.curCity][nextCity].pheromone;
                pheromone = Math.pow(pheromone, 2);

                float VAP = (float) visibility + (float) pheromone;
                totalVAP += VAP; // Accumulate VAP
                // Alpha and Beta **********************************************
                CanReachCity rCity = new CanReachCity(nextCity, VAP);

                canSelectedCityList.add(rCity);// Add to the list
            }
        }

        // Calculate the probability of each city being selected
        ListIterator<CanReachCity> iterator = canSelectedCityList.listIterator();
        while (iterator.hasNext()) {
            // Get the city
            CanReachCity rCity = iterator.next();

            // Calculate the probability
            rCity.rate = rCity.VAP / totalVAP;
        }

        // Roulette wheel selection of one city
        float rate = (float) Math.random();
        iterator = canSelectedCityList.listIterator();
        while (iterator.hasNext()) {
            CanReachCity rCity = iterator.next();
            if (rate <= rCity.rate)
                return rCity.id;
            else
                rate = rate - rCity.rate;
        }

        // Return the last city due to precision issues
        iterator = canSelectedCityList.listIterator();
        while (iterator.hasNext()) {
            CanReachCity rCity = iterator.next();
            if (!iterator.hasNext()) // Last element
                return rCity.id;
        }

        return Integer.MAX_VALUE;
    }

    // Copy the route
    void copyRoute(int[] route) {
        System.arraycopy(route, 0, minRoute, 0, minRoute.length);
    }

    void printRoute() {
        System.out.print("Shortest Route: ");
        for (int i = 0; i < minRoute.length; i++)
            System.out.print(minRoute[i] + "->");
        System.out.print(minRoute[0] + "\n" + "Shortest Distance: " + minLength);
    }

    void print() {
        System.out.println("Shortest Distance: " + minLength);
    }
}

// Class representing cities that can be reached in the next step (unchanged)
class CanReachCity {
    int id;
    float rate = 0.0f; // Selection probability
    float VAP = 0.0f; // Sum of visibility and pheromone concentration

    CanReachCity(int id, float VAP) {
        this.id = id;
        this.VAP = VAP;
    }
}

// Main Class
public class AntColony {
    public static void main(String[] args) {
        AntAlgorithm AA = new AntAlgorithm();
        AA.run();
        AA.printRoute();
    }
}
