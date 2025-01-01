package dangeon.trap;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import dangeon.characters.Knight;

public class Peaks extends AbstractActor {

    private Animation trapAnimation;
    private boolean isActive;

    public Peaks() {
        trapAnimation = new Animation("sprites/dangeon/peaks_1.png", 16, 16, 0.3f, Animation.PlayMode.LOOP_PINGPONG);
        isActive = false;
        setAnimation(trapAnimation);
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

    private void controlTrapAnimation() {
        new Loop<>(
            new ActionSequence<>(
                new Invoke<>(() -> trapAnimation.stop()),
                new Wait<>(2.0f),
                new Invoke<>(() -> {
                    trapAnimation.play();
                    trapAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
                }),
                new Wait<>(2f),
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
        controlTrapAnimation();
    }
}
