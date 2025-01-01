package dangeon.characters;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.GameApplication;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import dangeon.controllers.Keeper;
import dangeon.controllers.DangeonDirection;
import dangeon.controllers.DangeonMovable;
import dangeon.moves.CameraController;
import dangeon.items.Backpack;

public class Knight extends AbstractActor implements DangeonMovable, Keeper {
    private boolean isDead;
    private boolean isAttacking;
    private int speed;
    private Health health;
    private Animation idleAnimation1;
    private Animation idleAnimation2;
    private Animation walkAnimation1;
    private Animation walkAnimation2;
    private Animation attackAnimation1;
    private Animation attackAnimation2;
    private Animation deathAnimation1;
    private Animation deathAnimation2;
    private Backpack backpack;
    private DangeonDirection lastDirection;
    private CameraController cameraController;

    public Knight() {
        super("Ellen");
        idleAnimation1 = new Animation("sprites/dangeon/Knight.png", 18, 18, 0.2f, Animation.PlayMode.LOOP_PINGPONG);
        idleAnimation2 = new Animation("sprites/dangeon/Knight2.png", 18, 18, 0.2f, Animation.PlayMode.LOOP_PINGPONG);
        walkAnimation1 = new Animation("sprites/dangeon/KnightWalk1.png", 18, 18, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        walkAnimation2 = new Animation("sprites/dangeon/KnightWalk2.png", 18, 18, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        attackAnimation1 = new Animation("sprites/dangeon/KnightAttack.png", 36, 18, 0.08f, Animation.PlayMode.LOOP);
        attackAnimation2 = new Animation("sprites/dangeon/KnightAttack2.png", 36, 18, 0.08f, Animation.PlayMode.LOOP);
        deathAnimation1 = new Animation("sprites/dangeon/KnightDead.png", 18, 18, 0.1f, Animation.PlayMode.ONCE);
        deathAnimation2 = new Animation("sprites/dangeon/KnightDead2.png", 18, 18, 0.1f, Animation.PlayMode.ONCE);
        setAnimation(idleAnimation1);

        speed = 2;
        health = new Health(100, 100);
        backpack = new Backpack("Knight's backpack", 10);
        cameraController = new CameraController();
        isDead = false;
        isAttacking = false;

        health.onExhaustion(this::die);
    }

    public CameraController getCameraController() {
        return cameraController;
    }

    public void setCameraController(CameraController controller) {
        this.cameraController = controller;
    }

    public void performAttack() {
        if (isAttacking || isDead) return;

        isAttacking = true;
        new ActionSequence<>(
            new Invoke<>(() -> toggleCamera()),
            new Invoke<>(() -> setAnimationForAttack()),
            new Wait<>(0.5f),
            new Invoke<>(() -> resetAnimationAfterAttack()),
            new Invoke<>(() -> toggleCamera()),
            new Invoke<>(() -> isAttacking = false)
        ).scheduleFor(this);
    }

    private void toggleCamera() {
        if (cameraController != null) {
            cameraController.toggleCamera(getScene());
        }
    }

    private void setAnimationForAttack() {
        if (lastDirection != null && lastDirection.getDx() == -1) {
            setAnimation(attackAnimation2);
        } else {
            setAnimation(attackAnimation1);
        }
    }

    private void resetAnimationAfterAttack() {
        if (lastDirection != null && lastDirection.getDx() == -1) {
            setAnimation(idleAnimation2);
        } else {
            setAnimation(idleAnimation1);
        }
    }

    public void switchToWalkAnimation() {
        setAnimation(walkAnimation2);
    }

    public void switchToIdleAnimation() {
        setAnimation(walkAnimation1);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public Health getHealth() {
        return health;
    }


    public Backpack getBackpack() {
        return backpack;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public void startedMoving(DangeonDirection direction) {
        if (isAttacking || isDead) return;

        lastDirection = direction;
        getAnimation().setRotation(direction.getAngle());
        if (direction.getDx() == -1) {
            switchToWalkAnimation();
        } else {
            switchToIdleAnimation();
        }
        getAnimation().play();
    }

    @Override
    public void stoppedMoving() {
        if (isAttacking || isDead) return;

        if (lastDirection != null) {
            if (lastDirection.getDx() == -1) {
                setAnimation(idleAnimation2);
            } else {
                setAnimation(idleAnimation1);
            }
        }
    }

    public void displayStatus() {
        int windowHeight = getScene().getGame().getWindowSetup().getHeight();
        int yTextPos = windowHeight - GameApplication.STATUS_LINE_OFFSET;
        getScene().getGame().getOverlay().drawText("| HP : " + health.getValue(), 100, yTextPos);
    }

    private void die() {
        if (health.getValue() <= 0) {
            isDead = true;
            isAttacking = true;

            if (getAnimation() == walkAnimation2 || getAnimation() == idleAnimation2 || getAnimation() == attackAnimation2) {
                setAnimation(deathAnimation2);
            } else {
                setAnimation(deathAnimation1);
            }

            if (getScene() != null) {
                new ActionSequence<Actor>(
                    new Wait<>(2f),
                    new Invoke<>(() -> getScene().getGame().stop())
                ).scheduleOn(getScene());
            }
        }
    }
}
