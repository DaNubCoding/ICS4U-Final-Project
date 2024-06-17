/**
 * The Rick on a Stick is a magic weapon creates by merging 5 {@link JesterSword}s
 * together. It creates 3 {@link RickMegaphone}s on the click location.
 */
public class RickOnAStick extends MagicWeapon {
    private static SoundEffect[] sounds = {
        new SoundEffect("never_gonna_give_you_up1.wav"),
        new SoundEffect("never_gonna_give_you_up2.wav"),
    };
    private Vector3 customLocation;

    static {
        for (SoundEffect sound : sounds) {
            sound.setVolume(25);
        }
    }

    public RickOnAStick() {
        super("wand_of_rick_astley.png", 0, 1, 0, 480, RickMegaphone::new);
        setCenterOfRotation(new Vector2(1, 6));
    }

    @Override
    public Vector3 findTargetLocation() {
        return customLocation;
    }

    @Override
    public void attack() {
        Vector2 mousePos = MouseManager.getMouseWorldPos();
        Vector3 targetLocation = new Vector3(mousePos.x, 0, mousePos.y);
        int angleAdj = (int) (Math.random() * 360 / 3);
        for(int i = 0; i < 3; i++) {
            customLocation = targetLocation.addXZ(new Vector2(i * 360 / 3 + angleAdj).multiply(30));
            super.attack();
        }
        sounds[(int) (Math.random() * sounds.length)].play();
    }
}
