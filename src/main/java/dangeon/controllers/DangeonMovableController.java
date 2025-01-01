package dangeon.controllers;

import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import dangeon.characters.Knight;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DangeonMovableController implements KeyboardListener {
    private final DangeonMovable actor;
    private DangeonMove<DangeonMovable> cDangeonMove;
    private Set<DangeonDirection> keys = new HashSet<>();

    private Map<Input.Key, DangeonDirection> keyDirectionMap = Map.ofEntries(
        Map.entry(Input.Key.UP, DangeonDirection.NORTH),
        Map.entry(Input.Key.RIGHT, DangeonDirection.EAST),
        Map.entry(Input.Key.DOWN, DangeonDirection.SOUTH),
        Map.entry(Input.Key.LEFT, DangeonDirection.WEST)
    );

    public DangeonMovableController(DangeonMovable actor) {
        this.actor = actor;
        this.cDangeonMove = null;
    }

    private void stopMoving() {
        if (actor != null && cDangeonMove != null) {
            actor.stoppedMoving();
            cDangeonMove.stop();
        }
    }

    private void updateMove() {
        if (actor instanceof Knight && ((Knight) actor).isAttacking()) {
            return;
        }

        DangeonDirection combinedDangeonDirection = DangeonDirection.NONE;
        for (DangeonDirection dangeonDirection : keys) {
            if (dangeonDirection != null) {
                combinedDangeonDirection = combinedDangeonDirection.combine(dangeonDirection);
            }
        }
        if (combinedDangeonDirection != DangeonDirection.NONE) {
            if (cDangeonMove != null) {
                stopMoving();
            }
            cDangeonMove = new DangeonMove<>(combinedDangeonDirection, Float.MAX_VALUE);
            cDangeonMove.scheduleFor(actor);
        } else {
            stopMoving();
        }
    }

    @Override
    public void keyPressed(Input.Key key) {
        if (keyDirectionMap.containsKey(key) && !(actor instanceof Knight && ((Knight) actor).isAttacking())) {
            keys.add(keyDirectionMap.get(key));
            updateMove();
        }
    }

    @Override
    public void keyReleased(Input.Key key) {
        if (keyDirectionMap.containsKey(key)) {
            keys.remove(keyDirectionMap.get(key));
            updateMove();
        }
    }
}
