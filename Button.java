import greenfoot.*;

/**
 * A button that displays text or an image.
 * <p>
 * When passing in the method, it must be in the following format:
 * {@code new Button("TEXT", CLASS_NAME::METHOD_NAME)}
 * <p>
 * Where {@code CLASS_NAME} is the name of the class that contains the method
 * to be called, and {@code METHOD_NAME} is the name of the method to be called.
 * <p>
 * For example:
 * {@code new Button("Start Game", SprackWorld::start)}
 *
 * @author Andrew Wang
 * @author Martin Baldwin
 * @version April 2024
 */
public class Button extends Sprite {
    private GreenfootImage icon;
    private Runnable method;
    private boolean mouseDownOnThis;
    private boolean hovering;
    private GreenfootImage idleImage;
    private GreenfootImage hoverImage;
    private GreenfootImage clickImage;

    /**
     * Create a button that displays a text and runs a method when clicked.
     *
     * @param text The text string to display on the button
     * @param method The method that is ran when the button is clicked
     */
    public Button(String text, Runnable method) {
        this(Text.createStringImage(text), method);
    }

    /**
     * Create a button that displays an image and runs a method when clicked.
     *
     * @param icon The GreenfootImage to display on the button
     * @param method The method that is ran when the button is clicked
     */
    public Button(GreenfootImage icon, Runnable method) {
        super(Layer.UI);
        setIcon(icon);
        this.method = method;
    }

    /**
     * Set the text label of the button to the given string.
     *
     * @param text The text string to display on the button
     */
    public void setText(String text) {
        setIcon(Text.createStringImage(text));
    }

    /**
     * Set the icon of the button to the given image.
     *
     * @param icon The GreenfootImage to display on the button
     */
    public void setIcon(GreenfootImage icon) {
        this.icon = icon;
        idleImage = getIdleImage();
        hoverImage = getHoverImage();
        clickImage = getClickImage();
        if (mouseDownOnThis) {
            setImage(clickImage);
        } else if (hovering) {
            setImage(hoverImage);
        } else {
            setImage(idleImage);
        }
    }

    @Override
    public void update() {
        MouseInfo mouseInfo = Greenfoot.getMouseInfo();
        if (mouseInfo == null) return;

        boolean isMouseOver = mouseOver(mouseInfo);
        if (isMouseOver && !hovering) {
            setImage(hoverImage);
        } else if (!isMouseOver && hovering) {
            setImage(idleImage);
        }
        hovering = isMouseOver;

        if (mouseInfo.getButton() == 1) {
            if (Greenfoot.mousePressed(null) && hovering) { // mouse down
                mouseDownOnThis = true;
                setImage(clickImage);
            } else if (Greenfoot.mouseClicked(null)) { // mouse up
                if (hovering && mouseDownOnThis) {
                    mouseDownOnThis = false;
                    setImage(idleImage);
                    method.run();
                }
                mouseDownOnThis = false;
            }
        }
    }

    /**
     * Determine if the mouse is hovering over the button.
     *
     * @param mouseInfo The MouseInfo object obtained from Greenfoot
     * @return True if mouse is hovering over the button, false otherwise
     */
    private boolean mouseOver(MouseInfo mouseInfo) {
        int mouseX = mouseInfo.getX() / PixelWorld.PIXEL_SCALE;
        int mouseY = mouseInfo.getY() / PixelWorld.PIXEL_SCALE;
        int w = getImage().getWidth(), h = getImage().getHeight();
        boolean checkX = mouseX > getScreenX() - w / 2 && mouseX < getScreenX() + w / 2;
        boolean checkY = mouseY > getScreenY() - h / 2 && mouseY < getScreenY() + h / 2;
        return checkX && checkY;
    }

    /**
     * Get the image of the button when idle.
     *
     * @return The image of the button when idle
     */
    private GreenfootImage getIdleImage() {
        // Create the button image a bit larger than the icon image
        GreenfootImage image = new GreenfootImage((int) (icon.getWidth() * 1.6), (int) (icon.getHeight() * 1.6));

        // Fill background and draw border
        image.setColor(new Color(228, 228, 228, 200));
        image.fill();
        image.setColor(new Color(0, 0, 0));
        image.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);

        // Draw the icon at the center
        image.drawImage(icon, (int) (icon.getWidth() * 0.3), (int) (icon.getHeight() * 0.3));
        return image;
    }

    /**
     * Get the image of the button when the mouse if hovering.
     *
     * @return The image of the button when hovering
     */
    private GreenfootImage getHoverImage() {
        // Create the button image a bit larger than the icon image
        GreenfootImage image = new GreenfootImage((int) (icon.getWidth() * 1.6 + 2), (int) (icon.getHeight() * 1.6 + 2));

        // Fill background and draw border
        image.setColor(new Color(240, 240, 228, 200));
        image.fill();
        image.setColor(new Color(0, 0, 0));
        image.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);

        // Draw the icon at the center
        image.drawImage(icon, (int) (icon.getWidth() * 0.3 + 1), (int) (icon.getHeight() * 0.3) + 1);
        return image;
    }

    /**
     * Get the image of the button when it is being clicked.
     *
     * @return The image of the button when clicked
     */
    private GreenfootImage getClickImage() {
        // Create the button image a bit larger than the icon image
        GreenfootImage image = new GreenfootImage((int) (icon.getWidth() * 1.6), (int) (icon.getHeight() * 1.6));

        // Fill background and draw border
        image.setColor(new Color(255, 255, 255, 248));
        image.fill();
        image.setColor(new Color(0, 0, 0));
        image.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);

        // Draw the icon at the center
        image.drawImage(icon, (int) (icon.getWidth() * 0.3), (int) (icon.getHeight() * 0.3));
        return image;
    }
}
