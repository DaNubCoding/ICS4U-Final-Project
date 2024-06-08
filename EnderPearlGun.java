/**
 * A gun that shoots Ender Pearls.
 * 
 * @author Lucas Fu
 * @version June 2024
 */
public class EnderPearlGun extends RangedWeapon {
    public EnderPearlGun(Player player) {
        super(player, "test_pistol.png", 0, 2, 1, 0, 100, EnderPearl::new);
    }
}
