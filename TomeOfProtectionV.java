/**
 * The Tome of Protection V summons a Shield for the player using the {@link SaintShieldMaker}.
 */
public class TomeOfProtectionV extends MagicWeapon {
    public TomeOfProtectionV() {
        super("tome_of_protection_v.png", 50, 1, 25, 600, SaintShieldMaker::new);
    }
}
