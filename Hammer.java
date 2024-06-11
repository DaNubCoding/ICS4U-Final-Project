/**
 * Write a description of class Hammer here.
 *
 * @author Matthew Li
 * @version June 2024
 */
public class Hammer extends MeleeWeapon {
    public Hammer() {
        super("hammer.png", 20, 80, 55, 25, 150, 12);
        setCenterOfRotation(new Vector2(0, 6));
    }
}
