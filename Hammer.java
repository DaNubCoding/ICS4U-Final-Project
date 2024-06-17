/**
 * The Hammer, the slowest melee weapon that also deals the largest amount of
 * damage.
 *
 * @author Matthew Li
 * @version June 2024
 */
public class Hammer extends MeleeWeapon {
    public Hammer() {
        super("hammer.png", 20, 80, 55, 50, 150, 12, 10);
        setCenterOfRotation(new Vector2(0, 6));
    }
}
