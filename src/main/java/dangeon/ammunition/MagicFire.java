package dangeon.ammunition;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import dangeon.characters.Knight;

import java.util.Objects;

public class MagicFire extends AbstractActor {
    private boolean isActive;
    private Knight targetKnight;
    private Animation magicArrowAnimation;

    public MagicFire() {
        magicArrowAnimation = new Animation("sprites/dangeon/mag_a.png", 20, 5);
        setAnimation(magicArrowAnimation);
        targetKnight = null;
    }

    public Knight getTargetKnight() {
        return targetKnight;
    }

    public void setTargetKnight(Knight knight) {
        this.targetKnight = knight;
    }

    private void pursueTarget() {
        if (targetKnight == null) return;

        float deltaX = getPosX() - targetKnight.getPosX();
        float deltaY = getPosY() - targetKnight.getPosY();

        if (!intersects(targetKnight)) {
            setPosition(
                getPosX() + (deltaX > 0 ? -3 : 3),
                getPosY() + (deltaY > 0 ? -3 : 3)
            );
        }

        Knight target = (Knight) getScene().getFirstActorByName("Ellen");
        if (target != null && intersects(target)) {
            target.getHealth().drain(10);
            getScene().removeActor(this);
        }
    }

    public void activate() {
        setTargetKnight(Objects.requireNonNull(getScene()).getFirstActorByType(Knight.class));
        new Loop<>(new Invoke<>(this::pursueTarget)).scheduleFor(this);
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        activate();
    }
}
