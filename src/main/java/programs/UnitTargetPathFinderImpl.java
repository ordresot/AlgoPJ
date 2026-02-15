package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {
            {0, 1},   // вверх
            {1, 0},   // вправо
            {0, -1},  // вниз
            {-1, 0}   // влево
    };

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {

        boolean[][] occupied = new boolean[WIDTH][HEIGHT];
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Edge[][] previous = new Edge[WIDTH][HEIGHT];
        Queue<int[]> queue = new LinkedList<>();

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                occupied[unit.getxCoordinate()][unit.getyCoordinate()] = true;
            }
        }

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentX = current[0];
            int currentY = current[1];

            if (currentX == targetX && currentY == targetY) {
                break;
            }

            for (int[] dir : DIRECTIONS) {
                int newX = currentX + dir[0];
                int newY = currentY + dir[1];

                if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT) {
                    continue;
                }

                if (!occupied[newX][newY] && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    previous[newX][newY] = new Edge(currentX, currentY);
                    queue.add(new int[]{newX, newY});
                }
            }
        }

        return buildPath(previous, startX, startY, targetX, targetY);
    }

    private List<Edge> buildPath(Edge[][] previous, int startX, int startY, int targetX, int targetY) {
        LinkedList<Edge> path = new LinkedList<>();

        if (previous[targetX][targetY] == null) {
            return new ArrayList<>();
        }

        int currentX = targetX;
        int currentY = targetY;

        while (currentX != startX || currentY != startY) {
            path.addFirst(new Edge(currentX, currentY));
            Edge prev = previous[currentX][currentY];
            if (prev == null) break;
            currentX = prev.getX();
            currentY = prev.getY();
        }

        path.addFirst(new Edge(startX, startY));

        return path;
    }
}