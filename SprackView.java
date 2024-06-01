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
 * @version May 2024
 */
public class SprackView {
    /** All existing SprackView objects for use in the game, mapped by name. */
    private static final ConcurrentMap<String, SprackView> viewMap = new ConcurrentHashMap<>();

    /** The number of different rotation angles, evenly spaced, to make available in the cache. */
    private static final int IMAGE_CACHE_ANGLE_COUNT = 180;

    /**
     * Load and cache all SprackView objects to be used in the game.
     */
    public static void loadAll() {
        new Thread(() -> {
            Map<String, Integer> sheetInfo = new HashMap<>();
            sheetInfo.put("car", 9);
            sheetInfo.put("crate", 16);
            sheetInfo.put("tree", 40);
            sheetInfo.put("tower", 200);
            sheetInfo.put("knight", 21);

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
     * i / IMAGE_CACHE_ANGLE_COUNT * 360.
     */
    private final CacheEntry[] rotCache;

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
        // Get rotated dimensions of layer images
        int rotWidth = (int) (Math.abs(layerWidth * Math.cos(imageRad)) + Math.abs(layerHeight * Math.sin(imageRad)));
        int rotHeight = (int) (Math.abs(layerWidth * Math.sin(imageRad)) + Math.abs(layerHeight * Math.cos(imageRad)));
        if (rotWidth < layerWidth) {
            rotWidth = layerWidth;
        }
        if (rotHeight < layerHeight) {
            rotHeight = layerHeight;
        }
        // Draw layers onto an image
        GreenfootImage image = new GreenfootImage(rotWidth, rotHeight + layers.length);
        for (int i = 0; i < layers.length; i++) {
            GreenfootImage rotLayer = new GreenfootImage(rotWidth, rotHeight);
            rotLayer.drawImage(layers[i], (rotWidth - layerWidth) / 2, (rotHeight - layerHeight) / 2);
            rotLayer.rotate((int) imageDegrees);
            image.drawImage(rotLayer, 0, layers.length - 1 - i);
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
        int scaledWidth = (int) (cachedImage.getWidth() * scale);
        int scaledHeight = (int) (cachedImage.getHeight() * scale);
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
        return (int) (rotCache[getCacheIndex(rotation)].image.getWidth() * scale);
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
        return (int) (rotCache[getCacheIndex(rotation)].image.getHeight() * scale);
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
        return (int) (rotCache[getCacheIndex(rotation)].centerX * scale);
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
        return (int) (rotCache[getCacheIndex(rotation)].centerY * scale);
    }
}
