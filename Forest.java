/**
 * 
 */
public enum Forest {
    DEFAUlT("default", 1, 10, 8);

    private int spawnRate;
    public int density;
    public int maxRadius;
    public String treeType;
    private static int[] spawnRates = new int[Forest.length()];

    private Forest(String treeType, int spawnRate, int density, int maxRadius) {
        this.spawnRate = spawnRate;
        this.density = density;
        this.treeType = treeType;
        this.maxRadius = maxRadius;
    }

    static {
        spawnRates[0] = values()[0].spawnRate;
        for (int i = 1; i < Forest.length(); i++) {
            spawnRates[i] = spawnRates[i - 1] + values()[i].spawnRate;
        }
    }

    /**
     * Get the number of different types of features.
     *
     * @return the number of different types of features
     */
    public static int length() {
        return values().length;
    }

    /**
     * Get the spawn rate of the given Forest type.
     *
     * @param i the index of the type
     * @return the spawn rate of the given type
     */
    public static int getSpawnRate(int i) {
        return spawnRates[i];
    }

}
