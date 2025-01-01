package dangeon.trap;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.actions.Invoke;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.framework.actions.Loop;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import dangeon.characters.Knight;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class Boulder extends AbstractActor {
    private Knight knight;
    private boolean search;

    public Boulder() {
        Animation animation = new Animation("sprites/dangeon/Boulder.png", 13, 13, 0.2f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(animation);
        search = true;
    }

    public void searchKnight() {
        Ellipse2D.Float explosionArea = new Ellipse2D.Float(
            getPosX() - 100,
            getPosY() - 100,
            200,
            200
        );

        for (Actor actor : getScene().getActors()) {
            if (actor instanceof Knight) {
                Knight target = (Knight) getScene().getFirstActorByName("Ellen");
                Rectangle2D.Float knightBounds = new Rectangle2D.Float(
                    target.getPosX() - target.getWidth() / 2,
                    target.getPosY() - target.getHeight() / 2,
                    target.getWidth(),
                    target.getHeight()
                );

                if (search && explosionArea.intersects(knightBounds)) {
                    search = false;
                    searchAndDestroy();
                }
            }
        }
    }

    private void move() {
        if (knight != null) {
            setPosition(getPosX(), getPosY() - 2);
            MapTile currentTile = getScene().getMap().getTile(getPosX() / 16, getPosY() / 16);
            if (currentTile.getType() == MapTile.Type.WALL) {
                getScene().removeActor(this);
            }
        }

        if (knight != null) {
            Knight target = (Knight) getScene().getFirstActorByName("Ellen");
            if (target != null && intersects(target)) {
                target.getHealth().drain(25);
                getScene().removeActor(this);
            }
        }
    }

    public void searchAndDestroy() {
        setPlayer(Objects.requireNonNull(getScene()).getFirstActorByType(Knight.class));
        new Loop<>(new Invoke<>(this::move)).scheduleFor(this);
    }

    public void setPlayer(Knight knight) {
        this.knight = knight;
    }

    public boolean isSearching() {
        return search;
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        new Loop<>(new Invoke<>(this::searchKnight)).scheduleFor(this);
    }
}
