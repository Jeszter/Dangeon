package dangeon.items;

import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import dangeon.Collectible;
import dangeon.doors.LockedDoor;

public class Key extends AbstractActor implements Usable<LockedDoor>, Collectible {

    public Key() {
        Animation card = new Animation("sprites/dangeon/key1.png", 16, 16,0.2f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(card);
    }

    @Override
    public void useWith(LockedDoor lockedDoor){
        if (lockedDoor == null) {
            return;
        }
        lockedDoor.End();
    }

    @Override
    public Class<LockedDoor> getUsingActorClass() {
        return LockedDoor.class;
    }

}
