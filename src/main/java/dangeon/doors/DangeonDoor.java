package dangeon.doors;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import dangeon.items.Usable;

public class DangeonDoor extends AbstractActor implements Openable, Usable<Actor> {
    private Animation door;
    private boolean opened;
    private Orientation orientation;
    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }
    public DangeonDoor(String name, Orientation orientation){
        //door = new Animation("sprites/vdoor.png", 16, 32, 0.1f, Animation.PlayMode.ONCE);
        updateAnimation(orientation);
        setAnimation(door);
        door.stop();
        opened = false;
        this.orientation = orientation;
    }

    private void updateAnimation(Orientation orientation){
        if (orientation == Orientation.VERTICAL) {
            door = new Animation("sprites/dangeon/vdoordaneon.png", 16, 32, 0.1f, Animation.PlayMode.ONCE);
        }if (orientation == Orientation.HORIZONTAL) {
            door = new Animation("sprites/dangeon/hdoordaneon.png", 32, 16, 0.1f, Animation.PlayMode.ONCE);
        }
    }


    @Override
    public void useWith(Actor actor) {
        if (opened == true){
            close();
        }else{
            open();
        }
    }


    public void close() {
        door.setPlayMode(Animation.PlayMode.ONCE);
        door.play();
        door.stop();
        opened = false;


        getScene().getMap().getTile(this.getPosX() / 16, this.getPosY() / 16).setType(MapTile.Type.WALL);
        if (orientation == Orientation.VERTICAL) {
            getScene().getMap().getTile(this.getPosX() / 16, (this.getPosY() / 16) + 1).setType(MapTile.Type.WALL);

        } else if (orientation == Orientation.HORIZONTAL) {
            getScene().getMap().getTile(this.getPosX() / 16 + 1, this.getPosY() / 16 + 1).setType(MapTile.Type.WALL);
            getScene().getMap().getTile(this.getPosX() / 16 + 2, this.getPosY() / 16 + 1 ).setType(MapTile.Type.WALL);
        }
    }
    public boolean isOpen() {
        return opened;
    }



    public void open() {
        door.setPlayMode(Animation.PlayMode.ONCE_REVERSED);
        door.play();
        door.stop();
        opened = true;


        getScene().getMap().getTile(this.getPosX() / 16, this.getPosY() / 16).setType(MapTile.Type.CLEAR);
        if (orientation == Orientation.VERTICAL) {
            getScene().getMap().getTile(this.getPosX() / 16, (this.getPosY() / 16) + 1).setType(MapTile.Type.CLEAR);
        } else if (orientation == Orientation.HORIZONTAL) {
            getScene().getMap().getTile(this.getPosX() / 16 + 1, this.getPosY() / 16 + 1).setType(MapTile.Type.CLEAR);
            getScene().getMap().getTile(this.getPosX() / 16 + 2, this.getPosY() / 16 + 1 ).setType(MapTile.Type.CLEAR);
        }


        getAnimation().setPlayMode(Animation.PlayMode.ONCE_REVERSED);
        getAnimation().play();
        getAnimation().stop();
    }



    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        if(getScene() != null){
            getScene().getMap().getTile(this.getPosX() / 16, this.getPosY() / 16).setType(MapTile.Type.WALL);
            if (orientation == Orientation.VERTICAL) {
                getScene().getMap().getTile(this.getPosX() / 16, this.getPosY() / 16 + 1).setType(MapTile.Type.WALL);
            }
            if (orientation == Orientation.HORIZONTAL) {
                getScene().getMap().getTile(this.getPosX() / 16 + 1, this.getPosY() / 16 + 1).setType(MapTile.Type.WALL);
                getScene().getMap().getTile(this.getPosX() / 16 + 2, this.getPosY() / 16 + 1 ).setType(MapTile.Type.WALL);
            }
        }
    }

    @Override
    public Class<Actor> getUsingActorClass() {
        return Actor.class;
    }
}
