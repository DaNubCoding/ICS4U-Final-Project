/**
 * The sword.
 *
 * @author Matthew Li
 * @version June 2024
 */
public class Sword extends MeleeWeapon {
    public Sword(Player player) {
        super(player, "sword.png", 0, 30, 40, 10, 140);
        setCenterOfRotation(new Vector2(0, 6));
    }
}
