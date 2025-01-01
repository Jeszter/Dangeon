package dangeon.doors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import sk.tuke.kpi.gamelib.Actor;

public class LockedDoor extends DangeonDoor {
    public static boolean fight ;
    private boolean locked;
    private boolean isLocked() {
        return locked;
    }
    public LockedDoor(String name, DangeonDoor.Orientation orientation){
        super(name,orientation);
        locked= true;
    }

    public void lock() {
        locked = true;
        this.close();
    }
    public void unlock() {
        locked = false;
        this.open();
    }

    public void End(){
        fight = true;
        locked = false;
        this.open();
        OrthographicCamera camera = (OrthographicCamera) getScene().getCamera();
        camera.position.set(245,1120,0.8f);
        camera.zoom = 0.8f;
        getScene().stopFollowing();
        camera.update();
    }

    @Override
    public void useWith(Actor actor) {
        if (!isLocked())
            super.useWith(actor);
    }

}
