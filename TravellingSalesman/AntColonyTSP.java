package TravellingSalesman;

import java.util.Arrays;
import java.util.Random;

public class AntColonyTSP {

    private static final int NUM_ANTS = 10;
    private static final double pheremon = 1.0; // Pheromone importance
    private static final double dist = 2.0;  // Distance importance
    private static final double evapo = 0.5;   // Evaporation rate
    private static final double Q = 100.0;   // Pheromone deposit amount

    public static int[][] generateAntSolutions(int[][] graph, int[][] pheromones) {
        int n = graph.length;
        int[][] solutions = new int[NUM_ANTS][n];

        for (int ant = 0; ant < NUM_ANTS; ant++) {
            boolean[] visited = new boolean[n];
            solutions[ant][0] = 0;  // Start from city 0

            for (int step = 1; step < n; step++) {
                int currentCity = solutions[ant][step - 1];
                int nextCity = selectNextCity(graph, pheromones, visited, currentCity);
                solutions[ant][step] = nextCity;
                visited[nextCity] = true;
            }
        }

        return solutions;
    }

    public static int selectNextCity(int[][] graph, int[][] pheromones, boolean[] visited, int currentCity) {
        int n = graph.length;
        double[] probabilities = new double[n];
        double total = 0.0;

        for (int city = 0; city < n; city++) {
            if (!visited[city]) {
                double pheromone = Math.pow(pheromones[currentCity][city], pheremon);
                double distance = Math.pow(1.0 / graph[currentCity][city], dist);
                probabilities[city] = pheromone * distance;
                total += probabilities[city];
            }
        }

        // Roulette wheel selection
        double rand = Math.random() * total;
        double sum = 0.0;
        for (int city = 0; city < n; city++) {
            if (!visited[city]) {
                sum += probabilities[city];
                if (sum >= rand) {
                    return city;
                }
            }
        }

        // This should not happen
        return -1;
    }

    public static void updatePheromones(int[][] graph, int[][] pheromones, int[][] solutions) {
        int n = graph.length;

        // Evaporation
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pheromones[i][j] *= (1.0 - evapo);
            }
        }

        // Pheromone deposit
        for (int ant = 0; ant < NUM_ANTS; ant++) {
            double contribution = Q / calculateTourLength(graph, solutions[ant]);
            for (int i = 0; i < n - 1; i++) {
                int from = solutions[ant][i];
                int to = solutions[ant][i + 1];
                pheromones[from][to] += contribution;
                pheromones[to][from] += contribution;
            }
        }
    }

    public static int calculateTourLength(int[][] graph, int[] tour) {
        int length = 0;
        for (int i = 0; i < tour.length - 1; i++) {
            length += graph[tour[i]][tour[i + 1]];
        }
        length += graph[tour[tour.length - 1]][tour[0]]; // Return to the starting city
        return length;
    }

    public static void main(String[] args) {
        int[][] graph = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };

        int n = graph.length;
        int[][] pheromones = new int[n][n];
        Random random = new Random();

        // Initialize pheromones
        for (int i = 0; i < n; i++) {
            Arrays.fill(pheromones[i], 1);
        }

        int iterations = 90;
        for (int iter = 0; iter < iterations; iter++) {
            int[][] antSolutions = generateAntSolutions(graph, pheromones);
            updatePheromones(graph, pheromones, antSolutions);

            // Optional: Display the best tour length in each iteration
            int bestTourLength = Integer.MAX_VALUE;
            int[] bestTour = null;
            for (int[] solution : antSolutions) {
                int tourLength = calculateTourLength(graph, solution);
                if (tourLength < bestTourLength) {
                    bestTourLength = tourLength;
                    bestTour = solution.clone();
                }
            }

            System.out.println("Iteration " + (iter + 1) + ": Best Tour Length = " + bestTourLength + ", Tour = " + Arrays.toString(bestTour));
        }
    }
}
