/**
 * File: FGTile.java
 * Authors: B. Adam, C. Buescher, T. Pickens, C. Schmunsler
 * Last Modified By: 
 */

/**
 * FGTile: <add description> 
 */
public class FGTile extends Entity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4081052719686701412L;
    private static final int TILESIZE = 16;
    
    /**
     * Constructor: <add description>
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public FGTile(Game g, String anImage, int x, int y) {
        super(g, anImage, x*TILESIZE, y*TILESIZE);
        // TODO Auto-generated constructor stub
    }
    
}
