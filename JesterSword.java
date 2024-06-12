import java.util.Random;
/**
 * A sword that makes you randomly evade around before striking.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class JesterSword extends MeleeWeapon {
    private Vector3 initialPos;
    private Timer timer = new Timer(0);
    private int dashes;

    public JesterSword() {
        super("sword.png", 100, 0, 20, 25, 10, 5);
        dashes = 0;
    }

    @Override
    public void lockToPlayer() {
        super.lockToPlayer();
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
