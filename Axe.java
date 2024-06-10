/**
 * The axe.
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class Axe extends MeleeWeapon {
    public Axe() {
        super("axe.png", 0, 50, 40, 20, 120, 6, 30);
        setCenterOfRotation(new Vector2(0, 7));
    }
}
