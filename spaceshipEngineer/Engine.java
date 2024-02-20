package spaceshipEngineer;
import java.util.Arrays;

public class Engine {
    public static int timeForMaintainance(int[] engine, int splitCost) {
        int Engine = engine.length; //number of engine
        int[] dp = new int[Engine + 1]; // use dynamic programming to store minimun time to build engine

        Arrays.fill(dp, Integer.MAX_VALUE); //max value to compare and get min value
        dp[0] = 0; //set 1st engine to 0

        for (int i = 1; i <= Engine; i++) {
            dp[i] = engine[i - 1] + splitCost; // time to build one engine and + split cost
            for (int j = 1; j < i; j++) {
                dp[i] = Math.min(dp[i], dp[j] + dp[i - j]); // update minimum time
            }
        }
        return dp[Engine]; //min time
    }

    public static void main(String[] args) {
        int[] engine = {1,2,3};
        int splitCost = 1;

        int minTime = timeForMaintainance(engine, splitCost);
        System.out.println("Minimum time to build all engine= " + minTime+" units");
    }
}