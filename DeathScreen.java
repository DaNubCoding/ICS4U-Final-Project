import java.io.File;

import greenfoot.Color;
import greenfoot.GreenfootImage;

public class DeathScreen extends PixelWorld {
    private WorldData data;
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
        addSprite(new Text("Weapons discovered: " + data.getNumDicoveredWeapons() + " / " + Item.NAMES.size(),
                            Text.AnchorX.CENTER, Text.AnchorY.CENTER),
                            125, 160);
        File file = new File("saves/save_" + data.getSeed() + ".csv");
        file.delete();
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




