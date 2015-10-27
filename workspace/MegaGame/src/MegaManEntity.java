/**
 * MegaManEntity: Mega Man entity is used for Mega Man game.
 * Each Mega Man entity has a direction and direction time for moving the
 * entity.
 */
public class MegaManEntity extends Entity {
    private MegaManGame game;
    private float direction = 0;
    private long directionTime = 0;
    private String imageArray[] = {"damage1.gif","damage2.gif","damage3.gif","damage4.gif"};
    private String imageArrayRight[] = {"damageRight1.gif","damageRight2.gif","damage3.gif","damage4.gif"};
    private String runningArray[] = {"running1.gif","running2.gif","running3.gif","running4.gif"};
    private String runningRightArray[] = {"runningRight1.gif","runningRight2.gif","runningRight3.gif","runningRight4.gif"};
    private String climbingArray [] = {"climbing1.gif","climbing2.gif"};
    private String spawnArray [] = {"spawn1.gif","spawn2.gif","spawn3.gif","spawn4.gif","spawn5.gif","standingRight.gif"};
    private String runningShootArray [] = {"runshoot1.gif","runshoot2.gif","runshoot3.gif","runshoot4.gif"};
    private String runningRightShootArray [] = {"runshootRight1.gif","runshootRight2.gif","runshootRight3.gif","runshootRight4.gif"};
    private String deathArray [] = {"blackMM.gif","standingRight.gif","spawn3.gif","spawn2.gif","spawn1.gif"};
    private int isDying = 0;
    private String jumpingArray[] = {"jumping1.gif"};
    //private String jumpingRightArray[] = {"jumpingRight1.gif"};
    private int imageIndex = 0;
    private int numBullets = 0;
    private boolean jumpAdjust = false;
    private boolean isClimb = false;
    private boolean spawned = false;
    private boolean moveRight = false;
    private boolean moveLeft = false;
    private boolean isShooting = false;
    private boolean facingRight = true;
    private boolean isFire = false;
    private boolean isVuln = true;
    private int flash = 0;
    private float moveFactor = 10.0f;
    private float speed = 1.5f;
    private int shotTimer = 0;
    private float lastShotTime;
    private static final int MAX_HEALTH = 28;
    private static final int BLASTER_DAMAGE = 1;
    private static final int ATTACK_RANGE = 168;
    private int isDamage = 0;
    private float jumpTimer;
    private long currentTime = 0;
    private float spawnX = 0;
    private float spawnY = 0;
    
    public MegaManEntity(Game g, String file, int iX, int iY, int x, int y, int w, int h, int lives) {
        super(g, file, iX, iY);
        game = (MegaManGame) g;
        imageIndex = imageIndex + 5; 
        image = spawnArray[0];
        minX = x;
        minY = y;
        width = w;
        height = h;
        spawnX = iX;
        spawnY = iY;
        offsetX = Math.abs(imageX - minX);
        offsetY = Math.abs(imageY - minY);
        lastShotTime = 0;
        calculateBounds();
        setHealthPoints(MAX_HEALTH);
        this.lives = lives;
        jumpTimer = 0;
    }
    
    public float getSpawnX() {
        return spawnX;
    }
    
    public float getSpawnY() {
        return spawnY;
    }
    
    /*public float getDirection() {
        return direction;
    }
    
    public void setDirection(float direction) {
        this.direction = direction;
    }
    
    public void setDirectionTime(long dt) {
        directionTime = dt;
    }
    
    public long getDirectionTime() {
        return directionTime;
    }
    */
    
    public void reset() {
        setMinX(spawnX);
        setMinY(spawnY);
        imageIndex = imageIndex + 5; 
        image = spawnArray[0];
        lastShotTime = 0;
        calculateBounds();
        setHealthPoints(MAX_HEALTH);
        jumpAdjust = false;
        isClimb = false;
        spawned = false;
        moveRight = false;
        moveLeft = false;
        isShooting = false;
        facingRight = true;
        isFire = false;
        isVuln = true;
        flash = 0;
        shotTimer = 0;
        currentTime = 0;
        jumpTimer = 0;
        remove = false;
        isDying = 0;
        isDead = false;
    }
    
    public void nextAnimation(String arrayName, int frames) {  
        if(arrayName.equals("spawnArray") && spawned == false){
            imageIndex = imageIndex + 20; 
            if(imageIndex >= frames*100) {
                imageIndex = 0;
            }
            int test = imageIndex/100;
            image = spawnArray[test];
            if(image.equals("standingRight.gif")){
                spawned = true;
            }
        }
    }
    
    public void moveLeft(){
        imageIndex = imageIndex + 10; 
        if(imageIndex >= 400 && shotTimer >= 10) {
            imageIndex = 0;
        }
        if(shotTimer >= 10){
            image = runningArray[imageIndex/100];
        }
    }
    
    public void moveRight(){
        imageIndex = imageIndex + 10; 
        if(imageIndex >= 400) {
            imageIndex = 0;
        }
        if(shotTimer >= 10){
            image = runningRightArray[imageIndex/100];
        }
    }
    
    public void jumpLeft() {
        imageIndex = imageIndex + 10; 
        if(imageIndex >=100) {
            imageIndex = 0;
        }
        image = jumpingArray[imageIndex/100]; 
    }
    
    public void jumpRight() {
        imageIndex = imageIndex + 10; 
        if(imageIndex >= 100) {
            imageIndex = 0;
        }
        image = jumpingArray[imageIndex/100]; 
    }
    
    public void moveLeftShoot(){
        imageIndex = imageIndex + 10; 
        if(imageIndex >= 400) {
            imageIndex = 0;
        }
        image = runningShootArray[imageIndex/100];
    }
    
    public void moveRightShoot(){
        imageIndex = imageIndex + 10; 
        if(imageIndex >= 400) {
            imageIndex = 0;
        }
        image = runningRightShootArray[imageIndex/100];
    }
    public void climb(){
        imageIndex = imageIndex + 10; 
        if(imageIndex >= 200) {
            imageIndex = 0;
        }
        image = climbingArray[imageIndex/100];
    }
    
    public void jump(){
        imageIndex = imageIndex + 10; 
        image = "jumping1.gif";
    }

    public void setIsJump(boolean isJump) {
        this.isJump = isJump;
    }

    public boolean getIsJump() {
        return isJump;
    }

    public void setIsClimb(boolean isClimb) {
        this.isClimb = isClimb;
    }

    public boolean getIsClimb() {
        return isClimb;
    }
    /*
    public void move(float x, float y) {
        boolean collision = false;
        MegaManGame g = (MegaManGame) game;
        ArrayList<Entity> solids = g.getSolids();
        int count = 0;
        float iniX = minX;
        float iniY = minY;

        minX += x;
        minY += y;
        calculateBounds();
        
        for(Entity e : solids) {
            if(g.detectCollision(this, e)) {
                collision = true;
            }
        }
        
        while(collision && count < 10) {
            collision = false;
            minX -= (x*.1f);
            minY -= (y*.1f);
            calculateBounds();
            for(Entity e : solids) {
                if(g.detectCollision(this, e)) {
                    collision = true;
                }
            }
            count++;
        }
        
        if(count >= 10) {
            minX = iniX;
            minY = iniY;
            calculateBounds();
        }
    }
     */
    
    /**
     * fire: shoot a bullet
     */
    public void fire(boolean isRight) {
        GameEngine.playSound(game.sound_shot);
        if(isRight && !isFalling) {
            shots.add(new ShotEntity(game, "shot.gif",(int) maxX,
                    (int) minY+8, isRight, this, BLASTER_DAMAGE, ATTACK_RANGE));
        }
        else if(!isRight && !isFalling) {
            shots.add(new ShotEntity(game, "shot.gif",(int) minX,
                    (int) minY+8, isRight, this, BLASTER_DAMAGE, ATTACK_RANGE));
        }
        else if(isRight && isFalling) {
            shots.add(new ShotEntity(game, "shot.gif",(int) maxX,
                    (int) minY+2, isRight, this, BLASTER_DAMAGE, ATTACK_RANGE));
        }
        else if(!isRight && isFalling) {
            shots.add(new ShotEntity(game, "shot.gif",(int) minX,
                    (int) minY+2, isRight, this, BLASTER_DAMAGE, ATTACK_RANGE));
        }
        lastShotTime = GameEngine.getTime();
    }
    
    @Override
    public void update(long time) {
        movements(time);
        if(!isVuln){
            if(GameEngine.getTime() - currentTime > 750)
                isVuln = true;
        }
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
       
        reloadSprite();
        
    }
    @Override
    public void takeDamage(int d) {
        if(isDamage == 0 && isVuln){
            GameEngine.playSound(game.sound_hit);
            setHealthPoints(getHealthPoints()-d);
            isDamage = 1;
            imageIndex = 0;
            currentTime = GameEngine.getTime();
        }
        if(getHealthPoints() == 0 && !isDead) {
            GameEngine.playSound(game.sound_dead);
            isDead = true;
            isDying = 1;
            imageIndex = 0;
            currentTime = GameEngine.getTime();
        }
    }
    public void movements(long time) {
        if(spawned) {
            if(!isDead){
                if(isVuln || flash <= 4){
                    if(isDamage == 0){
                        if(!isClimb) {
                            if (((MegaManGame) game).right.isDown()) {
                                moveRight = true;
                                move((int) (speed * (time / moveFactor)), 0);
                            }
                            if (((MegaManGame) game).left.isDown()) {
                                moveLeft = true;
                                move(-(int) (speed * (time / moveFactor)), 0);
                            }
                        }
                        else if(isClimb) {
                            setIsFalling(false);
                            if (((MegaManGame) game).up.isDown()) {
                                move(0, -(int) (speed * (time / moveFactor)));
                            }
                            if (((MegaManGame) game).down.isDown()) {
                                move(0, (int) (speed * (time / moveFactor)));
                            }  
                        }
                        if(isFalling) {
                            float timeDif = 0;
                            if(((MegaManGame) game).jump.isDown() && isJump && !jumpAdjust) {
                                timeDif = GameEngine.getTime() - jumpTimer;
                            }
                            else if(!((MegaManGame) game).jump.isDown() && isJump && !jumpAdjust){
                                jumpAdjust = true;
                                if(timeDif < 1 && velY > 0) {
                                    velY = velY*timeDif;
                                }
                            }
                            velY = velY - (gravity*(time/1000.0f));
                            move(0, -(int) (velY * (time / moveFactor)));
                        }
                        else if(!isFalling) {
                            if (((MegaManGame) game).jump.isDown() && !isJump) {
                                velY = 3;
                                move(0, -(int) (velY * (time / moveFactor)));
                                setIsFalling(true);
                                isJump = true;
                                jumpTimer = GameEngine.getTime();
                            }
                            else if(!((MegaManGame) game).jump.isDown() && isJump) {
                                isJump = false;
                                jumpAdjust = false;
                            }
                        }
                        if (((MegaManGame) game).fire.isDown() && !isShooting) {
                            if(numBullets <= 2 && GameEngine.getTime() - lastShotTime > 100){
                                fire(facingRight);
                                numBullets++;
                            }
                            isShooting = true;
                            isFire = true;
                            shotTimer = 0;
                        }
                        else if(!((MegaManGame) game).fire.isDown() && isShooting) {
                            isShooting = false;
                        }
                        
                        if(isFire && isFalling) {
                            if(!moveRight && moveLeft) {
                                //moveLeftShoot();
                                moveLeft = false;
                                facingRight = false;
                                isFire = false;
                                image = "jumpshoot1.gif";
                            }
                            else if(moveRight && !moveLeft) {
                                //moveRightShoot();
                                moveRight = false;
                                facingRight = true;
                                isFire = false;
                                image = "jumpshootRight1.gif";
                            }
                            else if(moveRight && moveLeft) {
                                moveRight = false;
                                moveLeft = false;
                                isFire = false;
                                if(facingRight) {
                                    image = "jumpshootRight1.gif";
                                }
                                else if(!facingRight) {
                                    image = "jumpshoot1.gif";
                                }
                            }
                            else {
                                isFire = false;
                                if(facingRight) {
                                    image = "jumpshootRight1.gif";
                                }
                                else if(!facingRight) {
                                    image = "jumpshoot1.gif";
                                }
                            }
                        }
                        else if(isFire){
                            if(!moveRight && moveLeft) {
                                moveLeftShoot();
                                moveLeft = false;
                                facingRight = false;
                                isFire = false;
                            }
                            else if(moveRight && !moveLeft) {
                                moveRightShoot();
                                moveRight = false;
                                facingRight = true;
                                isFire = false;
                            }
                            else if(moveRight && moveLeft) {
                                moveRight = false;
                                moveLeft = false;
                                isFire = false;
                                if(facingRight) {
                                    image = "shootingRight1.gif";
                                }
                                else if(!facingRight) {
                                    image = "shooting1.gif";
                                }
                            }
                            else {
                                isFire = false;
                                if(facingRight) {
                                    image = "shootingRight1.gif";
                                }
                                else if(!facingRight) {
                                    image = "shooting1.gif";
                                }
                            }
                        }
                        else if(isFalling) {
                            if(!moveRight && moveLeft) {
                                jumpLeft();
                                moveLeft = false;
                                facingRight = false;
                            }
                            else if(moveRight && !moveLeft && shotTimer >= 15) {
                                jumpRight();
                                moveRight = false;
                                facingRight = true;
                                image = "jumpingRight1.gif";
                            }
                            else if(moveRight && moveLeft) {
                                moveRight = false;
                                moveLeft = false;
                                if(facingRight && shotTimer >= 15) {
                                    image = "jumpingRight1.gif";
                                }
                                else if(!facingRight && shotTimer >= 15) {
                                    image = "jumping1.gif";
                                }
                            }
                            else {
                                if(facingRight && shotTimer >= 15) {
                                    image = "jumpingRight1.gif";
                                }
                                else if(!facingRight && shotTimer >= 15) {
                                    image = "jumping1.gif";
                                }
                            }
                        }
                        else {
                            if(!moveRight && moveLeft) {
                                moveLeft();
                                moveLeft = false;
                                facingRight = false;
                            }
                            else if(moveRight && !moveLeft) {
                                moveRight();
                                moveRight = false;
                                facingRight = true;
                            }
                            else if(moveRight && moveLeft) {
                                moveRight = false;
                                moveLeft = false;
                                if(facingRight && shotTimer >= 15) {
                                    image = "standingRight.gif";
                                }
                                else if(!facingRight && shotTimer >= 15) {
                                    image = "standing.gif";
                                }
                            }
                            else {
                                if(facingRight && shotTimer >= 15) {
                                    image = "standingRight.gif";
                                }
                                else if(!facingRight && shotTimer >= 15) {
                                    image = "standing.gif";
                                }
                            }
                        }
                        shotTimer++;
                    }
                    else{
                        long seconds = GameEngine.getTime() - currentTime;
                        if(seconds > (1333/6)){
                            isDamage++;
                            currentTime = GameEngine.getTime();
                        }
                        imageIndex = imageIndex + 10; 
                        if(imageIndex >= 400) {
                            imageIndex = 0;
                        }
                        if(facingRight) {
                            image = imageArrayRight[imageIndex/100];
                        }
                        else if(!facingRight) {
                            image = imageArray[imageIndex/100];
                        }
                        
                        if(isDamage >= 4){
                            isDamage = 0;
                            isVuln = false;
                        }
                    }
                    flash++;
                }
                else{
                    image = "damage4.gif";
                    flash = 0;
                }
            }
            else{
                long seconds = GameEngine.getTime() - currentTime;
                if(seconds > (1666/6)){
                    isDying++;
                    currentTime = GameEngine.getTime();
                }
                imageIndex = imageIndex + 10; 
                if(imageIndex >= 500) {
                    imageIndex = 0;
                }
                image = deathArray[imageIndex/100];         
                if(isDying >= 5){
                    isDying = 0;
                    remove = true;
                }
            }
        }
    }
}
