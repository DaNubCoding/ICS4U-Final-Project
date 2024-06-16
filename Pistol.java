/**
 * A pistol weapon.
 *
 * @author Matthew Li
 * @author Lucas Fu
 * @version May 2024
 */
public class Pistol extends RangedWeapon {
    public Pistol() {
        super("pistol.png", 80, 2, 5, 0, 50, PistolBullet::new);
        setCenterOfRotation(new Vector2(3, 7));
    }
}
