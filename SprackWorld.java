import greenfoot.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Random;

/**
 * The main world of the game.
 * <p>
 * List of features that we would like you to take note :)
 * <ul>
 * <li>Try and collect 5 Jester Swords, and put them in the same place, we'll let the rest play out :)</li>
 * <li>Try and collect 5 Pearl Guns, same idea!</li>
 * <li>Use the map and waypoints to help you remember where you left your items!</li>
 * <li>Full seed consistency!</li>
 * <li>Chopping trees??? This ain't Minecraft!</li>
 * <li>
 * IMPORTANT: This game is VERY memory intensive due to the cached images of the
 * sprite stacks, and may run out of memory at times, if this happens, please
 * restart Greenfoot. If the game refuses to start due to memory issues, please
 * head to {@link SprackView} and change the value of {@code IMAGE_CACHE_ANGLE_COUNT}
 * to a lower value.
 *
 * @author Martin Baldwin
 * @author Lucas Fu
 * @author Andrew Wang
 * @version May 2024
 */
public class SprackWorld extends PixelWorld {
    private WorldData worldData;
    private Player player;
    private List<Damage> damages;
    private List<CollisionController> collisionControllers;
    private boolean bFlag = false;
    private Timer saveTimer = new Timer(1800);

    // world information
    public static final int WORLD_WIDTH = 256;
    public static final int WORLD_HEIGHT = 196;

    /**
     * Create a new SprackWorld.
     *
     * @param seeded whether to generate a random seed
     * @param seed the seed that is to be used, if any
     */
    public SprackWorld(boolean seeded, long seed) {
        super(WORLD_WIDTH, WORLD_HEIGHT);

        damages = new ArrayList<>();
        collisionControllers = new ArrayList<>();

        Cluster.clearClusters();

        if (seeded) {
            seed = new Random().nextLong();
        }
        worldData = new WorldData(seed);

        Vector2 playerPos = worldData.getPlayerLocation();
        player = new Player();
        player.setHealth(worldData.getPlayerHp());
        player.setArmor(worldData.getPlayerArmor());
        addWorldObject(player, playerPos.x * 20, 0, playerPos.y * 20);
        applyAdditions();

        Camera.resetTo(playerPos.x * 20, 0, playerPos.y * 20, 0, 1);
        Camera.setCloseness(0.3);

        worldData.generateWorld();
        for (Vector2 coord : worldData.getSurroundings().keySet()) {
            if (worldData.getSurroundings().get(coord) != null) {
                final int x = (int) coord.x * 20, z = (int) coord.y * 20;
                addWorldObject(worldData.getSurroundings().get(coord), x, 0, z);
            }
        }
        for(Item i : worldData.getHotbar()) {
            addWorldObject(i, player.getWorldPos());
            i.setPlayer(player);
            i.takeOffGround();
            player.pickupItem(i);
        }

        addSprite(new HotBar(), 0, 0);

        applyAdditions();
        render();
        updateImage();
        updateElements();
    }

    @Override
    public void stopped() {
        super.stopped();
        worldData.saveData();
    }

    @Override
    public void update() {
        Music.set("main_music.wav");
        List<? extends Sprite> spracks = getSpritesByLayer(Layer.SPRACK_DEFAULT);
        spracks.sort(Comparator.comparing(Sprite::getSortValue));
        spracks = getSpritesByLayer(Layer.SPRACK_CANOPY);
        spracks.sort(Comparator.comparing(Sprite::getSortValue));

        updateDamages();

        updateSprites();

        updateCollision();

        updateSurroundings();

        if (Greenfoot.isKeyDown("m")) {
            Greenfoot.setWorld(new WorldMap(this, worldData));
        }

        if (Greenfoot.isKeyDown("b") && !bFlag) {
            Vector2 playerPos = player.getWorldPos().xz.divide(20);
            Vector2 found = null;
            for (Vector2 waypoint : worldData.getWaypoints()) {
                if (waypoint.distanceTo(playerPos) < 5) {
                    found = waypoint;
                    break;
                }
            }
            if (found == null) {
                worldData.addWaypoint(playerPos);
            } else {
                worldData.removeWaypoint(found);
            }
            bFlag = true;
        } else if (bFlag && !Greenfoot.isKeyDown("b")) {
            bFlag = false;
        }

        if (saveTimer.ended()) {
            worldData.saveData();
            saveTimer.restart();
        }

        Timer.incrementAct();
        worldData.incrementTimePlayed();
    }

    private void updateSurroundings() {
        int cameraGridX = (int) (Camera.getX() / 20);
        int cameraGridZ = (int) (Camera.getZ() / 20);
        if (worldData.updatePlayerLocation(cameraGridX, cameraGridZ)) {
            updateElements();
        }
    }

    private void updateElements() {
        List<? extends Sprite> features = getSprites(Feature.class);
        Vector2 playerPos = worldData.getPlayerLocation();
        int genRad = worldData.getGenerationRadius();
        // remove objects not present in world data
        for (Sprite sprite : features) {
            Feature feature = (Feature) sprite;
            if (!worldData.getSurroundings().containsValue(feature)) {
                removeSprite(feature);
            }
        }
        // add objects not already in world
        for (Vector2 coord : worldData.getSurroundings().keySet()) {
            if (worldData.getSurroundings().get(coord) != null
            && !features.contains(worldData.getSurroundings().get(coord))) {
                final int x = (int) coord.x * 20, z = (int) coord.y * 20;
                addWorldObject(worldData.getSurroundings().get(coord), x, 0, z);
            }
        }
        // remove offscreen items and add onscreen items
        List<? extends Sprite> items = getSprites(Item.class);
        for (long id : worldData.getStoredItems().keySet()) {
            Item i = worldData.getStoredItems().get(id).item;
            Vector2 pos = worldData.getStoredItems().get(id).pos;
            // remove if offscreen
            if (pos.distanceTo(playerPos) > genRad && items.contains(i)) {
                removeSprite(i);
            }
            // add if onscreen
            if (pos.distanceTo(playerPos) <= genRad && !items.contains(i)) {
                addWorldObject(i, Vector3.fromXZ(pos).multiply(20));
            }
        }

        // refresh all entities
        List<? extends Sprite> entities = getSprites(Entity.class);
        // add all entities without repeating
        for (long id : worldData.getStoredEntities().keySet()) {
            Entity e = worldData.getStoredEntities().get(id).entity;
            Vector2 pos = worldData.getStoredEntities().get(id).pos;
            if(!entities.contains(e)) {
                addWorldObject(e, Vector3.fromXZ(pos).multiply(20));
            }
        }
        // force addition
        applyAdditions();
        // store all entities
        worldData.getStoredEntities().clear();
        entities = getSprites(Entity.class);
        for (Sprite s : entities) {
            Entity e = (Entity) s;
            Vector3 v = e.getWorldPos();
            worldData.storeEntity(new Vector2(v.x / 20, v.z / 20), e);
        }
        // unload all entities offscreen
        for (long id : worldData.getStoredEntities().keySet()) {
            Entity e = worldData.getStoredEntities().get(id).entity;
            Vector2 pos = worldData.getStoredEntities().get(id).pos;
            if (pos.distanceTo(worldData.getPlayerLocation()) > genRad) {
                removeSprite(e);
            }
        }
        applyRemovals();
    }

    private void updateDamages() {
        List<Damage> removals = new ArrayList<>();
        for (Damage damage : damages) {
            damage.update();
            if (damage.isRemoved()) {
                removals.add(damage);
            }
        }
        damages.removeAll(removals);
    }

    private void updateCollision() {
        List<CollisionController> removals = new ArrayList<>();
        for (CollisionController controller : collisionControllers) {
            controller.update();
            if (controller.isRemoved()) {
                removals.add(controller);
            }
        }
        collisionControllers.removeAll(removals);
    }

    @Override
    public void render() {
        GreenfootImage background = getCanvas();
        background.setColor(new Color(182, 189, 160));
        background.fill();
        renderSprites();
    }

    /**
     * Add a WorldObject to the world at the given world position.
     * <p>
     * This is equivalent to calling {@link #addSprite(Sprite, int, int)}
     * followed by {@link WorldObject#setWorldPos(double, double, double)}.
     *
     * @param worldObject the WorldObject to add
     * @param x the x position
     * @param y the y position
     * @param z the z position
     */
    public void addWorldObject(WorldObject object, double x, double y, double z) {
        addSprite((Sprite) object, 0, 0);
        object.setWorldPos(x, y, z);
    }

    /**
     * Add a WorldObject to the world at the given world position.
     * <p>
     * This is equivalent to calling {@link #addSprite(Sprite, int, int)}
     * followed by {@link WorldObject#setWorldPos(Vector3)}.
     *
     * @param object the WorldObject to add
     * @param pos the position
     */
    public void addWorldObject(WorldObject object, Vector3 pos) {
        addWorldObject(object, pos.x, pos.y, pos.z);
    }

    /**
     * Get all Spracks within a certain range of a center point as a stream.
     * <p>
     * You generally do not need to call this method. Instead, use the
     * {@link #getSpracksInRange(Vector3, double)} method to get a list of
     * Spracks within a range.
     *
     * @param center the center point
     * @param range the range
     * @return a stream of Spracks within the range
     */
    public Stream<Sprack> getSpracksInRangeAsStream(Vector3 center, double range) {
        return getSpritesAsStream(Sprack.class)
            .map(sprack -> (Sprack) sprack)
            .filter(sprack -> sprack.getWorldPos().distanceTo(center) < range);
    }

    /**
     * Get all Spracks within a certain range of a center point.
     *
     * @param center the center point
     * @param range the range
     * @return a list of Spracks within the range
     */
    public List<Sprack> getSpracksInRange(Vector3 center, double range) {
        return getSpracksInRangeAsStream(center, range).collect(Collectors.toList());
    }

    /**
     * Get all WorldSprites within a certain range of a center point as a stream.
     * <p>
     * You generally do not need to call this method. Instead, use the
     * {@link #getWorldSpritesInRange(Vector3, double)} method to get a list of
     * WorldSprites within a range.
     *
     * @param center the center point
     * @param range the range
     * @return a stream of WorldSprites within the range
     */
    public Stream<WorldSprite> getWorldSpritesInRangeAsStream(Vector3 center, double range) {
        return getSpritesAsStream(WorldSprite.class)
            .map(sprite -> (WorldSprite) sprite)
            .filter(sprite -> sprite.getWorldPos().distanceTo(center) < range);
    }

    /**
     * Get all WorldSprites within a certain range of a center point.
     *
     * @param center the center point
     * @param range the range
     * @return a list of WorldSprites within the range
     */
    public List<WorldSprite> getWorldSpritesInRange(Vector3 center, double range) {
        return getWorldSpritesInRangeAsStream(center, range).collect(Collectors.toList());
    }

    /**
     * Get all Entities within a certain range of a center point as a stream.
     * <p>
     * You generally do not need to call this method. Instead, use the
     * {@link #getEntitiesInRange(Vector3, double)} method to get a list of
     * Entities within a range.
     *
     * @param center the center point
     * @param range the range
     * @return a stream of Entities within the range
     */
    public Stream<Entity> getEntitiesInRangeAsStream(Vector3 center, double range) {
        return getSpritesAsStream(Entity.class)
            .map(entity -> (Entity) entity)
            .filter(entity -> entity.getWorldPos().distanceTo(center) < range);
    }

    /**
     * Get all Entities within a certain range of a center point.
     *
     * @param center the center point
     * @param range the range
     * @return a list of Entities within the range
     */
    public List<Entity> getEntitiesInRange(Vector3 center, double range) {
        return getEntitiesInRangeAsStream(center, range).collect(Collectors.toList());
    }

    /**
     * Get the player object.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the world data object.
     *
     * @return the world data
     */
    public WorldData getWorldData() {
        return worldData;
    }

    /**
     * Get the list of {@link Damage} objects in the world.
     *
     * @return the list of damages
     */
    public List<Damage> getDamages() {
        return damages;
    }

    /**
     * Add a {@link Damage} object to the world.
     *
     * @param damage the Damage object
     */
    public void addDamage(Damage damage) {
        damages.add(damage);
    }

    public void addCollisionController(CollisionController controller) {
        collisionControllers.add(controller);
    }

    public void removeCollisionController(CollisionController controller) {
        collisionControllers.remove(controller);
    }

    public List<CollisionController> getCollisionControllers() {
        return collisionControllers;
    }
}
