/**
 * The temporary test melee weapon.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class TestSword extends MeleeWeapon {
    public TestSword(Player player) {
        super(player, "sword.png", 0, 30, 40, 10, 90);
        setCenterOfRotation(new Vector2(0, 20));
    }
}
