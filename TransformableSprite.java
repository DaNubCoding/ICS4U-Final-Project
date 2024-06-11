import greenfoot.Color;
import greenfoot.GreenfootImage;

/**
 * A sprite that can be transformed, including rotation, mirroring, and scaling.
 * <p>
 * Supports center of rotation.
 *
 * @author Andrew Wang
 * @version May 2024
 */
public abstract class TransformableSprite extends Sprite {
    private static final boolean DEBUG_SHOW_IMAGE_BOUNDS = false;

    // The original upright image
    private GreenfootImage originalImage;
    private double originalWidth;
    private double originalHeight;
    private Vector2 centerOfRotation;
    // See createExpandedImage()
    private GreenfootImage expandedImage;
    // See createExpandedImage()
    private int maxDimension;
    // The image after transformations (rotation and mirror)
    private GreenfootImage transformedImage;
    // The width and height of the image after transformations
    private double transformedWidth;
    private double transformedHeight;
    // The rotation of the image
    private double screenRotation;
    // Whether the image is mirrored in either axis
    private boolean mirrorX;
    private boolean mirrorY;
    // The scale of the image
    private double scale;
    // The transparency of the sprite
    private int transparency;

    /**
     * Create a new TransformableSprite on the given {@link Layer}.
     *
     * @param layer the Layer to render this sprite on
     * @see Layer
     */
    public TransformableSprite(Layer layer) {
        super(layer);
        screenRotation = 0;
        mirrorX = mirrorY = false;
        scale = 1;
        transparency = 255;
    }

    /**
     * Render the sprite with the center of rotation in mind.
     *
     * @param canvas the GreenfootImage to render on
     */
    @Override
    public void render(GreenfootImage canvas) {
        double finalWidth = transformedWidth * scale;
        double finalHeight = transformedHeight * scale;
        Vector2 center = getImageOffsetGlobalPosition(new Vector2(originalWidth / 2, originalHeight / 2));
        Vector2 imagePos = center.subtract(new Vector2(finalWidth / 2, finalHeight / 2));
        GreenfootImage scaledImage = new GreenfootImage(transformedImage);
        scaledImage.setTransparency(transparency);
        scaledImage.scale((int) finalWidth, (int) finalHeight);
        canvas.drawImage(scaledImage, (int) Math.ceil(imagePos.x), (int) Math.ceil(imagePos.y));
        if (DEBUG_SHOW_IMAGE_BOUNDS) {
            canvas.setColor(Color.RED);
            canvas.fillRect((int) getScreenX(), (int) getScreenY(), 1, 1);
        }
    }

    /**
     * Set the screen rotation of this sprite.
     * <p>
     * The sprite will be rendered rotated by this amount.
     *
     * @param rotation the rotation to set
     */
    public void setScreenRotation(double rotation) {
        screenRotation = Vector2.normalizeAngle(rotation);
        createRotatedImage();
    }

    /**
     * Get the screen rotation of this sprite.
     * <p>
     * The rotation is in degrees, and is normalized to the range [0, 360).
     *
     * @return the rotation of this sprite
     */
    public double getScreenRotation() {
        return screenRotation;
    }

    /**
     * Set the original image of the sprite to a copy of a GreenfootImage.
     *
     * @param newImage The GreenfootImage to set as the new image
     */
    public void setOriginalImage(GreenfootImage newImage) {
        if (newImage == null) {
            originalImage = null;
            return;
        }
        originalWidth = newImage.getWidth();
        originalHeight = newImage.getHeight();
        if (DEBUG_SHOW_IMAGE_BOUNDS) {
            originalImage = new GreenfootImage((int) originalWidth, (int) originalHeight);
            originalImage.setColor(new Color(255, 0, 0, 64));
            originalImage.fill();
            originalImage.drawImage(newImage, 0, 0);
        } else {
            originalImage = new GreenfootImage(newImage);
        }
        centerOfRotation = new Vector2(originalWidth, originalHeight).divide(2);
        if (getMirrorX()) {
            originalImage.mirrorHorizontally();
            centerOfRotation = centerOfRotation.setX(originalWidth - 1 - centerOfRotation.x);
        }
        if (getMirrorY()) {
            originalImage.mirrorVertically();
            centerOfRotation = centerOfRotation.setY(originalHeight - 1 - centerOfRotation.y);
        }
        createExpandedImage();
        createRotatedImage();
    }

    /**
     * Set the point from which the image is rotated (center of rotation)
     * relative to the top-left corner of the original image.
     *
     * @param center the point to set as the center of rotation
     */
    public void setCenterOfRotation(Vector2 center) {
        centerOfRotation = center;
        if (mirrorX) centerOfRotation = center.setX(originalWidth - 1 - center.x);
        if (mirrorY) centerOfRotation = center.setY(originalHeight - 1 - center.y);
        createRotatedImage();
    }

    /**
     * Get the center of rotation of the image.
     *
     * @return the center of rotation
     */
    public Vector2 getCenterOfRotation() {
        return centerOfRotation;
    }

    /**
     * Generate the image that is a version of the centered image but with extra
     * margins, as to make it just large enough to contain any potential rotation
     * of the centered image.
     */
    private void createExpandedImage() {
        if (originalImage == null) return;
        maxDimension = (int) Math.ceil(Math.hypot(originalWidth, originalHeight));
        expandedImage = new GreenfootImage(maxDimension, maxDimension);
        int localX = (int) Math.floor((maxDimension - originalWidth) / 2);
        int localY = (int) Math.floor((maxDimension - originalHeight) / 2);
        if (DEBUG_SHOW_IMAGE_BOUNDS) {
            expandedImage.setColor(new Color(255, 255, 0, 64));
            expandedImage.fill();
        }
        expandedImage.drawImage(originalImage, localX, localY);
    }

    /**
     * Generate the rotated image while keeping it centered and expand the image
     * in order to fully contain the rotated image.
     */
    private void createRotatedImage() {
        if (originalImage == null) return;
        double angle = Math.toRadians(screenRotation);
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);
        transformedWidth = (int) Math.ceil((Math.abs(originalWidth * cosAngle) + Math.abs(originalHeight * sinAngle)));
        transformedHeight = (int) Math.ceil((Math.abs(originalWidth * sinAngle) + Math.abs(originalHeight * cosAngle)));

        GreenfootImage rotatedImage = new GreenfootImage(expandedImage);
        rotatedImage.rotate((int) screenRotation);
        transformedImage = new GreenfootImage((int) transformedWidth, (int) transformedHeight);
        int localX = (int) Math.floor((transformedWidth - maxDimension) / 2);
        int localY = (int) Math.floor((transformedHeight - maxDimension) / 2);
        if (DEBUG_SHOW_IMAGE_BOUNDS) {
            transformedImage.setColor(new Color(0, 255, 0, 64));
            transformedImage.fill();
        }
        transformedImage.drawImage(rotatedImage, localX, localY);
    }

    /**
     * Set the horizontal mirror of the image.
     *
     * @param mirror Whether to mirror or not
     */
    public void setMirrorX(boolean mirror) {
        if (mirrorX == mirror) return;
        if (originalImage != null) {
            originalImage.mirrorHorizontally();
        }
        final double mirroredX = originalWidth - 1 - centerOfRotation.x;
        centerOfRotation = centerOfRotation.setX(mirroredX);
        mirrorX = mirror;
        createExpandedImage();
        createRotatedImage();
    }

    /**
     * Set the vertical mirror of the image.
     *
     * @param mirror Whether to mirror or not
     */
    public void setMirrorY(boolean mirror) {
        if (mirrorY == mirror) return;
        if (originalImage != null) {
            originalImage.mirrorVertically();
        }
        final double mirroredY = originalHeight - 1 - centerOfRotation.y;
        centerOfRotation = centerOfRotation.setY(mirroredY);
        mirrorY = mirror;
        createExpandedImage();
        createRotatedImage();
    }

    /**
     * Get whether the image is mirrored horizontally.
     *
     * @return True if mirrored horizontally, false otherwise
     */
    public boolean getMirrorX() {
        return mirrorX;
    }

    /**
     * Get whether the image is mirrored vertically.
     *
     * @return True if mirrored vertically, false otherwise
     */
    public boolean getMirrorY() {
        return mirrorY;
    }

    /**
     * Set the scale of the image.
     *
     * @param scale the scale to set
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * Get the scale of the image.
     *
     * @return the scale of the image
     */
    public double getScale() {
        return scale;
    }

    /**
     * Get the world coordinates of a point relative to the top left corner of
     * this sprite's image AFTER the transformations of the image.
     *
     * @param offset the point relative to the top left corner of the image
     * @return the global coordinates of the transformed relative point
     */
    public Vector2 getImageOffsetGlobalPosition(Vector2 offset) {
        if (mirrorX) offset = offset.setX(originalWidth - 1 - offset.x);
        if (mirrorY) offset = offset.setY(originalHeight - 1 - offset.y);
        Vector2 rotatedOffset = offset.subtract(centerOfRotation).rotate(screenRotation);
        rotatedOffset = rotatedOffset.multiply(scale);
        return getScreenPos().add(rotatedOffset);
    }

    /**
     * Update the image of this sprite to the transformed image.
     * <p>
     * Call this method at the end of the act method.
     */
    public void updateImage() {
        setImage(transformedImage);
    }

    /**
     * Set the transparency of the Sprite.
     *
     * @param transparency the transparency of the Sprite, from 0 (invisible) to
     *                     255 (opaque)
     */
    public void setTransparency(int transparency) {
        this.transparency = transparency;
    }
}
