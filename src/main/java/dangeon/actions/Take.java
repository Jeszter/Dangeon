package dangeon.actions;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import dangeon.controllers.Keeper;
import dangeon.Collectible;

import java.util.List;



public class Take<K extends Keeper> extends AbstractAction<K> {

    public Take() {

    }


    @Override
    public void  execute(float deltaTime){
        if (getActor() == null || getActor().getScene() == null) {
            setDone(true);
            return;
        }
        Scene scene = getActor().getScene();
        if (!isDone()) {
            List<Actor> item = getActor().getScene().getActors();
            for (Actor actors : item) {
                if (actors instanceof Collectible && actors.intersects(getActor())) {
                    try {
                        getActor().getBackpack().add((Collectible) actors);
                        getActor().getScene().removeActor(actors);
                    }  catch (Exception ex) {
                        scene.getOverlay().drawText(ex.getMessage(), 0, 0).showFor(2);
                    }
                }
            }
            setDone(true);

        }
    }
}

