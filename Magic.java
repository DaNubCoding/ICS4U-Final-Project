import java.util.Random;

/**
 * Magic is summoned by magic weapons.
 * 
 * @author Lucas Fu
 * @version June 2024
 */
public abstract class Magic extends WorldSprite {
    private int targetYpos;
    private int lifespan;
    private int delay;
    private Vector3 startPos;
    private Random rand = new Random();

    /**
     * Create a new Magic with specified range of delays.
     * @param startPos the location where the magic will be created
     * @param inaccuracy the maximum radius from the startpos where the magic can be created
     * @param targetYpos the y-position where the magic will disappear
     * @param lifespan the number of acts this magic will last (use for stationary magic)
     * @param minDelay the minimum delay after which the magic will be created
     * @param maxDelay the maximum delay after which the magic will be created
     */
    public Magic(Vector3 startPos, double inaccuracy, int targetYpos, int lifespan,
                 int minDelay, int maxDelay) {
        super();
        this.targetYpos = targetYpos;
        this.lifespan = lifespan;

        double difference = maxDelay - minDelay;
        this.delay = minDelay + (int) (rand.nextDouble() * difference);

        double xAdj = rand.nextDouble() * 2 * inaccuracy - inaccuracy;
        double yAdj = rand.nextDouble() * 2 * inaccuracy - inaccuracy;
        Vector2 adj2 = new Vector2(xAdj, yAdj);

        // normalize diagonal adjustment
        if (inaccuracy != 0)
            adj2 = adj2.normalize().multiply(adj2.magnitude());
            
        Vector3 posAdjust = new Vector3(adj2.x, 0, adj2.y);
        this.startPos = startPos.add(posAdjust);

        // make sure this is rendered off screen
        setWorldPos(startPos.add(new Vector3(0, 1000, 0)));
    }

    /**
     * Create a new Magic with specified single delay.
     * @param startPos the location where the magic will be created
     * @param inaccuracy the maximum radius from the startpos where the magic can be created
     * @param targetYpos the y-position where the magic will disappear
     * @param lifespan the number of acts this magic will last (use for stationary magic)
     * @param delay the delay after which the magic will be created
     */
    public Magic(Vector3 startPos, double inaccuracy, int targetYpos, int lifespan,
                 int delay) {
        this(startPos, inaccuracy, targetYpos, lifespan, delay, delay);
    }

    @Override
    public void update() {
        if (delay > 0) {
            delay--;
            return;
        }
        if (delay == 0) {
            delay--;
            setWorldPos(startPos);
            return;
        }
        lifespan--;
        actionUpdate();
        if (startPos.y > targetYpos && getWorldPos().y < targetYpos
         || startPos.y < targetYpos && getWorldPos().y > targetYpos) {
            disappear();
        }
        if (lifespan <= 0) disappear();
    }

    /**
     * Actions to be performed each frame when existing.
     * <p>
     * For example, a meteor should fall and a gravity field should pull.
     */
    public abstract void actionUpdate();

    /**
     * Actions to be performed when disappearing.
     * <p>
     * Currently, this method only removes the object from the world. Feel free
     * to override this!
     */
    public void disappear() {
        getWorld().removeSprite(this);
    }
}
