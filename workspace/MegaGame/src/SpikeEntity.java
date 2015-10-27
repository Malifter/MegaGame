import java.util.Random;


/**
 * SpikeEntity: spike traps
 */
public class SpikeEntity extends Entity {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4363795480195834006L;
    MegaManGame game;
    private float direction = 0;
    private long directionTime = 0;
    private String imageArray[] = {"spikeFloor.gif","spikeCeiling.gif","spikeLeft.gif","spikeRight.gif"};
    private int imageIndex = 0;
    private boolean facingRight = true;
    private static final int COLLISION_DAMAGE = 10;
    private Entity player;
    
    public SpikeEntity(Game g, String file, int iX, int iY, int x, int y, int w, int h, Entity player) {
        super(g, file, iX, iY);
        game = (MegaManGame) g;
        image = imageArray[0];
        minX = x;
        minY = y;
        width = w;
        height = h;
        offsetX = Math.abs(imageX - minX);
        offsetY = Math.abs(imageY - minY);
        calculateBounds();
        setHealthPoints(0);
        damage = COLLISION_DAMAGE;
        this.player = player;
    }
    
    @Override
    public void update(long time) {
        this.reloadSprite();
    }
}