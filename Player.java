import java.util.ArrayList;

import greenfoot.*;

/**
 * The player controlled by the user.
 *
 * @author Martin Baldwin
 * @author Andrew Wang
 * @author Matthew Li
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
    private static final Animation dashAnimation = new Animation(25, "knight_dash");
    public static final int MAX_ITEM_NUM = 5;
    public static final Vector3 HAND_LOCATION = new Vector3(5, 8, -5);
    private static final SoundEffect[] footstepSounds = {
        new SoundEffect("grass_step1.wav"),
        new SoundEffect("grass_step2.wav"),
        new SoundEffect("grass_step3.wav"),
        new SoundEffect("grass_step4.wav"),
    };

    private double cameraTargetRotation;
    private Timer dashTimer;
    private Timer footstepTimer;
    private Vector3 footstepOffset;
    private Timer footstepSoundTimer;

    private ArrayList<Item> hotbar;
    private int heldIndex;

    private static final double maxArmor = 60;
    private double armor = 60;
    private Timer armorTimer = new Timer(0);
    private HealthBar armorBar;
    private HealthBar healthBar;

    private boolean tabFlag = false;
    private boolean qFlag = false;

    public Player() {
        super(standingAnimation);
        hotbar = new ArrayList<Item>();
        dashTimer = new Timer(90);
        heldIndex = 0;
        healthBar = new HealthBar(this);
        armorBar = new HealthBar(this);
        armorBar.setLostColor(new Color(0, 0, 0, 0));
        armorBar.setHaveColor(new Color(84, 164, 255));
        armorBar.setHealth(armor);
        setHealth(200);
        footstepTimer = new Timer(8);
        footstepOffset = new Vector3(0, 0, 4);
        footstepSoundTimer = new Timer(15);
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        getWorld().addCollisionController(new CollisionController(this, 8, 0.1, 0.0));
        ((SprackWorld) world).addWorldObject(armorBar, 0, 0, 0);
        ((SprackWorld) world).addWorldObject(healthBar, 0, 0, 0);
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
            physics.applyForce(new Vector2(getWorldRotation()).multiply(6));
            playOneTimeAnimation(dashAnimation);
            dashTimer.restart();
        }

        // Update player's held item
        if (hotbar.size() > 0) hotbar.get(heldIndex).update();

        // Apply weapon changing
        if (Greenfoot.isKeyDown("tab") && !tabFlag) {
            if (hotbar.size() > 1) {
                getWorld().removeSprite(hotbar.get(heldIndex));
                heldIndex = (heldIndex + 1) % hotbar.size();
                getWorld().addWorldObject(hotbar.get(heldIndex), getWorldPos().add(HAND_LOCATION.rotateY(getWorldRotation())));
            }
            tabFlag = true;
        } else if (tabFlag && !Greenfoot.isKeyDown("tab")) {
            tabFlag = false;
        }

        // Throw item
        if (Greenfoot.isKeyDown("q") && !qFlag) {
            throwItem();
            qFlag = true;
        } else if (qFlag && !Greenfoot.isKeyDown("q")) {
            qFlag = false;
        }

        if (Greenfoot.isKeyDown("e") && getWorldY() == 0) {
            physics.reduceMomentum(0.9);
            Vector2 horImpulse = new Vector2(getWorldRotation()).multiply(5);
            physics.applyForce(Vector3.fromXZ(horImpulse).add(new Vector3(0, 6, 0)));
        }

        // Update physics
        physics.update();

        // Update rotation
        if (MouseManager.isLocked()) {
            physics.setAlwaysTurnTowardsMovement(true);
        } else {
            physics.setAlwaysTurnTowardsMovement(false);
            Vector2 mousePos = MouseManager.getMouseWorldPos();
            if (mousePos != null) {
                physics.turnTowards(mousePos);
            }
        }

        // Set the animation based on whether the player is moving
        if (physics.isMoving()) {
            setLoopingAnimation(walkingAnimation);
            if (footstepTimer.ended() && getWorldY() == 0) {
                footstepOffset = footstepOffset.setZ(footstepOffset.z < 0 ? 4 : -4);
                Vector3 rotated = footstepOffset.rotateY(getWorldRotation());
                Vector3 footstepPos = getWorldPos().add(rotated);
                getWorld().addWorldObject(new FootstepParticle(), footstepPos);
                footstepTimer.restart();
            }
        } else {
            setLoopingAnimation(standingAnimation);
        }

        if (physics.isMoving() && footstepSoundTimer.ended()) {
            footstepSoundTimer.restart(15);
            footstepSounds[(int) (Math.random() * footstepSounds.length)].play();
        }

        // update armor if out of combat for long enough
        if (armorTimer.ended()) {
            armor = Math.min(armor + 0.2, maxArmor);
            armorBar.setHealth(armor);
        }

        // Update camera
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
        Camera.targetPosition(getWorldPos().add(new Vector3(0, 10, 0)));

        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null && mouse.getButton() == 3) {
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
        Camera.setZoom(Math.max(0.6, Math.min(2, zoom)));
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

    /**
     * Get the list of hotbar items.
     *
     * @return the hotbar
     */
    public ArrayList<Item> getHotbar() {
        return hotbar;
    }

    /**
     * Get the index in the hotbar of the item current being held.
     *
     * @return the index of the held item
     */
    public int getHeldIndex() {
        return heldIndex;
    }

    /**
     * Add an Item to the hotbar.
     *
     * @param i the item to be added
     */
    public void pickupItem(Item i) {
        hotbar.add(i);
        getWorld().getWorldData().setHotbar(hotbar);
        getWorld().getWorldData().tryAddNewWeapon(i);
        if(hotbar.size() > 1)
            getWorld().removeSprite(i);
    }

    /**
     * Toss an Item out of the hotbar, removing it.
     */
    public void throwItem() {
        if(hotbar.size() == 0) return;
        hotbar.get(heldIndex).drop();
        hotbar.remove(heldIndex);
        getWorld().getWorldData().setHotbar(hotbar);
        if(heldIndex >= hotbar.size()) {
            heldIndex = 0;
        }
        if(hotbar.size() != 0)
            getWorld().addWorldObject(hotbar.get(heldIndex), HAND_LOCATION.rotateY(getWorldRotation()));
    }

    @Override
    public void setHealth(double health) {
        super.setHealth(health);
        healthBar.setHealth(health);
    }

    @Override
    public void damage(Damage damage) {
        double dmg = damage.getDamage();
        getWorld().getWorldData().addPlayerDamageTaken(damage.getDamage());

        armorTimer.restart(480);
        if(armor > 0) {
            for (int i = 0; i < damage.getDamage() + 4; i++) {
                ArmorParticle particle = new ArmorParticle();
                Vector3 offset = new Vector3(
                    Math.random() * 20 - 10,
                    Math.random() * getHeight(),
                    Math.random() * 20 - 10
                );
                getWorld().addWorldObject(particle, getWorldPos().add(offset));
            }
        }
        armor -= dmg;
        armorBar.setHealth(armor);

        if(armor <= 0) {
            super.damage(new Damage(damage.getOwner(),
                                    damage.getSource(),
                                    -armor,
                                    damage.getCenter(),
                                    damage.getRadius()));
            armor = 0;
        }
    }

    @Override
    public void die() {
        getWorld().getWorldData().saveData();
        Greenfoot.setWorld(new DeathScreen(getWorld().getWorldData()));
    }
}
