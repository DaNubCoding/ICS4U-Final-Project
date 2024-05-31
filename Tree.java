import greenfoot.Greenfoot;

/**
 * TODO: add documentation for this and do some implementation
 * @author Lucas Fu
 * @version May 2024
 */
public class Tree extends Feature {
    private String type;

    /**
     * Create a new Tree with specified type.
     * @param treeType the type of this tree
     */
    public Tree(String treeType) {
        super("tree");
        type = treeType;
    }

    /**
     * Creates a default tree. ***DEFAULT TREES SHOULD NOT EXIST***
     */
    public Tree() {
        super("tree");
        type = "default";
        setWorldRotation(Greenfoot.getRandomNumber(360));
    }

    /**
     * Get the tree type.
     * @return what kind of tree this is
     */
    public String getTreeType() {
        return type;
    }
}
