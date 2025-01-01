package dangeon.items;

import sk.tuke.kpi.gamelib.ActorContainer;
import dangeon.Collectible;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Backpack implements ActorContainer<Collectible> {
    private String name;
    private int capacity;
    private List<Collectible> inBpk = new ArrayList<>();

    public Backpack(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public List<Collectible> getContent() {
        return List.copyOf(inBpk);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSize() {
        return inBpk.size();
    }

    public String getName() {
        return name;
    }

    public void add(Collectible actor) {
        if (inBpk.size() < getCapacity()) {
            inBpk.add(actor);
        } else {
            throw new IllegalStateException(getName() + "is full");
        }
    }


    public void remove(Collectible actor) {
        inBpk.remove(actor);
    }

    public Collectible peek() {
        if (getSize() > 0) {
            return inBpk.get(getSize() - 1);
        }
        return null;
    }

    public void shift() {
        Collections.rotate(inBpk,1);
    }

    public Iterator<Collectible> iterator() {
        return inBpk.iterator();
    }


}
