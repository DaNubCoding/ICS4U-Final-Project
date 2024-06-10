/**
 * A purple particle emitted by the statue and its pearl.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class StatueParticle extends Particle {
    public StatueParticle() {
        super(getRandomImage(), (int) (Math.random() * 40 + 20));
        physics.setAffectedByFrictionalForces(false);
        physics.applyForce(new Vector3(0, Math.random() * 0.4 + 0.3, 0));
    }

    private static String getRandomImage() {
        return "statue_particle" + (int) (Math.random() * 3 + 1) + ".png";
    }
}
