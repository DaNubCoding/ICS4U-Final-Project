/**
 * A pistol weapon.
 *
 * @author Matthew Li
 * @version May 2024
 */
public class Pistol extends RangedWeapon {
    public Pistol(Player player) {
        super("pistol.png", 90, 1, 5, 0, 20, TestProjectile::new);
        setCenterOfRotation(new Vector2(3, 7));
    }
}
