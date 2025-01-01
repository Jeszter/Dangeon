package dangeon.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import dangeon.controllers.Keeper;
import dangeon.Collectible;

public class Drop<A extends Keeper> extends AbstractAction<A> {

    @Override
    public void execute(float deltaTime) {
        if (getActor() == null || getActor().getScene() == null || getActor().getBackpack().peek() == null) {
            setDone(true);
            return;
        }

        Collectible item = getActor().getBackpack().peek();
        if (item != null) {
            int itemX = getActor().getPosX() + (getActor().getWidth() - item.getWidth()) / 2;
            int itemY = getActor().getPosY() + (getActor().getHeight() - item.getHeight()) / 2;

            getActor().getScene().addActor(item, itemX, itemY);
            getActor().getBackpack().remove(item);
        }

        setDone(true);
    }
}

