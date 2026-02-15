package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {

        List<Unit> playerAlive = new ArrayList<>(playerArmy.getUnits());
        List<Unit> computerAlive = new ArrayList<>(computerArmy.getUnits());

        while (!playerAlive.isEmpty() && !computerAlive.isEmpty()) {

            List<Unit> turnOrder = new ArrayList<>();

            turnOrder.addAll(playerAlive);
            turnOrder.addAll(computerAlive);

            turnOrder.sort(
                    (u1, u2) -> {
                        int attackCompare = Integer.compare(u2.getBaseAttack(), u1.getBaseAttack());

                        if (attackCompare != 0) { return attackCompare; }

                        return Integer.compare(u1.getHealth(), u2.getHealth());
                    });

            for (Unit attacker : turnOrder) {

                if (!attacker.isAlive()) continue;

                Army enemyArmy = attacker.getxCoordinate() < 3 ? computerArmy : playerArmy;

                if (enemyArmy.getUnits().stream().noneMatch(Unit::isAlive)) {
                    break;
                }

                Unit target = attacker.getProgram().attack();

                if (target != null) {
                    printBattleLog.printBattleLog(attacker, target);
                }
            }

            playerAlive.removeIf(unit -> !unit.isAlive());
            computerAlive.removeIf(unit -> !unit.isAlive());
        }
    }
}