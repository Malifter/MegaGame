import java.util.ArrayList;

/**
 * SpaceInvaderGame: Space invader game implementing the IGame interface
 */
public class SpaceInvaderGame extends Game {
    
    ArrayList<Input> inputs = null;
    private int numAliens = 20;
    private int shipSpeed = 2;
    private int alienSpeed = 1;
    private int theWidth = 800;
    private int theHeight = 600;
    private long lastShotTime = 0;
    ShipEntity playerShip;
    private float moveFactor = 10.0f;
    private float mouseMoveX;
    private float mouseMoveY;
    
    public int sound_hit;
    public int sound_shoot;
    
    Button right = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_RIGHT, PhysicalInput.KEYBOARD_D });
    Axis xAxis = new Axis(new PhysicalInput[] {PhysicalInput.MOUSE_X});
    Axis yAxis = new Axis(new PhysicalInput[] {PhysicalInput.MOUSE_Y});
    Button left = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_LEFT, PhysicalInput.KEYBOARD_A });
    Button up = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_UP, PhysicalInput.KEYBOARD_W });
    Button down = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_DOWN, PhysicalInput.KEYBOARD_S });
    Button fire = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_RETURN, PhysicalInput.KEYBOARD_F, PhysicalInput.MOUSE_LEFT });
    Button escape = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_ESCAPE });
    Button quit = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_Q });
    
    public SpaceInvaderGame(GameEngine e) throws Exception {
        super(e);
        setDisplay(new JLWGLDisplay("Space Invaders", theWidth, theHeight));
    }
    
    public ArrayList<Input> init() {
        super.init();
        doRestore = false;
        if (doRestore) {
           reInitFromSave();
        } else {
            entities.clear();
            playerShip = new ShipEntity(this, "spawn4.gif", 370, 550);
            entities.add(playerShip);
            
            int alienCount = 0;
            for (int x = 0; x < numAliens; x++) {
                Entity alien = new AlienEntity(this, "damage1.gif",
                        (int) (50 + (Math.random() * 700)),
                        (int) (Math.random() * 300));
                entities.add(alien);
                alienCount++;
            }
        }
        inputs = new ArrayList<Input>();
        inputs.add(left);
        inputs.add(right);
        inputs.add(up);
        inputs.add(down);
        inputs.add(fire);
        inputs.add(escape);
        inputs.add(quit);
        inputs.add(xAxis);
        inputs.add(yAxis);
        
        initSounds();
        
        GameEngine.setMouseHidden(true);
        
        return inputs;
    }
    
    private void initSounds() {
        sound_hit = GameEngine.addSound("hit.wav");
        sound_shoot = GameEngine.addSound("shot.wav");
    }
    
    @Override
    protected void reInitFromSave() {
        for (Entity e : entities) {
            if (e instanceof ShipEntity) {
                playerShip = (ShipEntity) e;
            }
            e.setGame(this);
            e.reloadSprite();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see IGame#update(long)
     */
    @Override
    public void update(long time) {
        
        controlAliens(time);
        controlShip(time);
        checkCollision();
        entities.removeAll(removeList);
        removeList.clear();
    }
    
    private void controlAliens(long time) {
        for (int x = 0; x < entities.size(); x++) {
            Entity current = entities.get(x);
            long curTime = GameEngine.getTime();
            if (current instanceof AlienEntity) {
                AlienEntity currentAlien = (AlienEntity) current;
                currentAlien.nextAnimation();
                currentAlien.reloadSprite();
                if (currentAlien.getMinY() > 600) {
                    currentAlien.setMinY(0);
                }
                if (currentAlien.getMinX() > 800) {
                    currentAlien.setMinX(0);
                }
                if (currentAlien.getMinX() < 0) {
                    currentAlien.setMinX(800);
                }
                if (curTime - currentAlien.getDirectionTime() > (500 + Math
                        .random() * 5000)) {
                    if (currentAlien.getDirection() > 0) {
                        currentAlien
                                .setDirection((-2 * (time / moveFactor)));
                    } else {
                        currentAlien
                                .setDirection((2 * (time / moveFactor)));
                    }
                    currentAlien.setDirectionTime(curTime);
                }
                currentAlien.move(currentAlien.getDirection(),
                        (alienSpeed * (time / moveFactor)));
            } else if (current instanceof ShotEntity) {
                current.move(time);
            }
        }
    }
    private void controlShip(long time) {
        mouseMoveX = xAxis.getValue();
        playerShip.move((int) mouseMoveX, 0);
        mouseMoveY = yAxis.getValue();
        playerShip.move(0, (int) mouseMoveY);
        if (right.isDown()) {
            playerShip.move((int) (shipSpeed * (time / moveFactor)), 0);
        }
        if (left.isDown()) {
            playerShip.move(-(int) (shipSpeed * (time / moveFactor)), 0);
        }
        if (up.isDown()) {
            playerShip.move(0, -(int) (shipSpeed * (time / moveFactor)));
        }
        if (down.isDown()) {
            playerShip.move(0, (int) (shipSpeed * (time / moveFactor)));
        }
        if (playerShip.getMinY() > 580) {
            playerShip.setMinY(580);
        }
        if (playerShip.getMinY() < 0) {
            playerShip.setMinY(0);
        }
        if (playerShip.getMinX() > 800) {
            playerShip.setMinX(0);
        }
        if (playerShip.getMinX() < 0) {
            playerShip.setMinX(800);
        }
        
        if (fire.isDown()) {
            fire();
        }
        if (escape.isDown()) {
            isDone = true;
            doRestore = true;
        }
        if (quit.isDown()) {
            isDone = true;
            doRestore = false;
        }
        
    }
    
    private void checkCollision() {
        for (int checkCollision = 0; checkCollision < entities.size(); checkCollision++) {
            // check each entity for collisions
            Entity checkEntity = entities.get(checkCollision);
            
            // if entity is an alien, check it for collision with the
            // player ship
            if (checkEntity instanceof AlienEntity) {
                boolean shipCollided = GameEngine.collisionBetween(
                        playerShip, checkEntity);
                if (shipCollided) {
                    playerShip.destroy();
                    isDone = true;
                    doRestore = false;
                }
            }
            
            // if the entity is a shot, check for collision with an alien
            if (checkEntity instanceof ShotEntity) {
                for (int checkBullet = 0; checkBullet < entities.size(); checkBullet++) {
                    Entity bulletHit = entities.get(checkBullet);
                    if (bulletHit instanceof AlienEntity) {
                        boolean shotAlien = GameEngine.collisionBetween(
                                checkEntity, bulletHit);
                        if (shotAlien) {
                            ((ShotEntity) checkEntity)
                                    .bulletHit(bulletHit);
                            GameEngine.playSound(sound_hit);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * fire: shoot a bullet
     */
    private void fire() {
        if (GameEngine.getTime() - lastShotTime > 250) {
            int playerX = (int) entities.get(0).getMinX();
            int playerY = (int) entities.get(0).getMaxY();
            entities.add(new ShotEntity(this, "shot.gif", playerX + 10, playerY - 20,true));
            GameEngine.playSound(sound_shoot);
            lastShotTime = GameEngine.getTime();
        }
    }
    
    /*
     * (non-Javadoc)
     * @see IGame#getDrawables()
     */
    @Override
    public ArrayList<Entity> getEntities() {
        return entities;
    }
    
}
