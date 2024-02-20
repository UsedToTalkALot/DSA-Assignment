package TravellingSalesman;

import java.util.Arrays;

public class TSP{

    public static int tsp(int[][] graph, int mask, int pos, int n, int[][] dp) {
        if (mask == (1 << n) - 1) {
            return graph[pos][0];  // Return to the starting city
        }

        if (dp[mask][pos] != -1) {
            return dp[mask][pos];
        }

        int ans = Integer.MAX_VALUE;

        for (int city = 0; city < n; city++) {
            if ((mask & (1 << city)) == 0) {
                int newMask = mask | (1 << city);
                int newDist = graph[pos][city] + tsp(graph, newMask, city, n, dp);
                ans = Math.min(ans, newDist);
            }
        }

        dp[mask][pos] = ans;
        return ans;
    }

    public static void main(String[] args) {
        int[][] graph = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };

        int n = graph.length;
        int[][] dp = new int[1 << n][n];
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }

        int minCost = tsp(graph, 1, 0, n, dp); // Start from city 0

        System.out.println("Minimum Cost: " + minCost);
    }
}

