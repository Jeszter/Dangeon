package dangeon.moves;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.Input;
import sk.tuke.kpi.gamelib.KeyboardListener;
import dangeon.characters.Enemy;
import dangeon.characters.Knight;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Attack implements KeyboardListener {
    private Knight knight;
    private boolean explosionActive;

    public Attack(Knight knight) {
        this.knight = knight;
        this.explosionActive = false;
    }

    @Override
    public void keyPressed(Input.Key key) {
        if (key == Input.Key.SPACE) {
            knight.performAttack();
            triggerExplosion();
        }
    }

    private void triggerExplosion() {
        Ellipse2D.Float explosionArea = new Ellipse2D.Float(
            knight.getPosX() - 36 / 2.0f,
            knight.getPosY() - 18 / 2.0f,
            36,
            18
        );


        int damage = 50;
        for (Actor actor : knight.getScene().getActors()) {
            if (actor instanceof Enemy && !(actor instanceof Knight)) {
                Rectangle2D.Float actorArea = new Rectangle2D.Float(
                    actor.getPosX() - actor.getWidth() / 2,
                    actor.getPosY() - actor.getHeight() / 2,
                    actor.getWidth(), actor.getHeight()
                );

                if (explosionArea.intersects(actorArea)) {
                    ((Enemy) actor).getHealth().drain(damage);
                }
            }
        }


    }
}
