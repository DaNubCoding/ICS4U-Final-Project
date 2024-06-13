/**
 * The axe.
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class Axe extends MeleeWeapon {
    public Axe() {
        super("axe.png", 16, 50, 40, 40, 120, 6);
        setCenterOfRotation(new Vector2(0, 7));
    }
}
