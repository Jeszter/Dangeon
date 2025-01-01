package dangeon.controllers;

import sk.tuke.kpi.gamelib.actions.Action;
import dangeon.characters.Knight;

public class DangeonMove<A extends DangeonMovable> implements Action<A> {
    private A actor;
    private boolean isDone;
    private boolean first = false;
    private DangeonDirection dangeonDirection;
    private float times;

    public DangeonMove(DangeonDirection dangeonDirection, float times) {
        this.dangeonDirection = dangeonDirection;
        this.times = times;
        isDone = false;
        first = false;
    }

    public A getActor() {
        return actor;
    }

    public void setActor(A actor) {
        this.actor = actor;
    }

    public boolean isDone() {
        return isDone;
    }

    public void reset() {
        actor.stoppedMoving();
        isDone = false;
        first = false;
        times = 0;
    }

    public void execute(float deltaTime) {

        if (actor instanceof Knight && ((Knight) actor).isAttacking()) {
            return;
        }

        if (actor != null) {
            times = times - deltaTime;
            first = true;
            if (first == true && !isDone) {
                actor.startedMoving(dangeonDirection);
            }
            actor.setPosition(actor.getPosX() + dangeonDirection.getDx() * actor.getSpeed(), actor.getPosY() + dangeonDirection.getDy() * actor.getSpeed());

            if ((actor.getScene()).getMap().intersectsWithWall(actor)) {
                actor.setPosition(actor.getPosX() - dangeonDirection.getDx() * actor.getSpeed(), actor.getPosY() - dangeonDirection.getDy() * actor.getSpeed());
            }

            if(Math.abs(deltaTime - this.times) <= 1e-5){
                this.stop();
                first = false;
            }
        }
    }

    public void stop() {
        isDone = true;
        actor.stoppedMoving();
    }
}
