package dangeon.characters;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import dangeon.controllers.DangeonMovable;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class Bat extends AbstractActor implements DangeonMovable, Enemy {
    private Animation animationIdle;
    private Animation animationIdleAlt;
    private Animation animationAttack;
    private Animation animationAttackAlt;
    private boolean isAttacking;
    private boolean isSearching;
    private Knight targetKnight;
    private Health health;
    private boolean lastFacingRight;

    public Bat() {
        animationIdle = new Animation("sprites/dangeon/bat.png", 35, 35, 0.1f, Animation.PlayMode.LOOP);
        animationAttack = new Animation("sprites/dangeon/batattack.png", 35, 35, 0.1f, Animation.PlayMode.LOOP);
        animationIdleAlt = new Animation("sprites/dangeon/bat2.png", 35, 35, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        animationAttackAlt = new Animation("sprites/dangeon/batattack2.png", 35, 35, 0.1f, Animation.PlayMode.LOOP);

        setAnimation(animationIdle);
        isAttacking = false;
        isSearching = true;
        targetKnight = null;
        health = new Health(150, 150);
        lastFacingRight = true;

        health.onExhaustion(() -> {
            if (getScene() != null) {
                getScene().removeActor(this);
            }
        });
    }

    @Override
    public Health getHealth() {
        return health;
    }

    public int getHP() {
        return health.getValue();
    }

    private void searchForKnight() {
        Ellipse2D.Float searchArea = new Ellipse2D.Float(
            getPosX() - 100,
            getPosY() - 100,
            200,
            200
        );

        for (Actor actor : getScene().getActors()) {
            if (actor instanceof Knight) {
                Knight knight = (Knight) getScene().getFirstActorByName("Ellen");
                Rectangle2D.Float knightBounds = new Rectangle2D.Float(
                    knight.getPosX() - knight.getWidth() / 2,
                    knight.getPosY() - knight.getHeight() / 2,
                    knight.getWidth(),
                    knight.getHeight()
                );

                if (isSearching && searchArea.intersects(knightBounds)) {
                    isSearching = false;
                    initiateAttack();
                }
            }
        }
    }

    private void startMoving() {
        Ellipse2D.Float attackRange = new Ellipse2D.Float(
            getPosX() - 8,
            getPosY() - 8,
            16,
            16
        );

        for (Actor actor : getScene().getActors()) {
            if (actor instanceof Knight) {
                Knight knight = (Knight) getScene().getFirstActorByName("Ellen");
                Rectangle2D.Float knightBounds = new Rectangle2D.Float(
                    knight.getPosX() - knight.getWidth() / 2,
                    knight.getPosY() - knight.getHeight() / 2,
                    knight.getWidth(),
                    knight.getHeight()
                );

                if (!isAttacking && attackRange.intersects(knightBounds)) {
                    isAttacking = true;
                    startShooting();
                } else if (!attackRange.intersects(knightBounds)) {
                    isAttacking = false;
                    moveTowardsTarget();
                }
            }
        }
    }

    public Knight getTargetKnight() {
        return targetKnight;
    }

    public void setTargetKnight(Knight knight) {
        this.targetKnight = knight;
    }

    private void initiateAttack() {
        setTargetKnight(Objects.requireNonNull(getScene()).getFirstActorByType(Knight.class));
        new Loop<>(new Invoke<>(this::startMoving)).scheduleFor(this);
    }

    private void moveTowardsTarget() {
        if (targetKnight == null) return;

        float deltaX = getPosX() - targetKnight.getPosX();
        float deltaY = getPosY() - targetKnight.getPosY();

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                lastFacingRight = false;
                setAnimation(animationIdle);
            } else {
                lastFacingRight = true;
                setAnimation(animationIdleAlt);
            }
        }

        if (getPosX() > targetKnight.getPosX()) setPosition(getPosX() - 1, getPosY());
        else if (getPosX() < targetKnight.getPosX()) setPosition(getPosX() + 1, getPosY());

        if (getPosY() > targetKnight.getPosY()) setPosition(getPosX(), getPosY() - 1);
        else if (getPosY() < targetKnight.getPosY()) setPosition(getPosX(), getPosY() + 1);
    }

    private void spawnAttack() {
        if (isAttacking && !isSearching) {
            new ActionSequence<>(
                new Invoke<>(() -> {
                    setAnimation(lastFacingRight ? animationAttackAlt : animationAttack);
                    new Wait<>(0.8f);
                }),
                new Invoke<>(() -> targetKnight.getHealth().drain(10)),
                new Wait<>(0.45f),
                new Invoke<>(() -> setAnimation(lastFacingRight ? animationIdleAlt : animationIdle))
            ).scheduleFor(this);
        }
    }

    private void startShooting() {
        if (getScene() != null) {
            new Loop<>(
                new ActionSequence<>(
                    new Invoke<>(this::spawnAttack),
                    new Wait<>(1.2f)
                )
            ).scheduleFor(this);
        }
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::searchForKnight)).scheduleFor(this);
    }

    @Override
    public int getSpeed() {
        return 2;
    }
}
