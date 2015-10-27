/**
 * File: BGTile.java
 * Authors: B. Adam, C. Buescher, T. Pickens, C. Schmunsler
 * Last Modified By: 
 */

/**
 * BGTile: Level background tile
 */
public class HealthBar extends Entity {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4081052719686701412L;
    private static final int TILESIZE = 16;
    private Camera cam;
    private Entity player;
    

    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public HealthBar(Game g, String anImage, Camera c, Entity p) {
        super(g,anImage, (int) p.getMinX(), (int) p.getMinY());
        cam = c;
        player = p;
    }
    
    @Override
    public void update(long time) {
        float c = cam.getPosX() + 20;
        float p = player.getMidX()-149.5f;
        if(p < c-2) {
            setMinX(c);
        }
        else if(p > c+2) {
            setMinX(c);
        }
        else {
            setMinX(p);
        }
        setMinY(cam.getPosY()+20);
    }
    
}
