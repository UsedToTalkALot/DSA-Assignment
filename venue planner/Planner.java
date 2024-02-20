import java.util.Scanner;

public class Planner {

    int minCost(int[][] costs) {
        if (costs == null || costs.length == 0 || costs[0].length == 0) {
            return 0;//incase of empty input
        }

        int n = costs.length;
        int k = costs[0].length;

        int[][]  cost_matrix = new int[n][k]; //using dynamic programming to solve the problem

        
        for (int j = 0; j < k; j++) {
             cost_matrix[0][j] = costs[0][j];//copy first row same to same
        }

        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < k; j++) {
                
                 cost_matrix[i][j] = Integer.MAX_VALUE;
                for (int adjcentTheme = 0; adjcentTheme < k; adjcentTheme++) {
                    if (adjcentTheme != j) {
                         cost_matrix[i][j] = Math.min( cost_matrix[i][j],  cost_matrix[i-1][adjcentTheme] + costs[i][j]);//get the min value
                    }
                }
            }
        }

        // Find the minimum cost among the last row of  cost_matrix
        int minCost = Integer.MAX_VALUE;
        for (int j = 0; j < k; j++) {
            minCost = Math.min(minCost,  cost_matrix[n - 1][j]);
        }

        return minCost;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of venues: ");
        int n = scanner.nextInt();

        System.out.print("Enter the number of themes: ");
        int k = scanner.nextInt();

        int[][] costs = new int[n][k];

        System.out.println("Enter the cost matrix:");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                System.out.print("Cost for venue " + (i + 1) + " with theme " + (j + 1) + ": ");
                costs[i][j] = scanner.nextInt();
            }
        }

        Planner p = new Planner();
        int cost = p.minCost(costs);
        System.out.println("Minimum cost: " + cost);

        scanner.close();
    }
}
