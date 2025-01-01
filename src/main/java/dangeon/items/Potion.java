package dangeon.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import dangeon.characters.Knight;
import dangeon.Collectible;


public class Potion extends AbstractActor implements Usable<Knight> , Collectible {
    public Potion() {
        Animation energy = new Animation("sprites/dangeon/potion.png", 16, 16);
        setAnimation(energy);
    }

    @Override
    public void useWith(Knight knight){
        if (knight == null) {
            return;
        }
        knight.getHealth().refill(50);;
        getScene().removeActor(this);
    }

    @Override
    public Class<Knight> getUsingActorClass() {
        return Knight.class;
    }

}
