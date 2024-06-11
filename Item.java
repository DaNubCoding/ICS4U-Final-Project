import java.util.HashMap;
import java.util.function.Supplier;
import greenfoot.GreenfootImage;

/**
 * Items are world sprites that can be held in the player inventory, used, and
 * dropped into the world.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class Item extends WorldSprite {
    public final long id = Math.round(Math.random() * (Long.MAX_VALUE - 1));
    private Player player;
    private boolean isOnGround;
    public final PhysicsController physics = new PhysicsController(this);
    private Timer pickupTimer = new Timer(0);
    private boolean updatedFlag = false;

    public static final HashMap<String, Supplier<Item>> NAMES = new HashMap<>();

    static {
        NAMES.put("pistol", Pistol::new);
        NAMES.put("sword", Sword::new);
        NAMES.put("enderpearlgun", PearlGun::new);
        NAMES.put("axe", Axe::new);
        NAMES.put("bat", Bat::new);
        NAMES.put("pitchfork", Pitchfork::new);
        NAMES.put("jestersword", JesterSword::new);
        NAMES.put("wandofmanycanopies", WandOfManyCanopies::new);
        NAMES.put("rpg", RPG::new);
        NAMES.put("repeater", Repeater::new);
        NAMES.put("hammer", Hammer::new);
    }

    /**
     * Create a new Item on the ground.
     * @param image the file name of the item's image
     */
    public Item(String image) {
        this.isOnGround = true;
        setOriginalImage(new GreenfootImage(image));
    }

    @Override
    public void update() {
        physics.update();

        if(isOnGround) {
            if(pickupTimer.ended() && !updatedFlag) {
                updatedFlag = true;
                Vector3 pos = getWorldPos();
                getWorld().getWorldData().storeItem(new Vector2(pos.x / 20, pos.z / 20), this);
            }
            awaitPickup();
            return;
        }

        lockToPlayer();
    }

    /**
     * Lock the weapon to the player's hand position and rotation.
     */
    public void lockToPlayer() {
        Player player = getPlayer();
        Vector3 playerPos = player.getWorldPos();
        // transform player's bottom-center location to hand location
        Vector3 handOffset = Player.HAND_LOCATION.rotateY(player.getWorldRotation());
        setWorldPos(playerPos.add(handOffset));

        setWorldRotation(player.getWorldRotation());
    }

    /**
     * Whether the item is on the ground.
     *
     * @return whether this item is on the ground
     */
    public boolean isOnGround() {
        return isOnGround;
    }

    /**
     * Manually take the item off the ground without using {@link #awaitPickup}
     */
    public void takeOffGround() {
        isOnGround = false;
    }

    /**
     * Wait for a player to pick this item up.
     * <p>
     * Magnetizes to a player within 45 pixels, and
     * gets picked up by a player within 15 pixels
     */
    private void awaitPickup() {
        player = getWorld().getPlayer();
        Vector3 playerPos = player.getWorldPos();
        if(getWorldPos().distanceTo(playerPos) < 30 && pickupTimer.ended()
        && player.getHotbarSize() < Player.MAX_ITEM_NUM) {
            physics.accelTowards(playerPos);
            getWorld().getWorldData().removeItem(id);
        }
        if(getWorldPos().distanceTo(playerPos) < 15 && pickupTimer.ended()) {
            if(player.getHotbarSize() < Player.MAX_ITEM_NUM) {
                isOnGround = false;
                player.pickupItem(this);
                updatedFlag = false;
                getWorld().getWorldData().removeItem(id);
            }
        }
    }

    /**
     * Untether this item from the player, flinging this in the process.
     * <p>
     * A timer of 100 frames is applied to prevent re-pickup.
     */
    public void drop() {
        physics.applyForce(new Vector3(3, 3, 0).rotateY(getPlayer().getWorldRotation()));
        isOnGround = true;
        pickupTimer.restart(100);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return getClass().getName().toLowerCase();
    }
}
