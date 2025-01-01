package dangeon.ammunition;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import dangeon.characters.Knight;

public class JustArrow extends AbstractActor {
    private Animation arrowAnimation;

    public JustArrow() {
        arrowAnimation = new Animation("sprites/dangeon/Just_arrow.png", 16, 16);
        setAnimation(arrowAnimation);
        startMovement();
    }

    private void moveUp() {
        setPosition(getPosX(), getPosY() - 1);
        MapTile currentTile = getScene().getMap().getTile(getPosX() / 16, getPosY() / 16);

        if (currentTile.getType() == MapTile.Type.WALL) {
            getScene().removeActor(this);
            return;
        }

        Knight target = (Knight) getScene().getFirstActorByName("Ellen");
        if (target != null && intersects(target)) {
            target.getHealth().drain(10);
            getScene().removeActor(this);
        }
    }

    private void startMovement() {
        if (getScene() != null) {
            new Loop<>(
                new ActionSequence<>(
                    new Invoke<>(this::moveUp)
                )
            ).scheduleFor(this);
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        startMovement();
    }
}
