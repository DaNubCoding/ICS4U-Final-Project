/**
 * A gun that shoots Statue Pearls.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class PearlGun extends RangedWeapon {
    public PearlGun() {
        super("pearl_gun.png", 0, 4, 1, 0, 100, StatuePearl::new);
        setCenterOfRotation(new Vector2(3, 7));
    }
}
