package dangeon.controllers;

import sk.tuke.kpi.gamelib.Actor;

public interface DangeonMovable extends Actor {
    int getSpeed();

    default void startedMoving(DangeonDirection dangeonDirection){}

    default void stoppedMoving() {}
}
