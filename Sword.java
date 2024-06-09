/**
 * The sword.
 *
 * @author Matthew Li
 * @version June 2024
 */
public class Sword extends MeleeWeapon {
    public Sword() {
        super("sword.png", 0, 30, 40, 10, 100, 9, 9);
        setCenterOfRotation(new Vector2(0, 6));
    }
}
