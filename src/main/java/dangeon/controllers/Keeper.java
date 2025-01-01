package dangeon.controllers;


import sk.tuke.kpi.gamelib.Actor;
import dangeon.items.Backpack;

public interface Keeper extends Actor {
    Backpack getBackpack();
}
