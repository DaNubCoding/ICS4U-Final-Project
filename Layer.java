/**
 * Separate layers which PixelActor objects may belong to, defining the
 * order in which they are rendered.
 * <p>
 * Actors belonging to later-defined layers in this enum are drawn on top of
 * earlier-defined layers.
 *
 * @author Martin Baldwin
 * @version May 2024
 */
public enum Layer {
    GROUND,
    SPRACK_DEFAULT,
    HEALTH_BAR,
    SPRACK_CANOPY,
    UI;
}
