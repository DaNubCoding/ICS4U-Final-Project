/**
 * The Repeater is a gun that fires fast, strong bullets at a low rate.
 *
 * @author Matthew Li
 * @version June 2024
 */
public class Repeater extends RangedWeapon {
    public Repeater() {
        super("repeater.png", 0, 6, 1, 0, 100, RepeaterProjectile::new);
        setCenterOfRotation(new Vector2(3, 7));
    }
}
