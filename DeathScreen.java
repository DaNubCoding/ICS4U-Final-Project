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
    private Timer delayTimer;
    public DeathScreen(WorldData worldData) {
        super(SprackWorld.WORLD_WIDTH, SprackWorld.WORLD_HEIGHT);
        data = worldData;
        addSprite(new Text("Damage Dealt: " + data.getPlayerDamageDone(),
                            Text.AnchorX.CENTER, Text.AnchorY.CENTER),
                            125, 80);
        addSprite(new Text("Damage Received: " + data.getPlayerDamageTaken(),
                            Text.AnchorX.CENTER, Text.AnchorY.CENTER),
                            125, 100);
        addSprite(new Text("Enemies Killed: " + data.getPlayerEnemiesKilled(),
                            Text.AnchorX.CENTER, Text.AnchorY.CENTER),
                            125, 120);
        addSprite(new Text("Time Played: " + data.getTimePlayed() / 60 + " seconds",
                            Text.AnchorX.CENTER, Text.AnchorY.CENTER),
                            125, 140);
        addSprite(new Text("Weapons discovered: " + data.getNumDicoveredWeapons() + " / " + Weapon.NAMES.size(),
                            Text.AnchorX.CENTER, Text.AnchorY.CENTER),
                            125, 160);
        File file = new File("saves/save_" + data.getSeed() + ".csv");
        file.delete();

        Music.stop();

        delayTimer = new Timer(120);
        // clear previous keys
        Greenfoot.getKey();
    }

    @Override
    public void update() {
        if (!delayTimer.ended()) Greenfoot.getKey(); // clear previous keys, again
        if (delayTimer.ended() && (Greenfoot.getKey() != null || Greenfoot.mouseClicked(null))) {
            Greenfoot.setWorld(new TitleWorld());
            return;
        }
        updateSprites();
        Timer.incrementAct();
    }

    @Override
    public void render() {
        GreenfootImage background = getCanvas();
        background.setColor(Color.GRAY);
        background.fill();
        renderSprites();
    }
}
