package dangeon.characters;

import java.util.ArrayList;
import java.util.List;

public class Health {
    private int valueHealth;
    private int maxHealth;
    private List<ExhaustionEffect> effects;

    public Health(int valueHealth, int maxHealth) {
        this.valueHealth = valueHealth;
        this.maxHealth = maxHealth;
        effects = new ArrayList<>();
    }

    public Health(int value) {
        this.valueHealth = this.maxHealth = value;
    }

    public int getValue() {
        return this.valueHealth;
    }

    public int getMaxValue() {
        return this.maxHealth;
    }

    public void refill (int amount) {
        if (valueHealth + amount <= maxHealth)
            valueHealth += amount;
        else
            restore();
    }

    public void restore() {
        valueHealth = maxHealth;
    }

    public void drain (int amount) {
        if ((this.valueHealth - amount) <= 0){
            exhaust();
        }else{
            valueHealth = (valueHealth - amount);
        }
    }

    public void exhaust() {
        if (valueHealth != 0) {
            valueHealth = 0;

            if (effects != null) {
                effects.forEach(ExhaustionEffect::apply);
            }
        }
    }

    @FunctionalInterface
    public interface ExhaustionEffect {
        void apply();
    }

    public void onExhaustion(ExhaustionEffect effect)
    {
        if (effects!=null)
            effects.add(effect);
    }





}
