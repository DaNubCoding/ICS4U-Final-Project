/**
 * A purely cosmetic Sprack created by CanopyBomb that resembles a canopy, but
 * perishes after a while.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class PerishableCanopy extends Magic {
    private Timer knockbackTimer;
    public PerishableCanopy(String sheetname) {
        super(0, -1, 600, 0);
        knockbackTimer = new Timer(0);
    }

    @Override
    public void actionUpdate() {
        if (knockbackTimer.ended()) {
            for (Entity e : getWorld().getEntitiesInRange(getWorldPos(), 80)) {
                int forceDivision;
                if (e instanceof Player) forceDivision = -5;
                else forceDivision = -4;
                e.physics.applyForce(e.getWorldPos()
                                      .subtract(getWorldPos())
                                      .normalize()
                                      .divide(forceDivision));
            }
        }
        setWorldRotation(getWorldRotation() + 5);
    }
}
