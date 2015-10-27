/**
 * An entity representing a shot fired by the player's ship
 * 
 * @author Kevin Glass
 * @author Brian Matzon
 */
public class ShotEntity extends Entity {
    
    private static final int MIN_RANGE = 168;

    /** The vertical speed at which the players shot moves */
    private float moveSpeedX = 250;
    
    //hardcoded for cannon
    private float moveSpeedY = 250;
    
    /** The game in which this entity exists */
    private MegaManGame game;
    
    //going to be used to reference when bullets should be delete/are off screen
    private Entity player = null;
    
    private static int range;
    
    /**
     * Create a new shot from the player
     * 
     * @param game
     *            The game in which the shot has been created
     * @param sprite
     *            The sprite representing this shot
     * @param x
     *            The initial x location of the shot
     * @param y
     *            The initial y location of the shot
     */
    public ShotEntity(Game g, String sprite, int x, int y, boolean isRight, Entity player, int damage, int range) {
        super(g, sprite, x, y);
        this.player = player;
        this.damage = damage;
        remove = false;
        if(range < MIN_RANGE) {
            this.range = MIN_RANGE;
        }
        else {
            this.range = range;
        }
        if(isRight)
            dx = moveSpeedX;
        else
            dx = -moveSpeedX;
        
        dy = 0;
        
        image = "shot.gif";
        this.game = (MegaManGame) g;
        
    }
    
    /**
     * Reinitializes this entity, for reuse
     * 
     * @param x
     *            new x coordinate
     * @param y
     *            new y coordinate
     */
    public void reinitialize(int x, int y) {
        setMinX(x);
        setMinY(y);
        remove = false;
    }
    
    @Override
    public void update(long time) {
        move(time);
    }
    
    /**
     * Request that this shot moved based on time elapsed
     * 
     * @param delta
     *            The time that has elapsed since last move
     */
    public void move(long delta) {
        // proceed with normal move
        super.move(delta);
        
        // if shot off the screen, remove
        if (player.midX+range < midX) {
            remove = true;
        }
        else if(player.midX-range > midX) {
            remove = true;
        }
    }
    
    /**
     * Notification that this shot has collided with another
     * entity
     * 
     * @param other
     *            The other entity with which we've collided
     */
    public void bulletHit(Entity enemy) {
        if (remove) {
            return;
        }
        remove = true;
        enemy.takeDamage(damage);
        //GameEngine.playSound(game.sound_hit);
    }
    
    @Override
    public void enableY() {
        dy = -moveSpeedY;
    }
    
    @Override
    public void disableY() {
        dy = 0;
    }
}
