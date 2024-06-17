import java.util.HashMap;
import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

/**
 * The world map that shows the player's surroundings.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class WorldMap extends PixelWorld {
    private SprackWorld initialWorld;
    private Color[][] map;
    private Color[][] itemMap;
    private Color[][] entityMap;

    private static final int CELL_SIZE = 5;
    private static final HashMap<Class<? extends Feature>, Color> colors = new HashMap<>();
    private static GreenfootImage flag = new GreenfootImage("small_flag.png");

    static {
        colors.put(Crate.class, new Color(230, 190, 70));
        colors.put(OakTree.class, new Color(20, 255, 20));
        colors.put(WillowTree.class, new Color(98, 253, 128));
        colors.put(Tombstone.class, new Color(99, 99, 99));
        colors.put(StatueCampfire.class, new Color(255, 110, 12));
        colors.put(PondSpawner.class, new Color(28, 163, 236));
        colors.put(DirtSpawner.class, new Color(88, 57, 39));
    }

    public WorldMap(SprackWorld oldWorld, WorldData worldData) {
        super(oldWorld.getWidth(), oldWorld.getHeight());

        worldData.saveData();

        // define variables
        int genRad = worldData.getGenerationRadius() - 4;
        Vector2 playerPos = worldData.getPlayerLocation();
        HashMap<Vector2, Feature> surroundings = worldData.getSurroundings();
        HashMap<Long, WorldData.ItemPosPair> items = worldData.getStoredItems();

        // update instance variables
        initialWorld = oldWorld;
        map = new Color[2 * genRad + 1][2 * genRad + 1];
        itemMap = new Color[2 * genRad + 1][2 * genRad + 1];
        entityMap = new Color[2 * genRad + 1][2 * genRad + 1];

        // fetch features
        for (Vector2 v : surroundings.keySet()) {
            if(surroundings.get(v) == null) continue;
            Feature f = surroundings.get(v);
            v = v.subtract(playerPos);
            v = v.add(new Vector2(genRad, genRad));
            if(v.y > -1 && v.y < map.length && v.x > -1 && v.x < map[0].length)
                map[(int)v.y][(int)v.x] = colors.get(f.getClass());
        }

        // put items
        for (long id : items.keySet()) {
            Vector2 v = items.get(id).pos;
            v = v.subtract(playerPos);
            v = v.add(new Vector2(genRad, genRad));
            if (v.y > -1 && v.y < itemMap.length && v.x > -1 && v.x < itemMap[0].length)
                itemMap[(int)v.y][(int)v.x] = new Color(160, 20, 160);
        }

        // put entities
        for (Entity e: initialWorld.getEntitiesInRange(Vector3.fromXZ(playerPos.multiply(20)), genRad * 20 * 1.5)) {
            if(e instanceof Player) continue;
            Vector2 v = new Vector2(e.getWorldPos().x, e.getWorldPos().z);
            v = v.divide(20);
            v = v.subtract(playerPos);
            v = v.add(new Vector2(genRad, genRad));
            if (v.y > -1 && v.y < entityMap.length && v.x > -1 && v.x < entityMap[0].length)
                entityMap[(int)v.y][(int)v.x] = new Color(160, 20, 20);
        }

        // put player
        entityMap[genRad][genRad] = new Color(255, 10, 10);

        // show coordinates
        String coords = "Coordinates:\n(" + (int) playerPos.x + ", " + (int) playerPos.y + ")";
        addSprite(new Text(coords,
                           Text.AnchorX.LEFT,
                           Text.AnchorY.CENTER,
                           new Color(180, 180, 180, 200)),
                           2, 50);

        applyAdditions();
        render();
        updateImage();

        // show seed
        String seed = "Seed: " + worldData.getSeed();
        addSprite(new Text(seed,
                           Text.AnchorX.LEFT,
                           Text.AnchorY.CENTER,
                           new Color(180, 180, 180, 200)),
                           2, 10);
    }

    @Override
    public void update() {
        if (Greenfoot.isKeyDown("escape")) {
            Greenfoot.setWorld(initialWorld);
        }
        updateSprites();
    }

    @Override
    public void render() {
        // the map
        GreenfootImage background = getCanvas();
        int widAdj = (initialWorld.getWidth() - CELL_SIZE * map.length) - 8;
        int heiAdj = (initialWorld.getHeight() - CELL_SIZE * map.length) / 2 + 8;
        background.setColor(new Color(56, 56, 56));
        background.fill();
        // feature map
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != null) {
                    background.setColor(map[i][j]);
                    background.drawRect(CELL_SIZE * j + widAdj,
                                        CELL_SIZE * i + heiAdj,
                                        CELL_SIZE - 1,
                                        CELL_SIZE - 1);
                }
            }
        }
        // entity map
        for (int i = 0; i < entityMap.length; i++) {
            for (int j = 0; j < entityMap[i].length; j++) {
                if (entityMap[i][j] != null) {
                    background.setColor(entityMap[i][j]);
                    background.drawRect(CELL_SIZE * j + widAdj + 1,
                                        CELL_SIZE * i + heiAdj + 1,
                                        CELL_SIZE - 3,
                                        CELL_SIZE - 3);
                }
            }
        }
        // item map
        for (int i = 0; i < itemMap.length; i++) {
            for (int j = 0; j < itemMap[i].length; j++) {
                if (itemMap[i][j] != null) {
                    background.setColor(itemMap[i][j]);
                    background.drawRect(CELL_SIZE * j + widAdj + 2,
                                        CELL_SIZE * i + heiAdj + 2,
                                        CELL_SIZE - 5,
                                        CELL_SIZE - 5);
                }
            }
        }

        background.setColor(new Color(180, 180, 180));
        background.drawRect(widAdj - 1, heiAdj - 1,
                            CELL_SIZE * map.length + 1, CELL_SIZE * map.length + 1);

        // waypoints
        for (Vector2 waypoint : initialWorld.getWorldData().getWaypoints()) {
            Vector2 v = waypoint.subtract(initialWorld.getWorldData().getPlayerLocation());
            v = v.add(new Vector2(map.length / 2, map.length / 2));
            if (v.y > -1 && v.y < map.length && v.x > -1 && v.x < map[0].length) {
                background.drawImage(flag, widAdj + CELL_SIZE * (int) v.x, heiAdj + CELL_SIZE * (int) v.y);
            } else {
                double x = Math.max(-1, Math.min(map.length, v.x));
                double y = Math.max(-1, Math.min(map.length, v.y));
                background.drawImage(flag, widAdj + CELL_SIZE * (int) x, heiAdj + CELL_SIZE * (int) y);
            }
        }

        background.setColor(new Color(255, 255, 255, 100));
        int endX = widAdj + CELL_SIZE * map.length / 2;
        int endY = heiAdj + CELL_SIZE * map.length / 2;
        endX += (int) (Math.cos(Math.toRadians(Camera.getRotation() - 90)) * 24);
        endY += (int) (Math.sin(Math.toRadians(Camera.getRotation() - 90)) * 24);
        background.drawLine(widAdj + CELL_SIZE * map.length / 2,
                            heiAdj + CELL_SIZE * map.length / 2,
                            endX, endY);

        renderSprites();
    }
}
