package MazeGame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Maze {
    int shortestPathAllKeys(char[][] grid) {

        int startI = 0;
        int startJ = 0;
        int keysCount = 0;

        String keys = "";

        for (int i = 0; i < grid.length; i++) {// find the starting position
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'S') { // start found
                    startI = i;
                    startJ = j;
                }
                if (grid[i][j] >= 'a' && grid[i][j] <= 'f') {// to find the no of keys (1 <= k<=6)
                    keysCount++;
                    keys += grid[i][j];
                    // System.out.println("keys = "+keys);//check the order of keys gathered
                }
            }
        }

        return bfs(grid, startI, startJ, new HashSet<>(), "", keysCount);
    }

    public int bfs(char[][] grid, int i, int j, Set<String> visited, String state, int keysCount) {

        Queue<Cell> queue = new LinkedList<>();
        queue.offer(new Cell(i, j, state));
        visited.add(i + " " + j + " " + state);

        int level = 0;

        while (!queue.isEmpty()) {
            level++;
            int size = queue.size();

            for (int k = 0; k < size; k++) {
                Cell current = queue.poll();
                visited.add(current.i + " " + current.j + " " + current.state);

                // UP
                int nextU = current.i - 1; // nextu = nextUP
                if (current.i > 0 &&
                        (grid[nextU][current.j] == '.' || grid[nextU][current.j] == 'S' ||
                                (grid[nextU][current.j] >= 'a' && grid[nextU][current.j] <= 'f'
                                        && state.contains(String.valueOf(grid[nextU][current.j]))))
                        &&
                        !visited.contains(nextU + " " + current.j + " " + current.state)) {
                    queue.add(new Cell(nextU, current.j, current.state));
                } else if (current.i > 0 &&
                        grid[nextU][current.j] >= 'A' &&
                        grid[nextU][current.j] <= 'F'
                        && !visited.contains(nextU + " " + current.j + " " + current.state)) {
                    if (current.state.contains(String.valueOf(grid[nextU][current.j]).toLowerCase())) {
                        queue.add(new Cell(nextU, current.j, current.state));
                    }
                } else if (current.i > 0 &&
                        grid[nextU][current.j] >= 'a' &&
                        grid[nextU][current.j] <= 'f' &&
                        !visited.contains(nextU + " " + current.j + " " + current.state)) {
                    String newState = current.state;
                    if (!current.state.contains(String.valueOf(grid[nextU][current.j]))) {
                        newState += grid[nextU][current.j];
                        if (newState.length() == keysCount) {
                            return level;
                        }
                    }
                    queue.add(new Cell(nextU, current.j, newState));
                }

                // DOWN
                int nextD = current.i + 1;
                if (current.i < grid.length - 1 &&
                        (grid[nextD][current.j] == '.' || grid[nextD][current.j] == 'S' ||
                                (grid[nextD][current.j] >= 'a' && grid[nextD][current.j] <= 'f'
                                        && state.contains(String.valueOf(grid[nextD][current.j]))))
                        &&
                        !visited.contains(nextD + " " + current.j + " " + current.state)) {
                    queue.add(new Cell(nextD, current.j, current.state));
                } else if (current.i < grid.length - 1 &&
                        grid[nextD][current.j] >= 'A' &&
                        grid[nextD][current.j] <= 'F'
                        && !visited.contains(nextD + " " + current.j + " " + current.state)) {
                    if (current.state.contains(String.valueOf(grid[nextD][current.j]).toLowerCase())) {
                        queue.add(new Cell(nextD, current.j, current.state));
                    }
                } else if (current.i < grid.length - 1 &&
                        grid[nextD][current.j] >= 'a' &&
                        grid[nextD][current.j] <= 'f' &&
                        !visited.contains(nextD + " " + current.j + " " + current.state)) {
                    String newState = current.state;
                    if (!current.state.contains(String.valueOf(grid[nextD][current.j]))) {
                        newState += grid[nextD][current.j];
                        if (newState.length() == keysCount) {
                            return level;
                        }
                    }
                    queue.add(new Cell(nextD, current.j, newState));
                }

                // RIGHT
                int nextR = current.j - 1;
                if (current.j > 0 &&
                        (grid[current.i][nextR] == '.' || grid[current.i][nextR] == 'S' ||
                                (grid[current.i][nextR] >= 'a' && grid[current.i][nextR] <= 'f'
                                        && state.contains(String.valueOf(grid[current.i][nextR]))))
                        &&
                        !visited.contains(current.i + " " + nextR + " " + current.state)) {
                    queue.add(new Cell(current.i, nextR, current.state));
                } else if (current.j > 0 &&
                        grid[current.i][nextR] >= 'A' &&
                        grid[current.i][nextR] <= 'F'
                        && !visited.contains(current.i + " " + nextR + " " + current.state)) {
                    if (current.state.contains(String.valueOf(grid[current.i][nextR]).toLowerCase())) {
                        queue.add(new Cell(current.i, nextR, current.state));
                    }
                } else if (current.j > 0 &&
                        grid[current.i][nextR] >= 'a' &&
                        grid[current.i][nextR] <= 'f' &&
                        !visited.contains(current.i + " " + nextR + " " + current.state)) {
                    String newState = current.state;
                    if (!current.state.contains(String.valueOf(grid[current.i][nextR]))) {
                        newState += grid[current.i][nextR];
                        if (newState.length() == keysCount) {
                            return level;
                        }
                    }
                    queue.add(new Cell(current.i, nextR, newState));
                }
                // LEFT
                int nextL = current.j + 1;
                if (current.j < grid[current.i].length - 1 &&
                        (grid[current.i][nextL] == '.' || grid[current.i][nextL] == 'S' ||
                                (grid[current.i][nextL] >= 'a' && grid[current.i][nextL] <= 'f'
                                        && state.contains(String.valueOf(grid[current.i][nextL]))))
                        &&
                        !visited.contains(current.i + " " + nextL + " " + current.state)) {
                    queue.add(new Cell(current.i, nextL, current.state));
                } else if (current.j < grid[current.i].length - 1 &&
                        grid[current.i][nextL] >= 'A' &&
                        grid[current.i][nextL] <= 'F'
                        && !visited.contains(current.i + " " + nextL + " " + current.state)) {
                    if (current.state.contains(String.valueOf(grid[current.i][nextL]).toLowerCase())) {
                        queue.add(new Cell(current.i, nextL, current.state));
                    }
                } else if (current.j < grid[current.i].length - 1 &&
                        grid[current.i][nextL] >= 'a' &&
                        grid[current.i][nextL] <= 'f' &&
                        !visited.contains(current.i + " " + nextL + " " + current.state)) {
                    String newState = current.state;
                    if (!current.state.contains(String.valueOf(grid[current.i][nextL]))) {
                        newState += grid[current.i][nextL];
                        if (newState.length() == keysCount) {
                            return level;
                        }
                    }
                    queue.add(new Cell(current.i, nextL, newState));
                }

            }

        }

        return -1;

    }

    class Cell {
        int i;
        int j;
        String state = "";

        public Cell(int i, int j, String state) {
            this.i = i;
            this.j = j;
            this.state = state;
        }
    }

    public static void main(String[] args) {
        Maze sol = new Maze();
        char[][] map = {
                { 'S', '.', '.', 'a', 'E', 'e' },
                { 'A', 'W', 'W', 'W', 'W', 'D' },
                { 'b', '.', 'B', 'c', 'C', 'd' }
        };
        int minSteps = sol.shortestPathAllKeys(map);
        System.out.println(minSteps);
    }
}