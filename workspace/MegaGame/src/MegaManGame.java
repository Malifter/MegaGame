import java.util.ArrayList;
import java.io.*;

/**
 * File: MegaManGame.java
 * Authors: M. Swindoll, D. Spitler, J. Medlock, G. Benoit
 * Last Modified By:
 */

/**
 * MegaManGame: This is our Mega Man game
 */
public class MegaManGame extends Game {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3118971393018891785L;
    ArrayList<Input> inputs = null;
    private int theWidth = 1024;
    private int theHeight = 768;
    private Entity player = null;
    private Entity winScreen = null;
    private Entity loseScreen = null;
    private Entity startScreen = null;
    private ArrayList<Entity> background = null;
    private ArrayList<Entity> entities = null;
    private ArrayList<Entity> traps = null;
    private ArrayList<Entity> foreground = null;
    private ArrayList<Entity> healthbar = null;
    private Camera cam = null;
    private String theAssetsPath = "assets/";
    private String theLevel[] = { theAssetsPath + "level1.oel", theAssetsPath + "TestLevel2.oel"};
    private boolean win = false;
    private boolean isStart = false;
    private boolean lose = false;
    private String theSaveFilename = "cpt.ser";
    
    public Button right = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_RIGHT, PhysicalInput.KEYBOARD_D });
    public Button left = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_LEFT, PhysicalInput.KEYBOARD_A });
    public Button up = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_UP, PhysicalInput.KEYBOARD_W });
    public Button down = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_DOWN, PhysicalInput.KEYBOARD_S });
    public Button fire = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_X, PhysicalInput.KEYBOARD_K });
    public Button jump = new Button(new PhysicalInput[] {
            PhysicalInput.KEYBOARD_Z, PhysicalInput.KEYBOARD_L });
    public Button escape = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_ESCAPE });
    public Button pause = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_P });
    public Button cameraMode = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_F2 });
    public Button startGame = new Button(
            new PhysicalInput[] { PhysicalInput.KEYBOARD_RETURN });
    
    
    public int sound_hit;
    public int sound_shot;
    public int sound_deflect;
    public int sound_spawn;
    public int sound_dead;
    public int BGM_quickman;
    private long timeBGM = 0;
    
    /**
     * Constructor
     * 
     * @param e
     */
    public MegaManGame(GameEngine e) throws Exception {
        super(e);
        setDisplay(new JLWGLDisplay("Mega Man", theWidth, theHeight));
        background = new ArrayList<Entity>();
        foreground = new ArrayList<Entity>();
        entities = new ArrayList<Entity>();
        healthbar = new ArrayList<Entity>();
        traps = new ArrayList<Entity>();
        cam = new Camera(this);
    }
    
    @Override
    public ArrayList<Entity> getEntities() {
        ArrayList<Entity> tmp = new ArrayList<Entity>();
        if(isStart){
            if(!win && !lose){
                tmp.addAll(background);        
                tmp.addAll(foreground);
                tmp.addAll(traps);
                for (Entity e : entities) {
                    tmp.addAll(e.getShots());
                }
                tmp.addAll(player.getShots());
                tmp.addAll(entities);
                tmp.add(player);
                tmp.add(healthbar.get(player.getHealthPoints()));
            }
            else if(win){
                tmp.add(winScreen);
            }
            else {
                tmp.add(loseScreen);
            }
        }
        else{
            tmp.add(startScreen);
        }
        return tmp;
    }
    
    public ArrayList<Entity> getSolids() {
        return foreground;
    }
    
    /**
     * loadLevel: Loads a level from a .oel (XML file)
     * 
     * @param filename
     */
    public void loadLevel(String filename) {
        String s = null; // This will store the current character
        String tmpS = null;
        int tmpI = 0;
        boolean done = false;
        boolean read = false;
        int yIndex = 0, xIndex = 0;
        
        // Create file reader
        FileReader inputStream = null;
        BufferedReader in = null;
        try {
            inputStream = new FileReader(filename);
            in = new BufferedReader(inputStream);
            
            // check level
            s = in.readLine();
//            if(!"<level>".equals(s)) {
//                throw new Exception("Invalid level file.");
//            }
            
            //check background and tileset
            s = in.readLine();
            tmpS = s.substring(3);
            tmpI = tmpS.indexOf(' ');
            if("Background".equals(tmpS.substring(0, tmpI))) {
                tmpS = tmpS.substring(tmpI+1);
                if(0 != tmpS.indexOf("tileset")) {
                    throw new Exception("Invalid level file.");
                }
                tmpI = tmpS.indexOf('\"');
                tmpS = tmpS.substring(tmpI+1);
                tmpI = tmpS.indexOf('\"');
                tmpS = tmpS.substring(0,tmpI);
                
                String bgTileSet = tmpS;
                
                tmpI = s.lastIndexOf('>');
                tmpS = s.substring(tmpI+1);
                while(!done) {
                    if(read == true) {
                        tmpS = in.readLine();
                        xIndex = 0;
                        yIndex++;
                        read = false;
                    }
                    tmpI = tmpS.indexOf(',');
                    if(tmpI == -1) {
                        if(-1 != (tmpI = tmpS.indexOf('<'))) {
                            done = true;
                        }
                        else {
                            //tmpI = tmpS.indexOf('\n');
                            tmpI = tmpS.length();
                            read = true;
                        }
                    }
                    
                    if(!"-1".equals(tmpS.substring(0,tmpI))) {
                        background.add(new BGTile(this, bgTileSet+tmpS.substring(0,tmpI)+".gif", xIndex++, yIndex));
                    }
                    else {
                        xIndex++;
                    }
                    if(!read) {
                        tmpS = tmpS.substring(tmpI+1);
                    }
                }
            }
            
            //check and create entities
            s = in.readLine();
            if("  <Entities>".equals(s)) {
                s = in.readLine();
                while(!"  </Entities>".equals(s)) {
                    tmpS = s;
                    tmpI = tmpS.indexOf('<');
                    tmpS = tmpS.substring(tmpI+1);
                    tmpI = tmpS.indexOf(' ');
                    String tmpE = tmpS.substring(0,tmpI);
                    tmpS = tmpS.substring(tmpI+1);
                    tmpI = tmpS.indexOf('x');
                    tmpS = tmpS.substring(tmpI);
                    tmpI = tmpS.indexOf('\"');
                    tmpS = tmpS.substring(tmpI+1);
                    tmpI = tmpS.indexOf('\"');
                    xIndex = Integer.parseInt(tmpS.substring(0,tmpI));
                    tmpI = tmpS.indexOf('y');
                    tmpS = tmpS.substring(tmpI);
                    tmpI = tmpS.indexOf('\"');
                    tmpS = tmpS.substring(tmpI+1);
                    tmpI = tmpS.indexOf('\"');
                    yIndex = Integer.parseInt(tmpS.substring(0,tmpI));
                    if("MegaMan".equals(tmpE)) {
                        player = new MegaManEntity(this, "spawn1.gif", xIndex, yIndex, xIndex+9, yIndex+6, 15, 24, 3);
                    }
                    else if ("ShieldEnemy".equals(tmpE)) {
                        entities.add(new ShieldGuyEntity(this, "ShieldGuy1.gif", xIndex, yIndex, xIndex+1, yIndex+2, 24, 22, player));
                        //entities.add(new SomeEnemyEntity(this, "enemy1.gif", xIndex, yIndex, xIndex+9, yIndex+6, 15, 24))
                    }
                    else if("Spike".equals(tmpE)) {
                        traps.add(new SpikeEntity(this, "spikeFloor.gif", xIndex, yIndex, xIndex+7, yIndex+2, 10, 15, player));
                    }
                    else if("cSpike".equals(tmpE)) {
                        traps.add(new SpikeEntity(this, "spikeCeiling.gif", xIndex, yIndex, xIndex+7, yIndex+2, 10, 15, player));
                    }
                    else if("WoodMan".equals(tmpE)) {
                        entities.add(new WoodManEntity(this, "\\enemies\\woodman1.gif", xIndex, yIndex, xIndex+6, yIndex+6, 30, 26, player));
                    }
                    else if("Cannon".equals(tmpE)) {
                        entities.add(new CannonEntity(this, "\\enemies\\cannon1floor.gif", xIndex, yIndex, xIndex, yIndex, 35, 25, player));
                    }
                    // ADD OTHER ENTITIES HERE
                    s = in.readLine();
                }
            }
            
            //check foreground and tileset
            s = in.readLine();
            tmpS = s.substring(3);
            tmpI = tmpS.indexOf(' ');
            xIndex = 0;
            yIndex = 0;
            if("Foreground".equals(tmpS.substring(0, tmpI))) {
                tmpS = tmpS.substring(tmpI+1);
                if(0 != tmpS.indexOf("tileset")) {
                    throw new Exception("Invalid level file.");
                }
                tmpI = tmpS.indexOf('\"');
                tmpS = tmpS.substring(tmpI+1);
                tmpI = tmpS.indexOf('\"');
                tmpS = tmpS.substring(0,tmpI);
                
                String fgTileSet = tmpS;
                
                tmpI = s.lastIndexOf('>');
                tmpS = s.substring(tmpI+1);
                done = false;
                read = false;
                while(!done) {
                    if(read == true) {
                        tmpS = in.readLine();
                        xIndex = 0;
                        yIndex++;
                        read = false;
                    }
                    tmpI = tmpS.indexOf(',');
                    if(tmpI == -1) {
                        if(-1 != (tmpI = tmpS.indexOf('<'))) {
                            done = true;
                        }
                        else {
                            //tmpI = tmpS.indexOf('\n');
                            tmpI = tmpS.length();
                            read = true;
                        }
                    }
                    if(!"-1".equals(tmpS.substring(0,tmpI))) {
                        foreground.add(new FGTile(this, fgTileSet+tmpS.substring(0,tmpI)+".gif", xIndex++, yIndex));
                    }
                    else {
                        xIndex++;
                    }
                    if(!read) {
                        tmpS = tmpS.substring(tmpI+1);
                    }

                }
            }
            
            for(int i = 0; i < 29; i++) {
                healthbar.add(new HealthBar(this, "\\healthbar\\health"+i+".gif", cam, player));
            }
            startScreen = new FGTile(this, "startScreen.gif", 0, 0);
            winScreen = new FGTile(this, "winScreen.gif", 0, 0);
            loseScreen = new FGTile(this, "game_over.gif", 0, 0);
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
    }

    public ArrayList<Input> init() {
        super.init();
        loadLevel(theLevel[0]);
        cam.setOrientation(512,0,0,0,1);
        cam.setFocusEntity(player);
        inputs = new ArrayList<Input>();
        inputs.add(left);
        inputs.add(right);
        inputs.add(up);
        inputs.add(down);
        inputs.add(fire);
        inputs.add(jump);
        inputs.add(escape);
        inputs.add(pause);
        inputs.add(cameraMode);
        inputs.add(startGame);
        
        initSounds();
        
        timeBGM = GameEngine.getTime();
        GameEngine.playMusic(BGM_quickman);
        GameEngine.playSound(sound_spawn);
        
        //GameEngine.setMouseHidden(true);
        
        return inputs;
    }
    
    private void initSounds() {
        sound_hit = GameEngine.addSound("hit.wav");
        sound_shot = GameEngine.addSound("shot.wav");
        sound_spawn = GameEngine.addSound("spawn.wav");
        sound_deflect = GameEngine.addSound("deflect.wav");
        sound_dead = GameEngine.addSound("dead.wav");
        BGM_quickman = GameEngine.addSound("music/quickmanBGM.wav");
    }
    
    public ArrayList<Input> getInputs() {
        return inputs;
    }

    /*
     * (non-Javadoc)
     * @see IGame#update(long)
     */
    @Override
    public void update(long time) {
        
        spawnPlayer(time);
        entities.removeAll(removeList);
        removeList.clear();
        
        if((GameEngine.getTime()-timeBGM) > 38000) {
            timeBGM = GameEngine.getTime();
            GameEngine.playMusic(BGM_quickman);
        }
        
        if(player.needsDelete()) {
            if(player.getLives() > 0) {
                ((MegaManEntity) player).reset();
                player.calculateBounds();
                player.setLives(player.getLives()-1);
            }
            else {
                lose = true;
                cameraMode.setDown(true);
                cam.mode = false;
            }
        }
        player.update(time);
        
        int i = 0;
        while(i < entities.size()) {
            if(entities.get(i).needsDelete()) {
                entities.remove(i);
            }
            else {
                entities.get(i).update(time);
                i++;
            }
        }
        
        for(Entity h: healthbar) {
            h.update(time);
        }
        
        camera();
        checkCollisions();
        checkTileCollision();
        
        if(escape.isDown()) {
            isDone = true;
            doRestore = false;
        }     
        if(!isStart){
            cameraMode.setDown(true);
            cam.mode = false;
            
            if(this.startGame.isDown()){
                isStart = true;
                cameraMode.setDown(false);
                cam.mode = true;
            }
       }
        if(win){
            cameraMode.setDown(true);
            cam.mode = false;
        }
    }
    
    private void camera() {
        cam.update();
    }

    private void spawnPlayer(long time) {
        ((MegaManEntity) player).nextAnimation("spawnArray",6);
    }
  

    private void checkCollisions() {
        for (int ent = 0; ent < entities.size(); ent++) {
            Entity enemy = entities.get(ent);
            if(detectCollision(enemy, player)) {
                player.takeDamage(enemy.getDamage());
            }
            for(int shot = 0; shot < player.getShots().size(); shot++) {
                // check each projectile to see if it hits an enemy
                Entity checkShot = player.getShots().get(shot);
                if(detectCollision(enemy, checkShot)) {
                    ((ShotEntity) checkShot).bulletHit(enemy);
                }
            }
            for(int shot = 0; shot < enemy.getShots().size(); shot++) {
                Entity checkShot = enemy.getShots().get(shot);
                if(detectCollision(checkShot, player)) {
                    ((ShotEntity) checkShot).bulletHit(player);
                }
            }
        }
        for(int t = 0; t < traps.size(); t++) {
            Entity trap = traps.get(t);
            if(detectCollision(trap, player)) {
                player.takeDamage(trap.getDamage());
            }
        }
    }

    // checks gravity and collision of all entities vs the world environment
    private void checkTileCollision() {

        player.setIsFalling(true);
        for(int tile = 0; tile < foreground.size(); tile++) {
            // check player for collisions
            Entity checkTile = foreground.get(tile);
            detectEnvironmentCollision(player, checkTile);
        }
        
        for (int ent = 0; ent < entities.size(); ent++) {
            Entity checkEntity = entities.get(ent);
            checkEntity.setIsFalling(true);
            for(int tile = 0; tile < foreground.size(); tile++) {
                // check each entity for collisions
                Entity checkTile = foreground.get(tile);
                detectEnvironmentCollision(checkEntity, checkTile);
            }
        }
    }
    
    // for collision correction with environment
    public boolean detectEnvironmentCollision(Entity ent, Entity tile) {
        return detectCollisionSphere(ent, tile) ? detectEnvironmentCollsionBox(ent, tile) : false;
    }
    
    // for collision detection with enemies/projectiles
    public boolean detectCollision(Entity ent1, Entity ent2) {
        return detectCollisionSphere(ent1, ent2) ? detectCollisionBox(ent1, ent2) : false;
    }
    
    private boolean detectCollisionSphere(Entity ent1, Entity ent2) {
        float relX = ent1.getMidX() - ent2.getMidX();
        float relY = ent1.getMidY() - ent2.getMidY();
        float dist = relX*relX + relY*relY;
        float minDist = ent1.getRadius() + ent2.getRadius();
        return dist <= minDist*minDist;
    }
    
    private boolean detectCollisionBox(Entity ent1, Entity ent2) {  
        if(ent1.getMaxX() < ent2.getMinX())
            return false;
        if(ent1.getMinX() >  ent2.getMaxX())
            return false;
        if(ent1.getMaxY() <  ent2.getMinY())
            return false;
        if(ent1.getMinY() >  ent2.getMaxY())
            return false;
        
        return true;
    }
    
    private boolean detectEnvironmentCollsionBox(Entity ent, Entity tile) {
        if(ent.getMaxX() < tile.getMinX())
            return false;
        if(ent.getMinX() > tile.getMaxX())
            return false;
        if(ent.getMaxY() < tile.getMinY())
            return false;
        if(ent.getMinY() > tile.getMaxY())
            return false;
        
        //calculate x and y penetration levels
        float penx = 0;
        float peny = 0;
        
        // tile below - neg
        if(ent.getMaxY() < tile.getMaxY() && ent.getMinY() < tile.getMinY()) {
            peny = -(ent.getMaxY() - tile.getMinY());
            if(peny == 0) {
                peny = -0.0000001f;
            }
            //System.out.printf("tile should be below me");
        }
        // tile above - pos
        else if(tile.getMaxY() < ent.getMaxY() && tile.getMinY() < ent.getMinY()) {
            peny = (tile.getMaxY() - ent.getMinY());
            if(peny == 0) {
                peny = 0.0000001f;
            }
            //System.out.printf("tile should be above me");
        }
        // tile right - neg
        if(ent.getMaxX() < tile.getMaxX() && ent.getMinX() < tile.getMinX()) {
            penx = -(ent.getMaxX() - tile.getMinX());
            if(penx == 0) {
                penx = -0.0000001f;
            }
            //System.out.printf("tile should be right of me");
        }
        // tile left - pos
        else if(tile.getMaxX() < ent.getMaxX() && tile.getMinX() < ent.getMinX()) {
            penx = (tile.getMaxX() - ent.getMinX());
            if(penx == 0) {
                penx = 0.0000001f;
            }
            //System.out.printf("tile should be left of me");
        }
        //System.out.printf("\n");
        if(penx == 0 && peny != 0) {
            if(ent.getMaxX() > tile.getMaxX() && ent.getMinX() < tile.getMinX()) {
                if(ent.getMaxX() - tile.getMinX() < tile.getMaxX() - ent.getMinX()) {
                    penx = ent.getMaxX() - tile.getMinX();
                }
                else if(ent.getMaxX() - tile.getMinX() > tile.getMaxX() - ent.getMinX()) {
                    penx = -(tile.getMaxX() - ent.getMinX());
                }
            }
            else if(tile.getMaxX() > ent.getMaxX() && tile.getMinX() < ent.getMinX()) {
                if(ent.getMaxX() - tile.getMinX() < tile.getMaxX() - ent.getMinX()) {
                    penx = ent.getMaxX() - tile.getMinX();
                }
                else if(ent.getMaxX() - tile.getMinX() > tile.getMaxX() - ent.getMinX()) {
                    penx = -(tile.getMaxX() - ent.getMinX());
                }
            }
        }
        else if(peny == 0 && penx != 0) {
            if(ent.getMaxY() > tile.getMaxY() && ent.getMinY() < tile.getMinY()) {
                if(ent.getMaxY() - tile.getMinY() < tile.getMaxY() - ent.getMinY()) {
                    peny = ent.getMaxY() - tile.getMinY();
                }
                else if(ent.getMaxY() - tile.getMinY() > tile.getMaxY() - ent.getMinY()) {
                    peny = -(tile.getMaxY() - ent.getMinY());
                }
            }
            else if(tile.getMaxY() > ent.getMaxY() && tile.getMinY() < ent.getMinY()) {
                if(ent.getMaxY() - tile.getMinY() < tile.getMaxY() - ent.getMinY()) {
                    peny = ent.getMaxY() - tile.getMinY();
                }
                else if(ent.getMaxY() - tile.getMinY() > tile.getMaxY() - ent.getMinY()) {
                    peny = -(tile.getMaxY() - ent.getMinY());
                }
            }
        }
        if(Math.abs(penx) > Math.abs(peny) && peny != 0) {
            penx = 0;
        }
        else if(Math.abs(penx) < Math.abs(peny) && penx != 0) {
            peny = 0;
        }
        
        if(peny <= 0 && penx == 0) {
            ent.setIsFalling(false);
        }
        if(peny > 0 && penx == 0) {
            peny = peny+2;
            ent.setVelocityY(0);
            ent.setIsFalling(true);
        }
        
        // apply penetration corrections
        ent.setMinX(ent.getMinX() + penx);
        ent.setMinY(ent.getMinY() + peny);
        
        return true;
    }

    public void setWin(boolean win) {
        this.win = win;
    }
    
    /**
     * saveToFile: Writes the fields of this game to an ObjectOutputStream
     */
    public void saveToFile() {
        System.out.println("Saving game to file...");
        FileOutputStream aFileStream = null;
        ObjectOutputStream anOutputStream = null;
        
        try {
            aFileStream = new FileOutputStream(theSaveFilename);
            anOutputStream = new ObjectOutputStream(aFileStream);
            anOutputStream.writeBoolean(true);
            anOutputStream.writeObject(player);
            anOutputStream.writeObject(healthbar);
            anOutputStream.writeObject(traps);
            anOutputStream.writeObject(entities);
            anOutputStream.flush();
            anOutputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't open file " + theSaveFilename
                    + " to save game state.");
        } catch (IOException e) {
            System.out.println("Error saving game state.");
            e.printStackTrace();
        }
    }
    
    /**
     * restoreFromFile: Restores the fields of this object from an
     * ObjectInputStream
     */
    @SuppressWarnings("unchecked")
    public void restoreFromFile() {
        doRestore = false;
        System.out.println("Restoring game from file...");
        File outFile = new File(theSaveFilename);
        FileInputStream aFileStream = null;
        ObjectInputStream anInputStream = null;
        
        try {
            aFileStream = new FileInputStream(outFile);
            anInputStream = new ObjectInputStream(aFileStream);
            doRestore = anInputStream.readBoolean();
            player = (Entity) anInputStream.readObject();
            healthbar = (ArrayList<Entity>) anInputStream.readObject();
            traps = (ArrayList<Entity>) anInputStream.readObject();
            entities = (ArrayList<Entity>) anInputStream.readObject();
            anInputStream.close();
            outFile.delete();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot restore game from file "
                    + theSaveFilename);
        } catch (IOException e) {
            System.out
                    .println("Error reading serialized objects from saved state.");
            doRestore = false;
        } catch (ClassNotFoundException e) {
            System.out
                    .println("Unidentified object in saved state; cannot restore game");
        }
    }
}

