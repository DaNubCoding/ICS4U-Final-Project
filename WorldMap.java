import java.util.HashMap;
import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

public class WorldMap extends PixelWorld {
    private SprackWorld initialWorld;
    private Color[][] map;
    private Color[][] itemMap;
    private Color[][] entityMap;

    private static final int CELL_SIZE = 5;
    private static final HashMap<Class<? extends Feature>, Color> colors = new HashMap<>();

    static {
        colors.put(Crate.class, new Color(230, 190, 70));
        colors.put(OakTree.class, new Color(20, 255, 20));
        colors.put(WillowTree.class, new Color(98, 253, 128));
        colors.put(Tombstone.class, new Color(99, 99, 99));
    }

    public WorldMap(SprackWorld oldWorld, WorldData worldData) {
        super(oldWorld.getWidth(), oldWorld.getHeight());

        worldData.saveData();

        // define variables
        int genRad = worldData.getGenerationRadius();
        Vector2 playerPos = worldData.getPlayerLocation();
        HashMap<Vector2, Feature> surroundings = worldData.getSurroundings();
        HashMap<Vector2, Item> items = worldData.getStoredItems();

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
        for (Vector2 v : items.keySet()) {
            v = v.subtract(playerPos);
            v = v.add(new Vector2(genRad, genRad));
            if (v.y > -1 && v.y < itemMap.length && v.x > -1 && v.x < itemMap[0].length)
                itemMap[(int)v.y][(int)v.x] = new Color(160, 20, 160);
        }

        // put entities
        for (Entity e: initialWorld.getEntitiesInRange(Vector3.fromXZ(playerPos.multiply(20)), genRad * 20)) {
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
                           Text.AnchorX.CENTER,
                           Text.AnchorY.CENTER,
                           new Color(180, 180, 180)),
                         50, 50);
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
        int widAdj = (initialWorld.getWidth() - CELL_SIZE * map.length) - 10;
        int heiAdj = (initialWorld.getHeight() - CELL_SIZE * map.length) / 2;
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

        renderSprites();
    }
}
