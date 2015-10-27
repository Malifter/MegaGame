import java.util.Random;


/**
 * ShieldGuyEntity: Shield Guy entity is used for Shield Guy game.
 * Each Shield Guy entity has a direction and direction time for moving the
 * entity.
 */
public class ShieldGuyEntity extends Entity {
    MegaManGame game;
    private float direction = 0;
    private long directionTime = 0;
    private String imageArray[] = {"shieldguy1.gif","shieldguy2.gif","shieldguy3.gif","shieldguy2.gif","shieldguy1.gif"};
    private String imageArrayRight[] = {"shieldguyRight1.gif","shieldguyRight2.gif","shieldguyRight3.gif","shieldguyRight2.gif","shieldguyRight1.gif"};
    private String dieArray[] = {"shieldguyDie1.gif","shieldguyDie2.gif","damage3.gif"};
    private String dieArrayRight[] = {"shieldguyDieRight1.gif","shieldguyDieRight2.gif","damage3.gif"};
    private int imageIndex = 0;
    private int numBullets;
    private boolean isJump = false;
    private boolean spawned = false;
    private boolean moveRight = false;
    private boolean moveLeft = false;
    private boolean isShoot = false;
    private boolean facingRight = true;
    private boolean isFire = false;
    private float moveFactor = 10.0f;
    private float speed = 1.5f;
    private int shotTimer = 0;
    private float lastShotTime;
    private static final int MAX_HEALTH = 5;
    private static final int BLASTER_DAMAGE = 3;
    private static final int COLLISION_DAMAGE = 5;
    private static final int AGGRO_RANGE = 128;
    private Entity player;
    private boolean isBlocking = false;
    private float blockTimer = 0;
    private int isDying = 0;
    private long currentTime = 0;
    
    public ShieldGuyEntity(Game g, String file, int iX, int iY, int x, int y, int w, int h, Entity player) {
        super(g, file, iX, iY);
        game = (MegaManGame) g;
        imageIndex = imageIndex + 10; 
        image = imageArray[0];
        minX = x;
        minY = y;
        width = w;
        height = h;
        offsetX = Math.abs(imageX - minX);
        offsetY = Math.abs(imageY - minY);
        lastShotTime = 0;
        calculateBounds();
        setHealthPoints(MAX_HEALTH);
        isDead = false;
        damage = COLLISION_DAMAGE;
        this.player = player;
        numBullets = 0;
    }
    
    /*public void nextAnimation(String arrayName, int frames) {  
        if(arrayName.equals("spawnArray") && spawned == false){
            imageIndex = imageIndex + 20; 
            if(imageIndex >= frames*100) {
                imageIndex = 0;
            }
            int test = imageIndex/100;
            image = imageArray[test];
            if(image.equals("standingRight.gif")){
                spawned = true;
            }
        }
    }*/
    
    /*public void jump(){
        imageIndex = imageIndex + 10; 
        image = "shieldguyJump.gif";
    }*/

    public void setIsJump(boolean isJump) {
        this.isJump = isJump;
    }

    public boolean getIsJump() {
        return isJump;
    }
    
    /**
     * fire: shoot a bullet
     */
    public void fire(boolean isRight) {
        GameEngine.playSound(game.sound_shot);
        if(isRight) {
            shots.add(new ShotEntity(game, "shieldguyshotRight.gif",(int) maxX,
                    (int) minY+8, isRight, player, BLASTER_DAMAGE, AGGRO_RANGE));
        }
        else if(!isRight) {
            shots.add(new ShotEntity(game, "shieldguyshot.gif",(int) minX,
                    (int) minY+8, isRight, player, BLASTER_DAMAGE, AGGRO_RANGE));
        }
        //GameEngine.playSound(sound_shoot);
        lastShotTime = GameEngine.getTime();
    }
    
    @Override
    public void takeDamage(int d) {
        //meaning not shielded up
        if(!isBlocking) {
            setHealthPoints(getHealthPoints()-d);
            GameEngine.playSound(((MegaManGame)game).sound_hit);
        } else {
            // BLOCKING SOUND
            GameEngine.playSound(((MegaManGame)game).sound_deflect);
        }
        if(getHealthPoints() == 0) {
            isDead = true;
            imageIndex = 0;
            deathAnimate();
        }
    }
    
    @Override
    public void update(long time) {
        movements(time);
        
        int i = 0;
        while(i < shots.size()) {
            if(shots.get(i).needsDelete()) {
                shots.remove(i);
                numBullets--;
            }
            else {
                shots.get(i).update(time);
                i++;
            }
        }
        
        this.reloadSprite();
    }
    
    public void deathAnimate() {
            isDying = 1;
            currentTime = GameEngine.getTime();
    }
    
    public void movements(long time) {
        if(!isDead) {
            if(Math.abs(player.getMidX()-midX) < AGGRO_RANGE && Math.abs(player.getMidY()-midY) < AGGRO_RANGE) {
                if(player.getMidX()-midX > 0) {
                    facingRight = true;
                }
                else {
                    facingRight = false;
                }
                if(!isFalling) {
                    if (player.getIsJumping() && !isJump) {
                        velY = 3;
                        move(0, -(int) (velY * (time / moveFactor)));
                        setIsFalling(true);
                        isJump = true;
                        isBlocking = true;
                    }
                    else if(!player.getIsJumping() && isJump) {
                        isJump = false;
                        isBlocking = true;
                    }
                    
                    Random generator = new Random( GameEngine.getTime() );
                    int rand = generator.nextInt() % 100;
                    if ((rand >= 80 && numBullets <= 1) || GameEngine.getTime() - lastShotTime > 2000) {
                        if(GameEngine.getTime() - lastShotTime > 200){
                            fire(facingRight);
                            numBullets++;
                            blockTimer = GameEngine.getTime();
                            isBlocking = false;
                        }
                        isFire = true;
                        shotTimer = 0;
                    }
                    if(!isBlocking) {
                        if(GameEngine.getTime() - blockTimer > 800) {
                            isBlocking = true;
                            blockTimer = 0;
                        }
                    }
                }
            }
            else {
                if(player.getMidX()-midX > 0) {
                    facingRight = true;
                    image = "shieldguyRight1.gif";
                }
                else {
                    facingRight = false;
                    image = "shieldguy1.gif";
                }
                isBlocking = true;
            }
            if(isFalling) {
                isBlocking = true;
                velY = velY - (gravity*(time/1000.0f));
                move(0, -(int) (velY * (time / moveFactor)));
                //image = "shieldguyJump.gif";
            }
            if(isBlocking){
                if(facingRight) {
                    image = "shieldguyRight1.gif";
                }
                else if(!facingRight) {
                    image = "shieldguy1.gif";
                }
            }
            if(isFalling && isJump) {
                if(facingRight) {
                    image = "shieldguyJumpRight.gif";
                }
                else if(!facingRight) {
                    image = "shieldguyJump.gif";
                }
            }
            else if(isFire && !isFalling && !isJump && !isBlocking){
                isFire = false;
                if(facingRight) {
                    image = "shieldguyRight3.gif";
                }
                else if(!facingRight) {
                    image = "shieldguy3.gif";
                }
            }
            shotTimer++;
        }
        else {
            long seconds = GameEngine.getTime() - currentTime;
            if(seconds > (1000/6)){
                isDying++;
                currentTime = GameEngine.getTime();
            }
            imageIndex = imageIndex + 10; 
            if(imageIndex >= 300) {
                imageIndex = 0;
            }
            if(facingRight) {
                image = dieArrayRight[imageIndex/100];
            }
            else if(!facingRight) {
                image = dieArray[imageIndex/100];
            }
            
            if(isDying >= 3){
                remove = true;
            }
        }
    }
}
