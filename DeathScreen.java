import java.io.File;

import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

/**
 * A world that is shown when the player dies, displaying their stats for that
 * world.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class DeathScreen extends PixelWorld {
    private WorldData data;
    public DeathScreen(WorldData worldData, Entity killer) {
        super(SprackWorld.WORLD_WIDTH, SprackWorld.WORLD_HEIGHT);

        addSprite(new Text("You were killed by a " + killer + "...",
                           Text.AnchorX.CENTER, Text.AnchorY.TOP,
                           new Color(172, 71, 71)),
                           getWidth() / 2, 25);

        data = worldData;
        addSprite(new Text("Damage Dealt: " + (int) data.getPlayerDamageDone(),
                           Text.AnchorX.CENTER, Text.AnchorY.CENTER),
                           125, 60);
        addSprite(new Text("Damage Received: " + (int) data.getPlayerDamageTaken(),
                           Text.AnchorX.CENTER, Text.AnchorY.CENTER),
                           125, 80);
        addSprite(new Text("Enemies Killed: " + data.getPlayerEnemiesKilled(),
                           Text.AnchorX.CENTER, Text.AnchorY.CENTER),
                           125, 100);
        long seconds = data.getTimePlayed() / 60;
        addSprite(new Text("Time Played: " + (seconds / 60) + ":" + (seconds % 60 < 10 ? "0" : "") + (seconds % 60),
                           Text.AnchorX.CENTER, Text.AnchorY.CENTER),
                           125, 120);
        addSprite(new Text("Weapons Discovered: " + data.getNumDicoveredWeapons() + " / " + Weapon.NAMES.size(),
                           Text.AnchorX.CENTER, Text.AnchorY.CENTER),
                           125, 140);
        addSprite(new Button("Back", () -> Greenfoot.setWorld(new TitleWorld())), getWidth() / 2, 170);
        File file = new File("saves/save_" + data.getSeed() + ".csv");
        file.delete();

        Music.stop();
    }

    @Override
    public void update() {
        updateSprites();
    }

    @Override
    public void render() {
        GreenfootImage background = getCanvas();
        background.setColor(Color.GRAY);
        background.fill();
        renderSprites();
    }
}
