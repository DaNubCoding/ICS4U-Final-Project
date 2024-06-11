/**
 * Write a description of class RPG here.
 *
 * @author Matthew Li
 * @version June 2024
 */
public class RPG extends RangedWeapon
{
    /**
     * Constructor for objects of class RPG
     */
    public RPG()
    {
        super("rpg.png", 2, 2, 1, 0, 150, RPGProjectile::new);
        setCenterOfRotation(new Vector2(3, 7));
    }
}
