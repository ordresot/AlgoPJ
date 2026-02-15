package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    final int MAX_UNITS_PER_TYPE = 11;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {

        Army army = new Army();

        int remainingPoints = maxPoints;
        List<Unit> selectedUnits = new ArrayList<>();
        Map<String, Integer> typeCount = new HashMap<>();
        List<Queue<Integer>> availablePositions = new ArrayList<>();

        Random random = new Random();

        unitList.sort((unit1, unit2) -> {
            double eff1 = (double) unit1.getBaseAttack() / unit1.getCost() + (double) unit1.getHealth() / unit1.getCost();
            double eff2 = (double) unit2.getBaseAttack() / unit2.getCost() + (double) unit2.getHealth() / unit2.getCost();
            return Double.compare(eff2, eff1);
        });

        for (int x = 0; x < 3; x++) {
            Queue<Integer> positions = new LinkedList<>();
            for (int y = 0; y < 21; y++) {
                positions.add(y);
            }
            availablePositions.add(positions);
        }

        while (true) {
            boolean unitAdded = false;

            List<Integer> xOrder = Arrays.asList(0, 1, 2);
            Collections.shuffle(xOrder, random);

            for (Unit unitType : unitList) {
                String type = unitType.getUnitType();
                int currentCount = typeCount.getOrDefault(type, 0);
                int cost = unitType.getCost();

                if (currentCount >= MAX_UNITS_PER_TYPE) continue;
                if (cost > remainingPoints) continue;

                for (int x : xOrder) {

                    Queue<Integer> positions = availablePositions.get(x);
                    Integer y = positions.poll();

                    if (y != null) {
                        Unit newUnit = new Unit(
                                type + " " + (currentCount + 1),
                                type,
                                unitType.getHealth(),
                                unitType.getBaseAttack(),
                                unitType.getCost(),
                                unitType.getAttackType(),
                                unitType.getAttackBonuses(),
                                unitType.getDefenceBonuses(),
                                x,
                                y
                        );

                        selectedUnits.add(newUnit);
                        typeCount.put(type, currentCount + 1);
                        remainingPoints -= cost;
                        unitAdded = true;
                        break;
                    }
                }

                if (unitAdded) break;
            }

            if (!unitAdded) break;
        }

        army.setUnits(selectedUnits);
        return army;
    }
}