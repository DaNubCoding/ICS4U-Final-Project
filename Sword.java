/**
 * The sword.
 *
 * @author Matthew Li
 * @version June 2024
 */
public class Sword extends MeleeWeapon {
    public Sword() {
        super("sword.png", 6, 30, 40, 10, 100, 9);
        setCenterOfRotation(new Vector2(0, 6));
    }
}
