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
    private PhysicsController playerPhysics;
    private Random rand = new Random();
    private int teleports;

    public JesterSword() {
        super("sword.png", 100, 0, 20, 25, 10, 5);
        teleports = 0;
    }

    @Override
    public void lockToPlayer() {
        super.lockToPlayer();
        playerPhysics = new PhysicsController(getPlayer());
    }

    @Override
    public void windup() {
        if (initialPos == null) initialPos = getPlayer().getWorldPos();
        if (timer.ended() && teleports++ < 7) {
            timer.restart(10);
            Vector2 randAdj = new Vector2(rand.nextInt(100) - 50, rand.nextInt(100) - 50);
            playerPhysics.setWorldPos(initialPos.addXZ(randAdj));
        }
        if (timer.ended()) {
            timer.restart(50);
            playerPhysics.setWorldPos(initialPos);
        }
    }

    @Override
    public void attack() {
        Vector2 playerFacingVector = new Vector2(getPlayer().getWorldRotation());
        playerPhysics.setWorldPos(initialPos);
        for (int i = 0; i < 150; i += 30) {
            Damage damage = new Damage(getPlayer(), this, 25,
                                       initialPos.addXZ(playerFacingVector.multiply(i)),
                                       15);
            for (int j = 0; j < 10; j++)
                getWorld().addWorldObject(new StatueParticle(), initialPos.addXZ(playerFacingVector.multiply(i)));
            try {
                getWorld().getDamages().add(damage);
            }
            catch (NullPointerException e) {} // if the weapon got switched out before doing damage
        }

        playerPhysics.applyForce(playerFacingVector.multiply(150));
        teleports = 0;
        initialPos = null;
    }

    @Override
    public void update() {
        if (playerPhysics != null)
            playerPhysics.update();
        super.update();
    }
}
