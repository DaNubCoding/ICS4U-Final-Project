/**
 * Separate layers which PixelActor objects may belong to, defining the
 * order in which they are rendered.
 * <p>
 * Actors belonging to later-defined layers in this enum are drawn on top of
 * earlier-defined layers.
 *
 * @author Martin Baldwin
 * @author Andrew Wang
 * @version May 2024
 */
public enum Layer {
    SPRACK_DEFAULT,
    UI;
}
