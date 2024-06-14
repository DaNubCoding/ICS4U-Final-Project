import java.util.List;

/**
 * A sword that teleports the attacker behind the enemy before delivering an
 * uppercut.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class StatueMagicSword extends MeleeWeapon {
    Entity target;
    Vector3 diff;

    public StatueMagicSword() {
        super("statue_projectile.png", 10, 100, 40, 40, 60, 30);
    }

    @Override
    public void windup() {
        Vector2 clickLocation = MouseManager.getMouseWorldPos();
        List<Entity> targets = getWorld().getEntitiesInRange(Vector3.fromXZ(clickLocation), 40);
        if (targets.size() == 0) return;
        for (int i = 0; i < 15; i++) {
            getWorld().addWorldObject(new StatueParticle(), getWorldPos().addXZ(new Vector2(Math.random() * 10, Math.random() * 10)));
        }
        Entity target = targets.get(0);
        this.target = target;
        Vector3 targetFacing = Vector3.fromXZ(new Vector2(target.getWorldRotation()));
        Vector3 targetPos = target.getWorldPos().subtract(targetFacing);
        getPlayer().physics.setWorldPos(targetPos);
    }

    @Override
    public double getTargetAngle() {
        if (target != null) {
            Vector3 diff = target.getWorldPos().subtract(getWorldPos());
            this.diff = diff;
            return new Vector2(diff.x, diff.z).angle();
        }
        return -1;
    }

    @Override
    public void attack() {
        super.attack();
        for (Entity e : getWorld().getEntitiesInRange(getWorldPos(), 40)) {
            if (e instanceof Player) continue;
            e.physics.applyForce(diff.add(new Vector3(0, 20, 0)));
            e.damage(new Damage(getPlayer(), this, 50, getWorldPos(), 40));
        }
    }
}
