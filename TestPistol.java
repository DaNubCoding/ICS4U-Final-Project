/**
 * A pistol weapon for testing purposes.
 *
 * @author Andrew Wang
 * @author Lucas Fu
 * @version May 2024
 */
public class TestPistol extends RangedWeapon {
    public TestPistol(Player player) {
        super(player, "test_pistol.png", 90, 1, 5, 0, 20, TestProjectile::new);
        setCenterOfRotation(new Vector2(0, 5));
    }
}
