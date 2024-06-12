/**
 * A gun that shoots Ender Pearls.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class PearlGun extends RangedWeapon {
    public PearlGun() {
        super("pistol.png", 0, 4, 1, 0, 100, StatuePearl::new);
        setCenterOfRotation(new Vector2(3, 7));
    }
}
