package dangeon.stenarios;

import dangeon.trap.Boulder;
import dangeon.trap.Dispenser;
import dangeon.trap.FireTrap;
import dangeon.trap.Peaks;
import sk.tuke.kpi.gamelib.*;
import dangeon.Box;
import dangeon.doors.LockedDoor;
import dangeon.characters.Mag;
import dangeon.characters.Slime;
import dangeon.controllers.DangeonKeeperController;
import dangeon.controllers.DangeonMovableController;
import dangeon.ChestWithKey;
import dangeon.doors.DangeonDoor;
import dangeon.characters.Knight;
import dangeon.characters.SkullHead;
import dangeon.moves.Attack;
import dangeon.trap.*;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class Dangeon implements SceneListener {

    public static class Factory implements ActorFactory {
        public Actor create(String type, String name) {
            assert name != null;
            if (name.equals("priest")) {
                return new Knight();
            }
            if (name.equals("arrow")) {
                return new Dispenser();
            }
            if (name.equals("pik")) {
                return new Peaks();
            }
            if (name.equals("fire_1")) {
                return new FireTrap("FireTrap",FireTrap.Orientation.VERTICAL);
            }
            if (name.equals("fire_2")) {
                return new FireTrap("FireTrap",FireTrap.Orientation.HORIZONTAL);
            }
            if (name.equals("chest_1")) {
                return new ChestWithKey();
            }
            if (name.equals("box")) {
                return new Box();
            }
            if (name.equals("boulder")) {
                return new Boulder();
            }
            if (name.equals("skullhead")) {
                return new SkullHead();
            }
            if (name.equals("slime")) {
                return new Slime();
            }
            if (name.equals("boss")) {
                return new Mag();
            }
            if (name.equals("DoorH")) {
                return new DangeonDoor("DoorH",DangeonDoor.Orientation.HORIZONTAL);
            }
            if (name.equals("DoorV")) {
                return new DangeonDoor("DoorV",DangeonDoor.Orientation.VERTICAL);
            }
            if (name.equals("LDoorV")) {
                return new LockedDoor("DoorV", LockedDoor.Orientation.VERTICAL);
            }



            return null;
        }
    }


    @Override
    public void sceneInitialized(Scene scene) {
        Knight ellen = scene.getFirstActorByType(Knight.class);
        scene.follow(ellen);





        DangeonMovableController dangeonMovableController = new DangeonMovableController(ellen);
        Disposable movController = scene.getInput().registerListener(dangeonMovableController);
        DangeonKeeperController dangeonKeeperController = new DangeonKeeperController(ellen);
        Disposable colController = scene.getInput().registerListener(dangeonKeeperController);
        scene.getGame().pushActorContainer(ellen.getBackpack());
        ellen.getBackpack().shift();
        Disposable attack = scene.getInput().registerListener(new Attack(ellen));


    }

    @Override
    public void sceneUpdating(Scene scene) {
        Knight ellen = scene.getFirstActorByType(Knight.class);
        ellen.displayStatus();

        OrthographicCamera camera = (OrthographicCamera) scene.getCamera();
        if (camera != null) {
            if(LockedDoor.fight == false){
                camera.zoom = 0.3f;

                camera.update();
            }
        }
    }
}
