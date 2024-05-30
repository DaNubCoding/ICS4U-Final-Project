/**
 * TODO: a description for this class
 * @author Lucas Fu
 * @version May 2024
 */
public class WorldElement extends Sprack{
    private long id; // note: this might not be necessary
    /**
     * Create a new WorldElement with the given id and the given sheet name.
     * <p>
     * Refer to {@link Sprack#Sprack}
     * @param id the unique id used to identify this element when regenerating the world
     */
    public WorldElement(long id, String sheetName){
        super(sheetName);
        this.id = id;
    }

    public void modify(){
        /**
         * ideally this should be part of an interface, but I'm not well versed
         * enough in interfaces to deal with this. This should modify the world 
         * element (such as having it destroyed)
         */

         // currently empty, as you can see...
    }
}
