import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * A sword that makes you randomly evade around before striking.
 *
 * @author Lucas Fu
 * @author Andrew Wang
 * @version June 2024
 */
public class JesterSword extends MeleeWeapon {
    private static Vector3 averagePos;
    private static boolean merging;

    private Vector3 initialPos;
    private Timer timer = new Timer(0);
    private int dashes;

    public JesterSword() {
        super("jester_sword.png", 100, 0, 20, 25, 10, 5);
        setCenterOfRotation(new Vector2(2, 7));
        dashes = 0;
        physics.setMaxSpeed(10);
    }

    @Override
    public void update() {
        super.update();

        if (getWorld() == null) return;

        List<? extends Sprite> swords = getWorld().getSprites(JesterSword.class);
        List<JesterSword> inRange = new ArrayList<>();
        for (Sprite sprite : swords) {
            JesterSword sword = (JesterSword) sprite;
            if (sword == this) continue;
            Vector3 delta = sword.getWorldPos().subtract(getWorldPos());
            double dist = delta.magnitude();
            if (dist < 60 && sword.isOnGround()) inRange.add(sword);
            for (double i = 0; i < dist; i += 5) {
                if (Math.random() < 0.99) continue;
                JesterParticle particle = new JesterParticle();
                getWorld().addWorldObject(particle, getWorldPos().add(delta.normalize().multiply(i)));
                particle.getLifeTimer().restart(20);
            }
        }

        if (inRange.size() >= 4 && isOnGround() && !merging) {
            averagePos = new Vector3(0, 0, 0);
            for (JesterSword sword : inRange) {
                averagePos = averagePos.add(sword.getWorldPos());
            }
            averagePos = averagePos.divide(inRange.size());
            merging = true;
        }

        if (merging) {
            Vector3 delta = averagePos.subtract(getWorldPos());
            physics.accelerate(delta.normalize().multiply(0.23));
            if (delta.magnitude() < 3) {
                merging = false;

                SprackWorld world = getWorld();
                for (JesterSword sword : inRange) {
                    world.removeSprite(sword);
                    world.getWorldData().removeItem(sword.id);
                }
                world.removeSprite(this);
                world.getWorldData().removeItem(this.id);

                for (int i = 0; i < 30; i++) {
                    JesterParticle particle = new JesterParticle();
                    Vector2 offset = new Vector2(Math.random() * 360).multiply(Math.random() * 20);
                    world.addWorldObject(particle, getWorldPos().addXZ(offset));
                }

                WandOfRickAstley wand = new WandOfRickAstley();
                world.addWorldObject(wand, getWorldPos());
                wand.physics.applyForce(new Vector3(
                    Math.random() * 6 - 3,
                    3,
                    Math.random() * 6 - 3
                ));
            }
        }
    }

    @Override
    public void windup() {
        if (initialPos == null) initialPos = getPlayer().getWorldPos();
        PhysicsController playerPhysics = getPlayer().physics;
        if (timer.ended() && dashes++ < 7) {
            timer.restart(8);
            playerPhysics.applyForce(new Vector2(Math.random() * 360).multiply(Math.random() * 10 + 6));
        }
        if (timer.ended()) {
            timer.restart(50);
            playerPhysics.setWorldPos(initialPos);
            Camera.shake(2, 50);
        }
        playerPhysics.reduceMomentum(0.15);
    }

    @Override
    public void attack() {
        Vector2 playerFacingVector = new Vector2(getPlayer().getWorldRotation());
        PhysicsController playerPhysics = getPlayer().physics;
        playerPhysics.setWorldPos(initialPos);
        for (int i = 0; i < 150; i += 30) {
            Damage damage = new Damage(getPlayer(), this, 40,
                                       initialPos.addXZ(playerFacingVector.multiply(i)),
                                       15);
            for (int j = 0; j < 3; j++)
                getWorld().addWorldObject(new JesterParticle(), initialPos.addXZ(playerFacingVector.multiply(i + j * 10)));
            try {
                getWorld().getDamages().add(damage);
            }
            catch (NullPointerException e) {} // if the weapon got switched out before doing damage
        }

        playerPhysics.applyForce(playerFacingVector.multiply(8.2));
        dashes = 0;
        initialPos = null;
        Camera.shake(4, 24);
    }
}
