package dangeon.actions;

import sk.tuke.kpi.gamelib.framework.actions.AbstractAction;
import dangeon.controllers.Keeper;

public class Shift<A extends Keeper> extends AbstractAction<A> {
    private Keeper keeper;
    public Shift(){
        this.keeper = keeper;
    }

    public void execute(float deltaTime) {
        if(!isDone() && getActor() != null) {
                getActor().getBackpack().shift();
                setDone(true);
        }
        else {
            setDone(true);
        }
    }
}
