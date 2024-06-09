/**
 * A gun that shoots Ender Pearls.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class EnderPearlGun extends RangedWeapon {
    public EnderPearlGun() {
        super("pistol.png", 0, 2, 1, 0, 100, StatuePearl::new);
        setCenterOfRotation(new Vector2(3, 7));
    }
}
