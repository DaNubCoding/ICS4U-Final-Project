import java.util.ArrayList;

import greenfoot.*;

/**
 * The player controlled by the user.
 *
 * @author Martin Baldwin
 * @author Andrew Wang
 * @author Matthew Li
 * @author Lucas Fu
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
    private double cameraRotDriftVel;
    private Timer dashTimer;
    private Timer footstepTimer;
    private Vector3 footstepOffset;
    private Timer footstepSoundTimer;

    private ArrayList<Item> hotbar;
    private int heldIndex;

    public static final double MAX_ARMOR = 80;
    public static final double MAX_HP = 200;
    private double armor = MAX_ARMOR;
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
        armorBar.setHaveColor(new Color(84, 164, 255));
        armorBar.setHealth(armor);
        armorBar.setYOffset(14);
        setHealth(MAX_HP);
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

        // Apply weapon changing
        if (Greenfoot.isKeyDown("tab") && !tabFlag) {
            if (hotbar.size() > 1) {
                getWorld().removeSprite(hotbar.get(heldIndex));
                heldIndex = (heldIndex + (Greenfoot.isKeyDown("shift") ? -1 : 1)) % hotbar.size();
                if (heldIndex < 0) {
                    heldIndex += hotbar.size();
                }
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

        // Update physics
        physics.update();

        // Update rotation
        if (MouseManager.isLocked()) {
            physics.setAlwaysTurnTowardsMovement(true);
        } else {
            physics.setAlwaysTurnTowardsMovement(false);
            Vector2 mousePos = MouseManager.getMouseWorldPos();
            if (mousePos != null) {
                double prevRot = physics.getWorldRotation();
                physics.turnTowards(mousePos);

                // Rotate the camera to drift slightly based on the amount of rotation of the player
                double diff = physics.getWorldRotation() - prevRot;
                if (diff > 180.0) {
                    diff -= 360.0;
                } else if (diff < -180.0) {
                    diff += 360.0;
                }
                cameraRotDriftVel += diff * 0.02;
            }
            cameraRotDriftVel *= 0.9;
        }
        Camera.targetRotation(Camera.getRotation() + cameraRotDriftVel);

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

        // Play footstep sounds when moving
        if (physics.isMoving() && footstepSoundTimer.ended()) {
            footstepSoundTimer.restart(15);
            footstepSounds[(int) (Math.random() * footstepSounds.length)].play();
        }

        // Restore armor if out of combat for long enough
        if (armorTimer.ended()) {
            armor = Math.min(armor + 0.2, MAX_ARMOR);
            armorBar.setHealth(armor);
            getWorld().getWorldData().setPlayerArmor(armor);
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
                cameraTargetRotation = Camera.getRotation();
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
        if(i instanceof Weapon)
            getWorld().getWorldData().tryAddNewWeapon((Weapon) i);
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

    /**
     * Set the armor of this player.
     *
     * @param armor the armor value
     */
    public void setArmor(double armor) {
        this.armor = armor;
        armorBar.setHealth(armor);
    }

    /**
     * Restart the dash timer.
     */
    public void restartDashTimer() {
        dashTimer.restart();
    }

    @Override
    public void damage(Damage damage) {
        double dmg = damage.getDamage();
        if (!dashTimer.ended()) return;

        getWorld().getWorldData().addPlayerDamageTaken(dmg);

        armorTimer.restart(480);
        if(armor > 0) {
            for (int i = 0; i < dmg + 4; i++) {
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

        getWorld().getWorldData().setPlayerArmor(armor);
        getWorld().getWorldData().setPlayerHp(getHealth());
    }

    @Override
    public void die(Entity killer) {
        getWorld().getWorldData().saveData();
        Greenfoot.setWorld(new DeathScreen(getWorld().getWorldData(), killer));
    }
}
