package TSPUsingACA;

// Constants
class Constant {
    static int CITY_NUM; // Number of cities
    static Route[][] routes; // Map of routes
    static final float C = 10.0f; // Initial pheromone value

    static {
        // City coordinates
        int[][] cityPoint = {
                {0, 0}, {12, 32}, {5, 25}, {8, 45}, {33, 17},
                {25, 7}, {15, 15}, {15, 25}, {25, 15}, {41, 12}
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

// Route Class
class Route {
    float distance; // Distance
    float pheromone; // Pheromone

    Route() {
        distance = 0.0f;
        pheromone = Constant.C;
    }
}

