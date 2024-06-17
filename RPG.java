/**
 * A ranged weapon that fires explosives that have limited range.
 *
 * @author Matthew Li
 * @version June 2024
 */
public class RPG extends RangedWeapon
{
    public RPG()
    {
        super("rpg.png", 2, 2, 1, 0, 150, RPGProjectile::new);
        setCenterOfRotation(new Vector2(3, 7));
    }
}
