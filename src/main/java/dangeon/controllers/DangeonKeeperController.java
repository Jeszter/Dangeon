package dangeon.controllers;

import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import dangeon.actions.Drop;
import dangeon.actions.Shift;
import dangeon.actions.Take;
import dangeon.actions.Use;
import dangeon.items.Usable;

public class DangeonKeeperController implements KeyboardListener {
    private Keeper keeper;
    public DangeonKeeperController(Keeper keeper) {
        this.keeper = keeper;
    }

    public void keyPressed(Input.Key key) {
        if (key == Input.Key.ENTER) {
            new Take<>().scheduleFor(keeper);
        }
        if (key == Input.Key.BACKSPACE) {
            new Drop<>().scheduleFor(keeper);
        }
        if (key == Input.Key.S) {
            new Shift<>().scheduleFor(keeper);
        }
        if (key == Input.Key.B) {
            keyB();
        }
        if (key == Input.Key.U) {
            keyU();
        }


    }

    public void keyB(){
        if (Usable.class.isInstance(keeper.getBackpack().peek())) {
            Use<?> use=new Use<>((Usable<?>)keeper.getBackpack().peek());
            use.scheduleForIntersectingWith(keeper);
        }
    }

    public void keyU() {
            Usable<?> usable = keeper.getScene().getActors().stream().filter(Usable.class::isInstance).filter(keeper::intersects).map(Usable.class::cast).findFirst().orElse(null);
            if (usable != null) {
                new Use<>(usable).scheduleForIntersectingWith(keeper);
            }
        }
    }

