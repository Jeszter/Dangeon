package dangeon.ammunition;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.actions.Rotate;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import dangeon.characters.Knight;

import java.util.Objects;

public class Fire extends AbstractActor {
    private boolean isActive;
    private Knight target;
    private final Animation fireAnimation;

    public Fire() {
        fireAnimation = new Animation("sprites/dangeon/fire.png", 8, 8);
        setAnimation(fireAnimation);
        target = null;
        isActive = false;
    }

    public Knight getTarget() {
        return target;
    }

    public void setTarget(Knight target) {
        this.target = target;
    }

    private void followTarget() {
        if (target == null) return;

        float deltaX = getPosX() - target.getPosX();
        float deltaY = getPosY() - target.getPosY();
        float rotation = (float) (Math.toDegrees(Math.atan2(deltaY, deltaX)) + 90);

        if (deltaX < 0) {
            rotation += 180;
        }

        Knight player = (Knight) getScene().getFirstActorByName("Ellen");
        if (player == null) return;

        if (!intersects(target)) {
            int speed = 3;
            int newX = getPosX();
            int newY = getPosY();

            if (getPosX() > target.getPosX()) {
                newX -= speed;
            } else if (getPosX() < target.getPosX()) {
                newX += speed;
            }

            if (getPosY() > target.getPosY()) {
                newY -= speed;
            } else if (getPosY() < target.getPosY()) {
                newY += speed;
            }

            setPosition(newX, newY);
        }

        if (intersects(player)) {
            player.getHealth().drain(10);
            getScene().removeActor(this);
        }

        if (getAnimation().getRotation() != rotation) {
            new Rotate<>(rotation, 0.05f).scheduleFor(this);
        }
    }


    public void activate() {
        setTarget(Objects.requireNonNull(getScene()).getFirstActorByType(Knight.class));
        new Loop<>(new Invoke<>(this::followTarget)).scheduleFor(this);
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        activate();
    }
}
