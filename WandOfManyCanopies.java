/**
 * Test class for magic weapons
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class WandOfManyCanopies extends MagicWeapon {
    public WandOfManyCanopies(Player player) {
        super("pistol.png", 100, 5, 0, 0, CanopyBomb::new);
        setCenterOfRotation(new Vector2(3, 7));
    }
}
