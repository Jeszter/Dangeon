package dangeon.trap;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import dangeon.ammunition.JustArrow;

public class Dispenser extends AbstractActor {

    private Animation dispenserAnimation;
    private boolean isOn;

    public boolean isOn() {
        return isOn;
    }

    public Dispenser() {
        dispenserAnimation = new Animation("sprites/dangeon/arrow_1.png", 16, 32, 0.3f, Animation.PlayMode.LOOP);
        this.isOn = false;
        dispenserAnimation.stop();
        setAnimation(dispenserAnimation);
        startSpawning();
    }

    private void spawnArrow() {
        getScene().addActor(new JustArrow(), getPosX(), getPosY());
    }

    public void startSpawning() {
        if (getScene() != null) {
            new Loop<>(
                new ActionSequence<>(
                    new Invoke<>(this::spawnArrow),
                    new Wait<>(1.2f)
                )
            ).scheduleFor(this);
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        startSpawning();
    }
}
