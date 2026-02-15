package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {

        List<Unit> suitableUnits = new ArrayList<>();

        if (isLeftArmyTarget) {

            Set<Integer> occupiedY = new HashSet<>();

            for (int columnIndex = unitsByRow.size() - 1; columnIndex >= 0; columnIndex--) {

                List<Unit> column = unitsByRow.get(columnIndex);

                if (column == null) continue;

                for (Unit unit : column) {
                    if (!unit.isAlive()) continue;

                    if (!occupiedY.contains(unit.getyCoordinate())) {
                        suitableUnits.add(unit);
                    }

                    occupiedY.add(unit.getyCoordinate());
                }
            }
        } else {

            Set<Integer> occupiedY = new HashSet<>();

            for (int columnIndex = 0; columnIndex < unitsByRow.size(); columnIndex++) {

                List<Unit> column = unitsByRow.get(columnIndex);

                if (column == null) continue;

                for (Unit unit : column) {
                    if (!unit.isAlive()) continue;

                    if (!occupiedY.contains(unit.getyCoordinate())) {
                        suitableUnits.add(unit);
                    }


                    occupiedY.add(unit.getyCoordinate());
                }
            }
        }

        return suitableUnits;
    }
}
