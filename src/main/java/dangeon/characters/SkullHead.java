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
import dangeon.ammunition.Fire;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class SkullHead extends AbstractActor implements DangeonMovable, Enemy {
    private Animation skullAttack;
    private Animation skullAttack2;
    private boolean isAttacking;
    private boolean isSearching;
    private Knight knight;
    private Health health;
    private boolean lastDirectionLeft;

    public SkullHead() {
        skullAttack = new Animation("sprites/dangeon/skull_head.png", 16, 16, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        skullAttack2 = new Animation("sprites/dangeon/skull_head2.png", 16, 16, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(skullAttack);
        isAttacking = false;
        isSearching = true;
        knight = null;
        health = new Health(150, 150);
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
        return this.health.getValue();
    }

    public void searchKnight() {
        Ellipse2D.Float explosionArea = new Ellipse2D.Float(
            this.getPosX() - 100,
            this.getPosY() - 100,
            200,
            200
        );
        for (Actor actor : getScene().getActors()) {
            if (actor instanceof Knight) {
                Knight target = (Knight) getScene().getFirstActorByName("Ellen");
                Rectangle2D.Float knightArea = new Rectangle2D.Float(
                    target.getPosX() - target.getWidth() / 2,
                    target.getPosY() - target.getHeight() / 2,
                    target.getWidth(),
                    target.getHeight()
                );
                if (isSearching && explosionArea.intersects(knightArea)) {
                    isSearching = false;
                    searchAndDestroy();
                }
                if (!isSearching && explosionArea.intersects(knightArea)) {
                    return;
                }
            }
        }
    }

    public void startMoving() {
        Ellipse2D.Float explosionArea = new Ellipse2D.Float(
            this.getPosX() - 50,
            this.getPosY() - 50,
            100,
            100
        );
        for (Actor actor : getScene().getActors()) {
            if (actor instanceof Knight) {
                Knight target = (Knight) getScene().getFirstActorByName("Ellen");
                Rectangle2D.Float knightArea = new Rectangle2D.Float(
                    target.getPosX() - target.getWidth() / 2,
                    target.getPosY() - target.getHeight() / 2,
                    target.getWidth(),
                    target.getHeight()
                );

                if (!isAttacking && explosionArea.intersects(knightArea)) {
                    isAttacking = true;
                    startSpawning();
                }
                if (!explosionArea.intersects(knightArea)) {
                    isAttacking = false;
                    move();
                }
            }
        }
    }

    private void move() {
        if (knight == null) {
            return;
        }

        Knight target = (Knight) getScene().getFirstActorByName("Ellen");

        if (target == null) {
            return;
        }

        if (!intersects(knight)) {
            if (getPosX() > knight.getPosX()) {
                setPosition(getPosX() - 1, getPosY());
                lastDirectionLeft = true;
            } else if (getPosX() < knight.getPosX()) {
                setPosition(getPosX() + 1, getPosY());
                lastDirectionLeft = false;
            }

            if (getPosY() > knight.getPosY()) {
                setPosition(getPosX(), getPosY() - 1);
            } else if (getPosY() < knight.getPosY()) {
                setPosition(getPosX(), getPosY() + 1);
            }

            setAnimation(lastDirectionLeft ? skullAttack2 : skullAttack);
        }
    }

    public Knight getPlayer() {
        return knight;
    }

    public void setPlayer(Knight knight) {
        this.knight = knight;
    }

    public void searchAndDestroy() {
        setPlayer(Objects.requireNonNull(getScene()).getFirstActorByType(Knight.class));
        new Loop<>(new Invoke<>(this::startMoving)).scheduleFor(this);
    }

    public boolean isSearching() {
        return isSearching;
    }

    private boolean canShoot = true;

    private void resetCanShoot() {
        canShoot = true;
    }


    private void spawnArrow() {
        if (isAttacking && !isSearching && canShoot) {
            getScene().addActor(new Fire(), getPosX(), getPosY());
            canShoot = false;
            new ActionSequence<>(
                new Wait<>(1.2f),
                new Invoke<>(this::resetCanShoot)
            ).scheduleFor(this);
        }
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
        new Loop<>(new Invoke<>(this::searchKnight)).scheduleFor(this);
    }

    @Override
    public int getSpeed() {
        return 2;
    }
}
