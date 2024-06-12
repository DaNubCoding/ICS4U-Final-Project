/**
 * A magic weapon that summons canopies from the sky to blow up opponents.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class WandOfManyCanopies extends MagicWeapon {
    public WandOfManyCanopies() {
        super("wand_of_many_canopies.png", 50, 3, 0, 700, CanopyBomb::new);
        setCenterOfRotation(new Vector2(3, 7));
    }
}
