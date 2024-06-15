/**
 * The axe.
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class Axe extends MeleeWeapon {
    public Axe() {
        super("axe.png", 16, 50, 40, 40, 120, 6, 7);
        setCenterOfRotation(new Vector2(0, 7));
    }

    @Override
    public void attack() {
        super.attack();
        Damage damage = getDamageObject();
        damage.setDeletables(OakTree.class, WillowTree.class);
    }
}
