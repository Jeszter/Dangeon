package dangeon;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.map.MapTile;
import dangeon.characters.Knight;
import dangeon.items.Potion;
import dangeon.items.Usable;

public class Box extends AbstractActor implements Usable<Knight> {
    private boolean used;
    private Animation chest_open_1 = new Animation("sprites/dangeon/box.png", 16, 16, 0.2f, Animation.PlayMode.ONCE);

    public Box() {
        Animation chest_1 = new Animation("sprites/dangeon/box.png", 16, 16, 0.1f, Animation.PlayMode.LOOP);
        setAnimation(chest_1);
        used = false;
    }

    @Override
    public void useWith(Knight knight) {
        if (knight == null) {
            return;
        }

        if (!used) {
            used = true;
            getScene().addActor(new Potion(), getPosX(), getPosY() - 20);
            setAnimation(chest_open_1);
        }

        setWallTileUnderChest();
    }


    private void setWallTileUnderChest() {
        getScene().getMap().getTile(this.getPosX() /16, this.getPosY() / 16).setType(MapTile.Type.WALL);
    }

    @Override
    public void addedToScene(Scene scene) {
        super.addedToScene(scene);
        if(getScene() != null){
            getScene().getMap().getTile(this.getPosX() /16, this.getPosY() / 16).setType(MapTile.Type.WALL);
        }
    }


    @Override
    public Class<Knight> getUsingActorClass() {
        return Knight.class;
    }
}
