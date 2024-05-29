import greenfoot.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.stream.Stream;

/**
 * A type of world whose display image is an upscaled version of its canvas
 * image.
 * <p>
 * All rendering should be done to a PixelWorld's canvas, which may then be
 * displayed to the user in the Greenfoot window by calling
 * {@link #updateImage}.
 * <p>
 * This class handles separating objects of the Sprite class by layer for
 * rendering. This render order is separate from the paint order of all Actor
 * objects defined by {@link #setPaintOrder}.
 *
 * @author Martin Baldwin
 * @author Andrew Wang
 * @version April 2024
 */
public abstract class PixelWorld extends World {
    /** The scale factor of all PixelWorld display images. */
    public static final int PIXEL_SCALE = 4;

    private final int worldWidth;
    private final int worldHeight;
    private final GreenfootImage canvas;

    // All sprites in this world mapped by their classes, for efficient access
    private Map<Class<? extends Sprite>, List<Sprite>> spritesByClass;
    // Sprite objects mapped by their assigned layer, for rendering order
    private Map<Layer, List<Sprite>> spritesByLayer;

    /**
     * Creates a new PixelWorld with the specified dimensions.
     * <p>
     * All PixelWorld objects use a Greenfoot cell size of 1 and are unbounded.
     *
     * @param worldWidth the width of this world, in canvas pixels
     * @param worldHeight the height of this world, in canvas pixels
     */
    public PixelWorld(int worldWidth, int worldHeight) {
        super(worldWidth * PIXEL_SCALE, worldHeight * PIXEL_SCALE, 1, false);
        canvas = new GreenfootImage(worldWidth, worldHeight);
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        spritesByClass = new HashMap<Class<? extends Sprite>, List<Sprite>>();
        spritesByLayer = new EnumMap<Layer, List<Sprite>>(Layer.class);
        for (Layer layer : Layer.values()) {
            spritesByLayer.put(layer, new ArrayList<Sprite>());
        }
    }

    /**
     * Returns the canvas image of this world, the GreenfootImage that is scaled
     * and displayed as this world's display image.
     * <p>
     * This image has dimensions matching the size specified when constructing
     * the world, and all rendering should be done to this image.
     *
     * @return the canvas image of this world, before scaling
     */
    public GreenfootImage getCanvas() {
        return canvas;
    }

    @Override
    public void act() {
        update();
        render();
        updateImage();
    }

    /**
     * Draws the display image of this world.
     * <p>
     * The canvas image of this world is scaled and drawn onto the world
     * background. This method should be called after all world rendering has
     * been done.
     */
    public void updateImage() {
        GreenfootImage scaled = new GreenfootImage(canvas);
        scaled.scale(worldWidth * PIXEL_SCALE, worldHeight * PIXEL_SCALE);
        setBackground(scaled);
    }

    /**
     * Perform any logic for this world.
     */
    public abstract void update();

    /**
     * Perform any rendering for this world.
     */
    public abstract void render();

    /**
     * Update all Sprites currently in this world by layer. Sprites are updated
     * by calling the {@link Sprite#update} method.
     * <p>
     * Update order is the same as render order.
     */
    public void updateSprites() {
        for (List<Sprite> layerSprites : spritesByLayer.values()) {
            for (Sprite sprite : layerSprites) {
                sprite.update();
            }
        }
    }

    /**
     * Renders all Sprites currently in this world by layer. Sprites are
     * rendered by calling the {@link Sprite#render} method on this world's
     * canvas.
     * <p>
     * Render order is defined by the order of layers in the {@link Layer} enum.
     *
     * @see Layer
     */
    public void renderSprites() {
        for (List<Sprite> layerSprites : spritesByLayer.values()) {
            for (Sprite sprite : layerSprites) {
                sprite.render(canvas);
            }
        }
    }

    /**
     * Add a Sprite to this world, storing it for efficient access.
     *
     * @param sprite the sprite to add
     * @param x the x coordinate of the position where the sprite is added
     * @param y the y coordinate of the position where the sprite is added
     */
    public void addSprite(Sprite sprite, int x, int y) {
        // Add this sprite to the list for its class
        List<Sprite> list = spritesByClass.get(sprite.getClass());
        if (list == null) {
            list = new ArrayList<Sprite>();
            spritesByClass.put(sprite.getClass(), list);
        }
        list.add(sprite);

        // Add sprites to the list for their layers
        spritesByLayer.get(sprite.getLayer()).add(sprite);

        sprite.addedToWorld(this);
        sprite.setWorld(this);
        sprite.setScreenPos(x, y);
    }

    /**
     * Removes a Sprite from this world.
     *
     * @param sprite the sprite to remove
     */
    public void removeSprite(Sprite sprite) {
        // Remove this sprite from the list for its class
        spritesByClass.get(sprite.getClass()).remove(sprite);

        // Remove sprite from the list for their layers
        spritesByLayer.get(sprite.getLayer()).remove(sprite);

        sprite.setWorld(null);
    }

    /**
     * Get all sprites of a particular class in this world.
     *
     * @param cls the class of sprites to look for, or {@code null} to find all
     *            sprites
     * @return a list of sprites in this world that are instances of the given
     *         class
     */
    public List<? extends Sprite> getSprites(Class<? extends Sprite> cls) {
        List<Sprite> result = new ArrayList<>();
        if (cls == null) {
            // Add all sprites to result list
            for (List<Sprite> list : spritesByClass.values()) {
                result.addAll(list);
            }
        } else {
            // Add all sprites of classes that are subclasses of or the same as
            // the given class
            for (Class<? extends Sprite> keyCls : spritesByClass.keySet()) {
                if (cls.isAssignableFrom(keyCls)) {
                    result.addAll(spritesByClass.get(keyCls));
                }
            }
        }
        return result;
    }

    /**
     * Get all sprites of a particular class in this world as a stream.
     * <p>
     * This is helpful if you need to iterate over the sprites after obtaining
     * them, so that the iteration does not occur twice.
     * <p>
     * There is generally no need to call this method as using it requires an
     * understanding of the Stream API.
     *
     * @param cls the class of sprites to look for, or {@code null} to find all
     *            sprites
     * @return a stream of sprites in this world that are instances of the given
     *         class
     */
    public Stream<? extends Sprite> getSpritesAsStream(Class<? extends Sprite> cls) {
        if (cls == null) {
            return spritesByClass.values().stream().flatMap(List::stream);
        }
        return spritesByClass.keySet().stream()
            .filter(cls::isAssignableFrom)
            .flatMap(keyCls -> spritesByClass.get(keyCls).stream());
    }

    public List<? extends Sprite> getSpritesByLayer(Layer layer) {
        return spritesByLayer.get(layer);
    }

    /**
     * Return all Sprites of the specified class within a specified radius
     * around a point. An Sprite is within range if the distance between its
     * screen position and the given point is less than or equal to the given
     * radius.
     *
     * @param radius the radius of the circle, in canvas pixels
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @param cls the class of Sprites to look for, or {@code null} for all
     *            types of Sprites
     * @return a list of Sprites in this actor's world of the given class within
     *         the given range
     */
    public List<Sprite> getSpritesInRange(int radius, double x, double y, Class<? extends Sprite> cls) {
        List<Sprite> result = new ArrayList<>();
        for (Sprite sprite : getSprites(cls)) {
            if (sprite.distanceTo(x, y) <= radius) {
                result.add(sprite);
            }
        }
        return result;
    }

    /**
     * Gets the width of the downscaled world.
     * <p>
     * This is not the width of the final world that is displayed to the user.
     * To calculate that value, multiply the value returned by this method by
     * PixelWorld.PIXEL_SCALE.
     *
     * @return the width of the world, before scaling
     */
    @Override
    public int getWidth() {
        return worldWidth;
    }

    /**
     * Gets the height of the downscaled world.
     * <p>
     * This is not the height of the final world that is displayed to the user.
     * To calculate that value, multiply the value returned by this method by
     * PixelWorld.PIXEL_SCALE.
     *
     * @return the height of the world, before scaling
     */
    @Override
    public int getHeight() {
        return worldHeight;
    }
}
