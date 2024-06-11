import greenfoot.*;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A container of cached sprite stack images pre-rendered from many angles for
 * efficient use with the Sprack class. SprackView objects are immutable, and
 * only one object per sprite stack sheet should be created, since a new cache
 * of images will be created for each instance.
 *
 * @author Martin Baldwin
 * @version June 2024
 */
public class SprackView {
    /** All existing SprackView objects for use in the game, mapped by name. */
    private static final ConcurrentMap<String, SprackView> viewMap = new ConcurrentHashMap<>();

    /**
     * The number of different rotation angles, evenly spaced, to make available
     * in the cache.
     */
    private static final int IMAGE_CACHE_ANGLE_COUNT = 120;

    /**
     * The scale factor of the images within a SprackView object's cache.
     * If spracks are transformed using a scale larger than this value, they may
     * appear blocky. Smaller values decrease the time required to create a
     * SprackView object's cache.
     */
    public static final double IMAGE_CACHE_SCALE = 6;

    /**
     * The number of layers per vertical voxel in a sprite stack. The higher
     * this value is, the less noticeable the separation between layers will be.
     * This will only make a difference when it is less than or equal to
     * {@link #IMAGE_CACHE_SCALE}.
     */
    public static final int LAYERS_PER_PIXEL = 6;

    /**
     * Load and cache all SprackView objects to be used in the game.
     */
    public static void loadAll() {
        new Thread(() -> {
            Map<String, Integer> sheetInfo = new HashMap<>();
            sheetInfo.put("crate", 16);
            sheetInfo.put("tree_oak_trunk", 29);
            sheetInfo.put("tree_oak_canopy", 21);
            sheetInfo.put("tree_willow_trunk", 29);
            sheetInfo.put("tree_willow_canopy", 31);
            sheetInfo.put("knight_standing", 24);
            sheetInfo.put("knight_walk1", 24);
            sheetInfo.put("knight_walk2", 24);
            sheetInfo.put("knight_walk3", 24);
            sheetInfo.put("knight_walk4", 24);
            sheetInfo.put("knight_dash", 21);
            sheetInfo.put("tombstone", 14);
            sheetInfo.put("statue_dormant", 27);
            sheetInfo.put("statue_active", 29);
            sheetInfo.put("statue_activating1", 29);
            sheetInfo.put("statue_activating2", 29);
            sheetInfo.put("statue_attack1", 23);
            sheetInfo.put("statue_attack2", 29);
            sheetInfo.put("statue_attack3", 26);
            sheetInfo.put("statue_attack4", 28);
            sheetInfo.put("statue_attack5", 28);
            sheetInfo.put("jester_idle", 32);
            sheetInfo.put("jester_walk1", 32);
            sheetInfo.put("jester_walk2", 32);
            sheetInfo.put("jester_guard", 32);
            sheetInfo.put("jester_counter", 32);
            sheetInfo.put("jester_stab", 32);
            sheetInfo.put("jester_spin", 32);
            sheetInfo.put("jester_flip1", 31);
            sheetInfo.put("jester_flip2", 29);
            sheetInfo.put("jester_flip3", 31);
            sheetInfo.put("jester_flip4", 28);

            ExecutorService service = Executors.newFixedThreadPool(sheetInfo.size());
            for (Map.Entry<String, Integer> entry : sheetInfo.entrySet()) {
                final String name = entry.getKey();
                final GreenfootImage sheet = new GreenfootImage(name + ".png");
                final int layerCount = entry.getValue();
                service.execute(() -> {
                    System.out.println("start " + name);
                    viewMap.put(name, new SprackView(sheet, layerCount));
                    System.out.println("done " + name);
                });
            }
            service.shutdown();
            try {
                service.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                service.shutdownNow();
            }
        }).start();
    }

    /** The width of an untransformed layer, in pixels. */
    private final int layerWidth;
    /** The height of an untransformed layer, in pixels. */
    private final int layerHeight;

    private static class CacheEntry {
        private final GreenfootImage image;
        private final int centerX;
        private final int centerY;

        private CacheEntry(GreenfootImage image, int centerX, int centerY) {
            this.image = image;
            this.centerX = centerX;
            this.centerY = centerY;
        }
    }

    /**
     * Images of the sprite stack at different rotation angles. rotCache[i]
     * contains the image representing the sprite stack at an angle of
     * i / IMAGE_CACHE_ANGLE_COUNT * 360, scaled by a factor of IMAGE_CACHE_SCALE.
     */
    private final CacheEntry[] rotCache;
    private final int layerCount;

    /**
     * Create a new cache for a sprite stack created by layering the specified
     * number of layers from the given layer sheet image.
     *
     * @param layerSheet a GreenfootImage containing individual layers arranged
     *                   vertically from bottom to top
     * @param layerCount the number of layers to extract from the given layer
     *                   sheet
     */
    public SprackView(GreenfootImage layerSheet, int layerCount) {
        this.layerCount = layerCount;

        // Create individual layer images from sheet
        layerWidth = layerSheet.getWidth();
        layerHeight = layerSheet.getHeight() / layerCount;
        GreenfootImage[] layers = new GreenfootImage[layerCount];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new GreenfootImage(layerWidth, layerHeight);
            layers[i].drawImage(layerSheet, 0, -layerHeight * (layerCount - 1 - i));
        }

        // Create rotated image cache
        rotCache = new CacheEntry[IMAGE_CACHE_ANGLE_COUNT];
        final int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(processors);
        for (int i = 0; i < IMAGE_CACHE_ANGLE_COUNT; i++) {
            final int index = i;
            final double rot = 360.0 / IMAGE_CACHE_ANGLE_COUNT * index;
            service.execute(() -> {
                rotCache[index] = createCacheEntry(rot, layers, layerWidth, layerHeight);
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            service.shutdownNow();
        }
    }

    /**
     * Create a new cache entry for a sprite stack given the rotation angle and
     * individual layer images.
     *
     * @param imageDegrees the rotation angle of the sprite stack, in degrees
     * @param layers the individual layer images of the sprite stack
     * @param layerWidth the width of an untransformed layer
     * @param layerHeight the height of an untransformed layer
     * @return a new CacheEntry object containing the pre-rendered sprite stack
     *         image
     */
    public static CacheEntry createCacheEntry(double imageDegrees, GreenfootImage[] layers, int layerWidth, int layerHeight) {
        double imageRad = Math.toRadians(imageDegrees);
        // Get scaled dimensions of layers
        int width = (int) (layerWidth * IMAGE_CACHE_SCALE);
        int height = (int) (layerHeight * IMAGE_CACHE_SCALE);
        // Get rotated and scaled dimensions of layer images
        int rotWidth = (int) (Math.abs(width * Math.cos(imageRad)) + Math.abs(height * Math.sin(imageRad)));
        int rotHeight = (int) (Math.abs(width * Math.sin(imageRad)) + Math.abs(height * Math.cos(imageRad)));
        if (rotWidth < width) {
            rotWidth = width;
        }
        if (rotHeight < height) {
            rotHeight = height;
        }
        // Draw layers onto an image
        GreenfootImage image = new GreenfootImage(rotWidth, rotHeight + (int) (layers.length * IMAGE_CACHE_SCALE));
        for (int i = 0; i < layers.length; i++) {
            GreenfootImage layer = new GreenfootImage(layers[i]);
            layer.scale(width, height);
            GreenfootImage rotLayer = new GreenfootImage(rotWidth, rotHeight);
            rotLayer.drawImage(layer, (rotWidth - width) / 2, (rotHeight - height) / 2);
            rotLayer.rotate((int) imageDegrees);
            for (int j = 0; j < LAYERS_PER_PIXEL; j++) {
                image.drawImage(rotLayer, 0, (int) (IMAGE_CACHE_SCALE * (layers.length - 1 - i) - j * IMAGE_CACHE_SCALE / LAYERS_PER_PIXEL));
            }
            // image.drawImage(rotLayer, 0, (int) (IMAGE_CACHE_SCALE * (layers.length - 1 - i)));
        }
        return new CacheEntry(image, rotWidth / 2, image.getHeight() - rotHeight / 2);
    }

    /**
     * Get the SprackView objects with the given name.
     *
     * @param sheetName the name of the SprackView object
     * @return the SprackView with the given name, or null if it doesn't exist
     */
    public static SprackView getView(String sheetName) {
        return viewMap.get(sheetName);
    }

    /**
     * Return the index into a SprackView object's cache arrays that corresponds
     * to the given rotation angle.
     *
     * @param rotation the rotation angle of the sprite stack, in degrees
     * @return the index into cache arrays to access the data corresponding to the given angle
     */
    private static int getCacheIndex(double rotation) {
        return (int) (Vector2.normalizeAngle(rotation) / 360.0 * IMAGE_CACHE_ANGLE_COUNT);
    }

    /**
     * Return a GreenfootImage representing the sprite stack of this SprackView
     * rotated and scaled by the specified amounts, or null if the image would
     * be empty.
     *
     * @param rotation the rotation angle of the sprite stack, in degrees
     * @param scale the scale factor of the sprite stack
     * @return a new GreenfootImage representing the sprite stack, or null if the image would be empty
     * @throws UnsupportedOperationException if the given scale factor is larger than the scale factor of the image cache
     */
    public GreenfootImage getTransformedImage(double rotation, double scale) {
        GreenfootImage cachedImage = rotCache[getCacheIndex(rotation)].image;
        int scaledWidth = (int) (cachedImage.getWidth() / IMAGE_CACHE_SCALE * scale);
        int scaledHeight = (int) (cachedImage.getHeight() / IMAGE_CACHE_SCALE * scale);
        if (scaledWidth <= 0 || scaledHeight <= 0) {
            return null;
        }
        GreenfootImage image = new GreenfootImage(cachedImage);
        image.scale(scaledWidth, scaledHeight);
        return image;
    }

    /**
     * Return the screen width of the sprite stack of this SprackView rotated
     * and scaled by the specified amounts.
     * <p>
     * This is a less expensive operation compared to calling
     * {@link GreenfootImage#getWidth} on the image returned by
     * {@link #getTransformedImage}.
     *
     * @param rotation the rotation angle of the sprite stack, in degrees
     * @param scale the scale factor of the sprite stack
     * @return the width of the image that would be used if the same parameters
     *         were passed to {@link #getTransformedImage}
     */
    public int getTransformedWidth(double rotation, double scale) {
        return (int) (rotCache[getCacheIndex(rotation)].image.getWidth() / IMAGE_CACHE_SCALE * scale);
    }

    /**
     * Return the screen height of the sprite stack of this SprackView rotated
     * and scaled by the specified amounts.
     * <p>
     * This is a less expensive operation compared to calling
     * {@link GreenfootImage#getHeight} on the image returned by
     * {@link #getTransformedImage}.
     *
     * @param rotation the rotation angle of the sprite stack, in degrees
     * @param scale the scale factor of the sprite stack
     * @return the height of the image that would be used if the same parameters
     *         were passed to {@link #getTransformedImage}
     */
    public int getTransformedHeight(double rotation, double scale) {
        return (int) (rotCache[getCacheIndex(rotation)].image.getHeight() / IMAGE_CACHE_SCALE * scale);
    }

    /**
     * Return the positional horizontal screen center of the sprite stack of
     * this SprackView rotated and scaled by the specified amounts.
     * <p>
     * The offset returned is located at the center of the sprite stack.
     *
     * @param rotation the rotation angle of the sprite stack, in degrees
     * @param scale the scale factor of the sprite stack
     * @return the screen x offset of the sprite stack's center, relative to the
     *         image that would be used if the same parameters were passed to
     *         {@link #getTransformedImage}
     */
    public int getCenterX(double rotation, double scale) {
        return (int) (rotCache[getCacheIndex(rotation)].centerX / IMAGE_CACHE_SCALE * scale);
    }

    /**
     * Return the positional vertical screen center of the sprite stack of
     * this SprackView rotated and scaled by the specified amounts.
     * <p>
     * The offset returned is located at the center of the bottommost layer of
     * the sprite stack.
     *
     * @param rotation the rotation angle of the sprite stack, in degrees
     * @param scale the scale factor of the sprite stack
     * @return the screen y offset of the sprite stack's center, relative to the
     *         image that would be used if the same parameters were passed to
     *         {@link #getTransformedImage}
     */
    public int getCenterY(double rotation, double scale) {
        return (int) (rotCache[getCacheIndex(rotation)].centerY / IMAGE_CACHE_SCALE * scale);
    }

    /**
     * Return the number of layers in the sprite stack of this SprackView.
     * <p>
     * This can be considered the height of the sprite stack.
     *
     * @return the number of layers in the sprite stack
     */
    public int getLayerCount() {
        return layerCount;
    }
}
