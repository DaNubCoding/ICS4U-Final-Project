public abstract class MeleeWeapon extends Weapon {
    public MeleeWeapon(Player player, String image) {
        super(player, image);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public abstract void attack();
}
