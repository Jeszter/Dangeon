package dangeon;

import sk.tuke.kpi.gamelib.*;
import sk.tuke.kpi.gamelib.backends.lwjgl.LwjglBackend;
import dangeon.stenarios.Dangeon;

public class DangeonMain {
    public static void main(String[] args) {
        WindowSetup windowSetup = new WindowSetup("Dangeon", 600, 400);
        Game game = new GameApplication(windowSetup, new LwjglBackend());
        //Scene dangeon = new InspectableScene(new World("dangeon.tmx", "maps/dangeon.tmx", new Dangeon.Factory()) , List.of("sk.tuke.kpi")) ;
        Scene dangeon = new World("dangeon.tmx", "maps/dangeon.tmx", new Dangeon.Factory()) ;
        //FirstSteps FirstSteps = new FirstSteps();
        Dangeon Dangeon = new Dangeon();
        game.addScene(dangeon);
        dangeon.addListener(Dangeon);
        game.start();
        game.getInput().onKeyPressed(Input.Key.ESCAPE, game::stop);
    }


}
