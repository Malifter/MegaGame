/**
 * File: BGTile.java
 * Authors: B. Adam, C. Buescher, T. Pickens, C. Schmunsler
 * Last Modified By: 
 */

/**
 * BGTile: Level background tile
 */
public class BGTile extends Entity {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4081052719686701412L;
    private static final int TILESIZE = 16;
    

    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public BGTile(Game g, String anImage, int x, int y) {
        super(g,anImage,x*TILESIZE,y*TILESIZE);
    }
    
}
