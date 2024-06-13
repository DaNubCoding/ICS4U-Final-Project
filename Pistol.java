/**
 * A pistol weapon.
 *
 * @author Matthew Li
 * @version May 2024
 */
public class Pistol extends RangedWeapon {
    public Pistol() {
        super("pistol.png", 90, 2, 5, 0, 50, PistolBullet::new);
        setCenterOfRotation(new Vector2(3, 7));
    }
}
