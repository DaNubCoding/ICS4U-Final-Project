/**
 * The pitchfork.
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class Pitchfork extends MeleeWeapon {
    private Timer stabTimer;
    private int stabDistance;
    private Vector3 weaponOffset;

    public Pitchfork() {
        super("pitchfork.png", 0, 20, 80, 10, 20, 18, 2);
        setCenterOfRotation(new Vector2(0, 7));
        stabDistance = 20;
        weaponOffset = new Vector3();
    }

    @Override
    public void lockToPlayer() {
        Vector3 playerPos = getPlayer().getWorldPos();
        double playerRotation = getPlayer().getWorldRotation();

        //set weapon location to appropriate point
        Vector3 handOffset = Player.HAND_LOCATION.rotateY(playerRotation);
        setWorldPos(playerPos.add(handOffset).add(weaponOffset));

        setWorldRotation(playerRotation);

        stabTimer = super.getSwingTimer();
        if (stabTimer != null) {
            weaponOffset = new Vector3(Math.sin(stabTimer.progress() * Math.PI) * stabDistance, 0, 0).rotateY(playerRotation);
            if (stabTimer.ended()) {
                stabTimer = null;
                weaponOffset = new Vector3();
            }
        }
    }
}
