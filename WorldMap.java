import java.util.HashMap;
import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

public class WorldMap extends PixelWorld {
    private SprackWorld initialWorld;
    private Color[][] map;

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

        // update instance variables
        initialWorld = oldWorld;
        map = new Color[2 * genRad + 1][2 * genRad + 1];

        // fetch features
        for (Vector2 v : surroundings.keySet()) {
            if(surroundings.get(v) == null) continue;
            Feature f = surroundings.get(v);
            v = v.subtract(playerPos);
            v = v.add(new Vector2(genRad, genRad));
            if(v.y > -1 && v.y < map.length && v.x > -1 && v.x < map.length)
                map[(int)v.y][(int)v.x] = colors.get(f.getClass());
        }

        // put entities
        for(Entity e : initialWorld.getEntitiesInRange(Vector3.fromXZ(playerPos), genRad * 20)) {
            if(e instanceof Player) continue;
            Vector3 v = e.getWorldPos().divide(20);
            v = v.subtract(Vector3.fromXZ(playerPos));
            v = v.add(new Vector3(genRad, 0, genRad));
            try {
                if(v.z > -1 && v.z < map.length && v.z > -1 && v.z < map.length)
                    map[(int)v.z][(int)v.x] = new Color(180, 20, 20);
            } catch (ArrayIndexOutOfBoundsException ex) {
                // do nothing if the entity is outside the map
            }
        }

        // put player
        map[genRad][genRad] = new Color(255, 10, 10);

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
        if(Greenfoot.isKeyDown("escape")) {
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
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != null) {
                    background.setColor(map[i][j]);
                    background.drawRect(CELL_SIZE * j + widAdj,
                                        CELL_SIZE * i + heiAdj,
                                        CELL_SIZE - 2,
                                        CELL_SIZE - 2);
                }
            }
        }
        background.setColor(new Color(180, 180, 180));
        background.drawRect(widAdj - 1, heiAdj - 1,
                            CELL_SIZE * map.length, CELL_SIZE * map.length);

        renderSprites();
    }
}
