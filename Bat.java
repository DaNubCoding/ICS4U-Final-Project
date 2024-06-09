/**
 * The bat.
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class Bat extends MeleeWeapon {
    public Bat() {
        super("bat.png", 0, 20, 60, 5, 80, 7, 7);
        setCenterOfRotation(new Vector2(0, 2));
    }
}
