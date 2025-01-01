package dangeon.trap;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import dangeon.characters.Knight;

public class FireTrap extends AbstractActor {

    private Animation trapAnimation;
    private boolean isActive;
    private Orientation orientation;

    public enum Orientation { HORIZONTAL, VERTICAL }

    public FireTrap(String name, Orientation orientation) {
        this.isActive = false;
        this.orientation = orientation;
        setTrapAnimation(orientation);
        setAnimation(trapAnimation);
    }

    private void setTrapAnimation(Orientation orientation) {
        if (orientation == Orientation.VERTICAL) {
            trapAnimation = new Animation("sprites/dangeon/flamethrower_2_1.png", 32, 16, 0.2f, Animation.PlayMode.LOOP);
        } else {
            trapAnimation = new Animation("sprites/dangeon/flamethrower_1_1.png", 16, 32, 0.2f, Animation.PlayMode.LOOP);
        }
    }

    private void checkForDamage() {
        int currentFrame = trapAnimation.getCurrentFrameIndex();
        isActive = currentFrame != 0;

        Knight knight = (Knight) getScene().getFirstActorByName("Ellen");
        if (knight == null) {
            return;
        }

        if (intersects(knight) && isActive) {
            knight.getHealth().drain(1);
        }
    }

    private void animateTrap() {
        new Loop<>(
            new ActionSequence<>(
                new Invoke<>(() -> trapAnimation.stop()),
                new Wait<>(2.0f),
                new Invoke<>(() -> {
                    trapAnimation.play();
                    trapAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                }),
                new Wait<>(0.7f),
                new Invoke<>(() -> trapAnimation.stop())
            )
        ).scheduleFor(this);
    }

    public void activateTrap() {
        if (getScene() != null) {
            new Loop<>(
                new ActionSequence<>(
                    new Invoke<>(this::checkForDamage)
                )
            ).scheduleFor(this);
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        activateTrap();
        animateTrap();
    }
}
