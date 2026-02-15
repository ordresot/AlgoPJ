package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {
            {0, 1},
            {1, 0},
            {0, -1},
            {-1, 0}
    };

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {

        boolean[][] occupied = new boolean[WIDTH][HEIGHT];
        int[][] distance = new int[WIDTH][HEIGHT];
        Edge[][] previous = new Edge[WIDTH][HEIGHT];

        if (
                attackUnit.getxCoordinate() == targetUnit.getxCoordinate() &&
                attackUnit.getyCoordinate() == targetUnit.getyCoordinate()
        ) {
            return List.of(new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate()));
        }

        for (Unit unit : existingUnitList) {
            if (
                    unit.isAlive() &&
                            unit != attackUnit &&
                            unit != targetUnit
            ) {
                occupied[unit.getxCoordinate()][unit.getyCoordinate()] = true;
            }
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                distance[i][j] = Integer.MAX_VALUE;
            }
        }

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt(n -> n.distance)
        );

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        distance[startX][startY] = 0;

        priorityQueue.add(new Node(startX, startY, 0));

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();

            if (current.x == targetUnit.getxCoordinate() && current.y == targetUnit.getyCoordinate()) {
                break;
            }

            if (current.distance > distance[current.x][current.y]) {
                continue;
            }

            for (int[] dir : DIRECTIONS) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT) {
                    continue;
                }

                if (occupied[newX][newY]) {
                    continue;
                }

                int newDistance = distance[current.x][current.y] + 1;

                if (newDistance < distance[newX][newY]) {
                    distance[newX][newY] = newDistance;
                    previous[newX][newY] = new Edge(current.x, current.y);
                    priorityQueue.add(new Node(newX, newY, newDistance));
                }
            }
        }

        return buildPath(previous, startX, startY, targetUnit.getxCoordinate(), targetUnit.getyCoordinate());
    }

    private List<Edge> buildPath(Edge[][] previous, int startX, int startY, int targetX, int targetY) {

        LinkedList<Edge> path = new LinkedList<>();

        if (previous[targetX][targetY] == null && (startX != targetX || startY != targetY)) {
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

    private static class Node {
        int x;
        int y;
        int distance;

        Node(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }
    }
}