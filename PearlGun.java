import java.util.ArrayList;
import java.util.List;

/**
 * A gun that shoots Statue Pearls.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class PearlGun extends RangedWeapon {
    private static Vector3 averagePos;
    private static boolean merging;

    public PearlGun() {
        super("pearl_gun.png", 0, 4, 1, 0, 100, StatuePearl::new);
        setCenterOfRotation(new Vector2(3, 7));
    }

    @Override
    public void update() {
        super.update();

        if (getWorld() == null) return;

        int distSum = 0;
        List<? extends Sprite> guns = getWorld().getSprites(PearlGun.class);
        List<PearlGun> inRange = new ArrayList<>();

        // determine if all guns are in range, and create particles
        for (Sprite sprite : guns) {
            PearlGun gun = (PearlGun) sprite;
            if (gun == this) continue;
            Vector3 delta = gun.getWorldPos().subtract(getWorldPos());
            double dist = delta.magnitude();
            if (dist < 60 && gun.isOnGround()) inRange.add(gun);
            distSum += dist;
            for (double i = 0; i < dist; i += 5) {
                if (Math.random() < 0.99) continue;
                StatueParticle particle = new StatueParticle();
                getWorld().addWorldObject(particle, getWorldPos().add(delta.normalize().multiply(i)));
                particle.getLifeTimer().restart(20);
            }
        }

        // if ready to merge, find the average position and start pushing the guns together
        if (inRange.size() >= 4 && isOnGround() && !merging) {
            averagePos = new Vector3(0, 0, 0);
            for (PearlGun gun : inRange) {
                averagePos = averagePos.add(gun.getWorldPos());
            }
            averagePos = averagePos.divide(inRange.size());
            for (PearlGun gun : inRange) {
                Vector3 delta = averagePos.subtract(gun.getWorldPos());
                gun.physics.applyForce(delta.scaleToMagnitude(8));
                gun.physics.setWorldPos(averagePos.add(delta.scaleToMagnitude(-3)));
            }
            Vector3 delta = averagePos.subtract(getWorldPos());
            physics.applyForce(delta.scaleToMagnitude(8));
            physics.setWorldPos(averagePos.add(delta.scaleToMagnitude(-3)));
            merging = true;
        }

        // combine into new weapons
        if (merging) {
            disablePickup();
            Vector3 delta = averagePos.subtract(getWorldPos());
            physics.accelerate(delta.scaleToMagnitude(0.3));
            if (delta.magnitude() < 3) {
                physics.setWorldPos(averagePos);
                physics.reduceMomentum(1.0);
            }
            if (distSum < 10) {
                merging = false;

                SprackWorld world = getWorld();
                for (PearlGun gun : inRange) {
                    world.removeSprite(gun);
                    world.getWorldData().removeItem(gun.id);
                }
                world.removeSprite(this);
                world.getWorldData().removeItem(this.id);

                for (int i = 0; i < 30; i++) {
                    StatueParticle particle = new StatueParticle();
                    Vector2 offset = new Vector2(Math.random() * 360).multiply(Math.random() * 20);
                    world.addWorldObject(particle, getWorldPos().addXZ(offset));
                }

                SwordOfSurprise sword = new SwordOfSurprise();
                world.addWorldObject(sword, getWorldPos());
                sword.physics.applyForce(new Vector3(
                    Math.random() * 6 - 3,
                    3,
                    Math.random() * 6 - 3
                ));
            }
        }
    }
}
