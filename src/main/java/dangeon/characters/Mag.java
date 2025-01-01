package dangeon.characters;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.ActionSequence;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.actions.Wait;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import dangeon.ammunition.MagicFire;
import dangeon.controllers.DangeonMovable;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class Mag extends AbstractActor implements DangeonMovable, Enemy {
    private boolean attack;
    private boolean seach;
    private Knight knight;
    private Health health;
    private boolean lastDirectionLeft;
    private Animation magattack;
    private Animation magattack2;
    private Animation magsummon;
    private Animation magsummon2;
    private Animation die;
    private Animation die2;
    private boolean lastDirectionRight;
    private boolean isSummoning;
    private boolean arrowSpawned;

    public Mag() {
        magattack = new Animation("sprites/dangeon/MagAttack.png", 36, 56, 0.1f, Animation.PlayMode.LOOP);
        magattack2 = new Animation("sprites/dangeon/MagAttack2.png", 36, 56, 0.1f, Animation.PlayMode.LOOP);
        magsummon = new Animation("sprites/dangeon/MagSummon.png", 36, 56, 0.1f, Animation.PlayMode.LOOP);
        magsummon2 = new Animation("sprites/dangeon/MagSummon2.png", 36, 56, 0.1f, Animation.PlayMode.LOOP);
        die = new Animation("sprites/dangeon/MagDie.png", 36, 56, 0.2f, Animation.PlayMode.ONCE);
        die2 = new Animation("sprites/dangeon/MagDie2.png", 36, 56, 0.2f, Animation.PlayMode.ONCE);
        setAnimation(magsummon);
        attack = false;
        seach = true;
        knight = null;
        health = new Health(1000, 1000);
        lastDirectionRight = true;
        isSummoning = false;
        arrowSpawned = false;
        health.onExhaustion(() -> {
            if (getScene() != null) {
                die();
            }
        });
    }
    public void die() {
        if (getScene() != null) {
            isSummoning = true;
            if(getAnimation() == magsummon2 || getAnimation() == magattack2){
                this.setAnimation(die2);
            }
            else
            {
                this.setAnimation(die);
            }
            new ActionSequence<>(
                new Wait<>(5f),
                new Invoke<>(() -> getScene().getGame().stop())
            ).scheduleFor(this);
        }
    }

    @Override
    public Health getHealth() {
        return health;
    }

    public int getHP() {
        return this.health.getValue();
    }

    public void seachKnight() {
        if (isSummoning) return;
        Ellipse2D.Float explosionArea = new Ellipse2D.Float(
            this.getPosX() - 200,
            this.getPosY() - 200,
            200 * 2,
            200 * 2
        );
        for (Actor actor : getScene().getActors()) {
            if (actor instanceof Knight) {
                Knight project = (Knight) getScene().getFirstActorByName("Ellen");
                Rectangle2D.Float Knight = new Rectangle2D.Float(
                    project.getPosX() - project.getWidth() / 2,
                    project.getPosY() - project.getHeight() / 2,
                    project.getWidth(),
                    project.getHeight()
                );
                if (seach && explosionArea.intersects(Knight)) {
                    seach = false;
                    searchAndDestroy();
                }
            }
        }
    }

    public void startMoving() {
        if (isSummoning) return;
        Ellipse2D.Float explosionArea = new Ellipse2D.Float(
            this.getPosX() - 100,
            this.getPosY() - 100,
            100 * 2,
            100 * 2
        );
        for (Actor actor : getScene().getActors()) {
            if (actor instanceof Knight) {
                Knight project = (Knight) getScene().getFirstActorByName("Ellen");
                Rectangle2D.Float Knight = new Rectangle2D.Float(
                    project.getPosX() - project.getWidth() / 2,
                    project.getPosY() - project.getHeight() / 2,
                    project.getWidth(),
                    project.getHeight()
                );
                if (!attack && explosionArea.intersects(Knight)) {
                    attack = true;
                    startSpawning();
                }
                if (!explosionArea.intersects(Knight)) {
                    attack = false;
                    Move();
                }
            }
        }
    }

    private void Move() {
        if (isSummoning) return;
        if (knight == null) return;
        Knight project = (Knight) getScene().getFirstActorByName("Ellen");
        if (project == null) return;

        float deltaX = getPosX() - knight.getPosX();
        float deltaY = getPosY() - knight.getPosY();

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                lastDirectionRight = false;
                setAnimation(magsummon);
            } else {
                lastDirectionRight = true;
                setAnimation(magsummon);
            }
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

            if (lastDirectionLeft && !isSummoning) {
                setAnimation(magattack);
            } else if(!isSummoning) {
                setAnimation(magattack2);
            }
        }
    }

    public void summonBat() {
        isSummoning = true;
        new ActionSequence<>(
            new Invoke<>(() -> {
                if(getAnimation() == magattack2 ) {
                    setAnimation(magsummon2);
                }
                if(getAnimation() == magattack ) {
                    setAnimation(magsummon);
                }
            }),
            new Wait<>(1),
            new Invoke<>(() -> {
                getScene().addActor(new SkullHead(), this.getPosX(), this.getPosY() - 20);
            }),
            new Wait<>(0.3f),
            new Invoke<>(() -> {
                getScene().addActor(new Bat(), this.getPosX() + 20, this.getPosY() - 20);
            }),
            new Wait<>(0.5f),
            new Invoke<>(() ->  {
                getHealth().drain(1);
                isSummoning =false;
            }),
            new Invoke<>(() -> {
                if(!isSummoning) {
                    setAnimation(lastDirectionRight ? magattack2 : magattack);
                    attack = true;
                    isSummoning = false;
                }
            })
        ).scheduleFor(this);
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

    public boolean getSeach() {
        return seach;
    }

    private void spawnArrow() {
        if (isSummoning) return;
        if (attack && !seach) {
            new ActionSequence<>(
                new Invoke<>(() -> {
                    if (lastDirectionRight && !isSummoning) {
                        setAnimation(magattack2);
                    } else if (!isSummoning) {
                        setAnimation(magattack);
                    }
                })
            ).scheduleFor(this);

            int currentFrame = getAnimation().getCurrentFrameIndex();
            if (currentFrame == 9 && !arrowSpawned) {
                getScene().addActor(new MagicFire(), this.getPosX(), this.getPosY() + 20);
                arrowSpawned = true;
            } else if (currentFrame != 9) {
                arrowSpawned = false;
            }

            if (this.health.getValue() == 700 || this.health.getValue() == 399) {
                summonBat();
            }
        }
    }

    public void startSpawning() {
        if (isSummoning) return;
        if (getScene() != null) {
            new Loop<>(
                new ActionSequence<>(
                    new Invoke<>(this::spawnArrow)
                )
            ).scheduleFor(this);
        }
    }

    @Override
    public void addedToScene(Scene scene) {

        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::seachKnight)).scheduleFor(this);
    }

    @Override
    public int getSpeed() {
        return 2;
    }
}
