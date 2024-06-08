
/**
 * Items are world sprites that can be held in the player inventory, used, and
 * dropped into the world.
 * 
 * @author Lucas Fu
 * @version June 2024
 */
public class Item extends WorldSprite {
    private Player player;
    private boolean isOnGround;
    private PhysicsController physics = new PhysicsController(this);
    private Timer pickupTimer = new Timer(0);

    public Item(Player player, boolean isOnGround) {
        this.isOnGround = isOnGround;
        this.player = player;
    }

    @Override
    public void update() {
        if(isOnGround) awaitPickup();
        physics.update();
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    private void awaitPickup() {
        player = getWorld().getPlayer();
        Vector3 playerPos = player.getWorldPos();
        if(getWorldPos().distanceTo(playerPos) < 45 && pickupTimer.ended()
        && player.getHotbarSize() < Player.MAX_ITEM_NUM) {
            physics.accelTowards(playerPos);
        }
        if(getWorldPos().distanceTo(playerPos) < 15 && pickupTimer.ended()) {
            if(player.getHotbarSize() < Player.MAX_ITEM_NUM) {
                isOnGround = false;
                player.pickupItem(this);
            }
        }
    }

    public void drop() {
        physics.applyImpulse(new Vector3(3, 3, 0).rotateY(getPlayer().getWorldRotation()));
        isOnGround = true;
        pickupTimer.restart(100);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
