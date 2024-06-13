/**
 * The bat.
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class Bat extends MeleeWeapon {
    public Bat() {
        super("bat.png", 5, 20, 60, 10, 80, 7);
        setCenterOfRotation(new Vector2(0, 2));
    }
}
