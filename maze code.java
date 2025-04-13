import java.util.*;

public class MazeSolver {

    // Constants
    public static final char WALL = '#';
    public static final char PATH = ' ';
    public static final char START = 'S';
    public static final char EXIT = 'E';
    public static final char VISITED = '.';

    static class Maze {
        private int rows, cols;
        private char[][] maze;
        private boolean[][] visited;
        private final int[] dx = {0, 1, 0, -1};  // Directions for right, down, left, up
        private final int[] dy = {1, 0, -1, 0};  // Directions for right, down, left, up
        private final int startX = 1, startY = 1;
        private final int endX, endY;

        public Maze(int rows, int cols) {
            if (rows < 5 || cols < 5) {
                throw new IllegalArgumentException("Maze dimensions must be at least 5x5.");
            }
            this.rows = rows;
            this.cols = cols;
            this.endX = rows - 2;
            this.endY = cols - 2;
            this.maze = new char[rows][cols];
            this.visited = new boolean[rows][cols];
        }

        // Maze generation using recursive backtracking
        public void generateMaze() {
            // Fill maze with walls
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    maze[i][j] = WALL;
                }
            }

            // Start carving the maze from the start position
            maze[startX][startY] = PATH;
            carveMaze(startX, startY);

            // Mark the start and exit points
            maze[startX][startY] = START;
            maze[endX][endY] = EXIT;
        }

        private void carveMaze(int x, int y) {
            List<int[]> directions = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                directions.add(new int[]{dx[i], dy[i]});
            }
            Collections.shuffle(directions);

            for (int[] direction : directions) {
                int nx = x + direction[0] * 2;
                int ny = y + direction[1] * 2;
                if (nx >= 1 && nx < rows - 1 && ny >= 1 && ny < cols - 1 && maze[nx][ny] == WALL) {
                    maze[x + direction[0]][y + direction[1]] = PATH;
                    maze[nx][ny] = PATH;
                    carveMaze(nx, ny);
                }
            }
        }

        // DFS-based pathfinding
        public boolean solveMaze() {
            return dfs(startX, startY);
        }

        private boolean dfs(int x, int y) {
            if (x < 0 || y < 0 || x >= rows || y >= cols) return false;
            if (visited[x][y]) return false;
            if (maze[x][y] == WALL) return false;

            visited[x][y] = true;

            if (maze[x][y] == EXIT) {
                maze[x][y] = VISITED;
                return true;
            }

            maze[x][y] = VISITED;

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (dfs(nx, ny)) {
                    return true;
                }
            }

            // Backtrack if no path is found
            return false;
        }

        // Print the maze
        public void printMaze() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    System.out.print(maze[i][j]);
                }
                System.out.println();
            }
        }
    }

    // Main function to drive the program
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter the number of rows for the maze (min 5): ");
            int rows = Integer.parseInt(sc.nextLine());
            System.out.print("Enter the number of columns for the maze (min 5): ");
            int cols = Integer.parseInt(sc.nextLine());

            Maze maze = new Maze(rows, cols);
            maze.generateMaze();

            System.out.println("\nGenerated Maze:");
            maze.printMaze();

            if (maze.solveMaze()) {
                System.out.println("\nPath Found!");
            } else {
                System.out.println("\nNo Path Found.");
            }

            maze.printMaze();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}