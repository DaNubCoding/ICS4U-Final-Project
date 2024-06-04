/**
 * Test class for magic weapons
 * 
 * @author Lucas Fu
 * @version June 2024
 */
public class WandOfManyCrates extends MagicWeapon{
    public WandOfManyCrates(Player player) {
        super(player, "test_pistol.png", 100, 5, 0, 0, CrateBomb::new);
        setCenterOfRotation(new Vector2(0, 5));
    }
}