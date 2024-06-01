/**
 * A pistol weapon for testing purposes.
 *
 * @author Andrew Wang
 * @version May 2024
 */
public class TestPistol extends RangedWeapon {
    public TestPistol(Player player) {
        super(player, "test_pistol.png");
        setCenterOfRotation(new Vector2(0, 5));
    }

    @Override
    public void attack() {

    }
}
