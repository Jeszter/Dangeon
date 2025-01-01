package dangeon.characters;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Disposable;
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

public class Slime extends AbstractActor implements DangeonMovable, Enemy {
    private Animation normalAnimation;
    private Animation attackAnimation;
    private Animation alternateNormalAnimation;
    private Animation alternateAttackAnimation;
    private boolean attack;
    private boolean search;
    private Knight knight;
    private Health health;
    private boolean lastDirectionRight;

    public Slime() {
        normalAnimation = new Animation("sprites/dangeon/slime2.png", 16, 16, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        attackAnimation = new Animation("sprites/dangeon/slimeAttack2.png", 16, 32, 0.1f, Animation.PlayMode.LOOP);
        alternateNormalAnimation = new Animation("sprites/dangeon/slime.png", 16, 16, 0.1f, Animation.PlayMode.LOOP_PINGPONG);
        alternateAttackAnimation = new Animation("sprites/dangeon/slimeAttack.png", 16, 32, 0.1f, Animation.PlayMode.LOOP);
        setAnimation(normalAnimation);

        attack = false;
        search = true;
        knight = null;
        health = new Health(100, 100);
        lastDirectionRight = true;
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
                Knight project = (Knight) getScene().getFirstActorByName("Ellen");
                Rectangle2D.Float knightBounds = new Rectangle2D.Float(
                    project.getPosX() - project.getWidth() / 2,
                    project.getPosY() - project.getHeight() / 2,
                    project.getWidth(),
                    project.getHeight()
                );
                if (search && explosionArea.intersects(knightBounds)) {
                    search = false;
                    searchAndDestroy();
                }
            }
        }
    }

    public void startMoving() {
        Ellipse2D.Float explosionArea = new Ellipse2D.Float(
            this.getPosX() - 8,
            this.getPosY() - 8,
            16,
            16
        );
        for (Actor actor : getScene().getActors()) {
            if (actor instanceof Knight) {
                Knight project = (Knight) getScene().getFirstActorByName("Ellen");
                Rectangle2D.Float knightBounds = new Rectangle2D.Float(
                    project.getPosX() - project.getWidth() / 2,
                    project.getPosY() - project.getHeight() / 2,
                    project.getWidth(),
                    project.getHeight()
                );

                if (!attack && explosionArea.intersects(knightBounds)) {
                    attack = true;
                    startSpawning();
                }
                if (!explosionArea.intersects(knightBounds)) {
                    attack = false;
                    move();
                }
            }
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

    private void move() {
        if (knight == null) {
            return;
        }

        float deltaX = getPosX() - knight.getPosX();
        float deltaY = getPosY() - knight.getPosY();

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                lastDirectionRight = false;
                setAnimation(normalAnimation);
            } else {
                lastDirectionRight = true;
                setAnimation(alternateNormalAnimation);
            }
        }

        if (getPosX() > knight.getPosX()) {
            setPosition(getPosX() - 1, getPosY());
        } else if (getPosX() < knight.getPosX()) {
            setPosition(getPosX() + 1, getPosY());
        }

        if (getPosY() > knight.getPosY()) {
            setPosition(getPosX(), getPosY() - 1);
        } else if (getPosY() < knight.getPosY()) {
            setPosition(getPosX(), getPosY() + 1);
        }
    }

    private void spawnArrow() {
        if (attack && !search) {
            Disposable disposable = new ActionSequence<>(
                new Invoke<>(() -> {
                    if (lastDirectionRight) {
                        setAnimation(alternateAttackAnimation);
                    } else {
                        setAnimation(attackAnimation);
                    }
                }),
                new Invoke<>(() -> {
                    knight.getHealth().drain(10);
                }),
                new Wait<>(0.45f),
                new Invoke<>(() -> {
                    setAnimation(lastDirectionRight ? alternateNormalAnimation : normalAnimation);
                })
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

