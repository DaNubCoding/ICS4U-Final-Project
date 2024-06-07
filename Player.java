import java.util.ArrayList;

import greenfoot.*;

/**
 * The player controlled by the user.
 *
 * @author Martin Baldwin
 * @author Andrew Wang
 * @version May 2024
 */
public class Player extends Entity {
    private static final Animation walkingAnimation = new Animation(6,
        "knight_walk1",
        "knight_walk2",
        "knight_walk3",
        "knight_walk4"
    );
    private static final Animation standingAnimation = new Animation(-1, "knight_standing");
    public static final int MAX_ITEM_NUM = 2;

    private double cameraTargetRotation;
    private Timer dashTimer;

    private ArrayList<Item> hotbar;
    private int heldIndex;

    private boolean tabFlag = false;

    public Player() {
        super(standingAnimation);
        hotbar = new ArrayList<Item>();
        dashTimer = new Timer(90);
        physics.setAlwaysTurnTowardsMovement();
        setHealth(200);
        heldIndex = 0;
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        getWorld().addWorldObject(new EnderPearlGun(this), getWorldPos());
    }

    @Override
    public void update() {
        // Apply input acceleration
        if (Greenfoot.isKeyDown("d")) {
            physics.accelerate(new Vector2(Camera.getRotation()));
        }
        if (Greenfoot.isKeyDown("s")) {
            physics.accelerate(new Vector2(Camera.getRotation() + 90));
        }
        if (Greenfoot.isKeyDown("a")) {
            physics.accelerate(new Vector2(Camera.getRotation() + 180));
        }
        if (Greenfoot.isKeyDown("w")) {
            physics.accelerate(new Vector2(Camera.getRotation() + 270));
        }

        // Dashing
        if (Greenfoot.isKeyDown("space") && dashTimer.ended()) {
            physics.applyImpulse(new Vector2(getWorldRotation()).multiply(6));
            dashTimer.restart();
        }

        // Update player's held item
        if (hotbar.size() > 0) hotbar.get(heldIndex).update();

        // Apply weapon changing
        if (Greenfoot.isKeyDown("tab") && !tabFlag) {
            if (hotbar.size() > 0) {
                getWorld().removeSprite(hotbar.get(heldIndex));
                heldIndex = (heldIndex + 1) % hotbar.size();
                getWorld().addSprite(hotbar.get(heldIndex), 0, 0);
            }
            tabFlag = true;
        } else if (tabFlag && !Greenfoot.isKeyDown("tab")) {
            tabFlag = false;
        }

        // TODO: TEMPORARY for demo purposes
        if (Greenfoot.isKeyDown("q") && getWorldY() == 0) {
            physics.reduceMomentum(0.33);
            physics.applyImpulse(new Vector3(0, 4, 0));
        }

        // TODO: TEMPORARY for demo purposes
        if (Greenfoot.isKeyDown("e") && getWorldY() == 0) {
            physics.reduceMomentum(0.9);
            Vector2 horImpulse = new Vector2(getWorldRotation()).multiply(5);
            physics.applyImpulse(Vector3.fromXZ(horImpulse).add(new Vector3(0, 6, 0)));
        }

        // Update physics
        physics.update();

        // Set the animation based on whether the player is moving
        if (physics.isMoving()) {
            setLoopingAnimation(walkingAnimation);
        } else {
            setLoopingAnimation(standingAnimation);
        }

        // Update camera stuff
        Camera.targetPosition(getWorldPos().add(new Vector3(0, 10, 0)));
        updateCameraRotation();
    }

    @Override
    public void render(GreenfootImage canvas) {
        double rot = getVisualRotation();
        // Render the weapon behind the player if the player is facing away
        if ((rot >= 0 && rot < 180) || rot > 330) {
            super.render(canvas);
            if (hotbar.size() > 0) hotbar.get(heldIndex).render(canvas);
        } else {
            if (hotbar.size() > 0) hotbar.get(heldIndex).render(canvas);
            super.render(canvas);
        }
    }

    private void updateCameraRotation() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null && mouse.getButton() == 1) {
            if (Greenfoot.mousePressed(null)) {
                MouseManager.initMouseLock();
            }
        }
        if (Greenfoot.mouseClicked(null)) {
            MouseManager.releaseMouseLock();
        }
        if (!MouseManager.isLocked()) return;
        Vector2 mouseRel = MouseManager.getMouseRel();
        MouseManager.lockMouse();
        cameraTargetRotation += mouseRel.x * 0.13;
        double zoom = Camera.getZoom() * (1 + mouseRel.y * 0.002);
        Camera.setZoom(Math.max(0.8, Math.min(6, zoom)));
        Camera.targetRotation(cameraTargetRotation);
    }

    /**
     * Get the number of items currently held.
     * 
     * @return the size of the hotbar
     */
    public int getHotbarSize() {
        return hotbar.size();
    }

    public void pickupItem(Item i) {
        hotbar.add(i);
        if(hotbar.size() > 1)
            getWorld().removeSprite(i);
    }

    public void throwItem() {
        if(hotbar.size() == 0) return;
        System.out.println(hotbar.remove(heldIndex));
        System.out.println(hotbar.size());
        if(heldIndex < 0 || heldIndex >= hotbar.size()) {
            heldIndex = 0;
            if(hotbar.size() != 0)
                getWorld().addSprite(hotbar.get(heldIndex), 0, 0);
        }
    }

    @Override
    public void damage(Damage damage) {
        super.damage(damage);
        System.out.println("Player took " + damage.getDamage() + " points of damage" + " and has " + getHealth() + " health left");
    }
}
