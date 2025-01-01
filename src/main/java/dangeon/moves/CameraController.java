package dangeon.moves;



import sk.tuke.kpi.gamelib.Scene;
import dangeon.doors.LockedDoor;
import dangeon.characters.Knight;

public class CameraController {
    private boolean follow;

    public CameraController() {
        this.follow = false;
    }

    public void toggleCamera(Scene scene) {
        if (LockedDoor.fight == false) {
            if (follow) {
                Knight ellen = scene.getFirstActorByType(Knight.class);
                if (ellen != null) {
                    scene.follow(ellen);
                } else {
                }
            } else {
                scene.stopFollowing();
            }
            follow = !follow;
        }
    }


}
