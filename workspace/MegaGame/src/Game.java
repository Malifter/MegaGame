import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * File: IGame.java
 * Authors: B. Adam, C. Buescher, T. Pickens, C. Schmunsler
 * Last Modified By: C. Buescher
 */

/**
 * IGame: Game interface
 */
public abstract class Game implements Serializable {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4174358208259933141L;
    private String theSaveFilename = "save.ser";
    protected transient GameEngine engine;
    protected ArrayList<Entity> entities;
    protected ArrayList<Entity> removeList;
    protected boolean doRestore;
    protected boolean isDone;
    private transient IDisplay theDisplay;
    
    public Game(GameEngine e) {
        engine = e;
        entities = new ArrayList<Entity>();
        removeList = new ArrayList<Entity>();
        doRestore = false;
        isDone = false;
    }
    
    /**
     * isDone: Return is done; true if the game is done (time to exit);
     * false otherwise.
     * 
     * @return isDone
     */
    public boolean isDone() {
        return isDone;
    }
    
    /**
     * removeEntity: Remove an entity from the game
     * 
     * @param anEntity
     *            the entity to remove
     */
    public void removeEntity(Entity anEntity) {
        removeList.add(anEntity);
    }
    
    /**
     * init: initializes the game and returns a list of the inputs the
     * game is interested in
     * 
     * @return a list of the inputs the game will use
     */
    public ArrayList<Input> init() {
        this.restoreFromFile();
        return null;
    }
    
    public void shutdown() {
      
    }
    
    /**
     * update: updates the Game's world.
     * 
     * @param time
     *            <add description>
     */
    public void update(long time) {
        
    }
    
    /**
     * reInitFromSave: Reinitialize entities after restoring from
     * serialized save file. Stuff like reloading graphics, time-sensitive
     * data, etc.
     */
    protected void reInitFromSave() {
        
    }
    
    /**
     * getDrawables: Returns a list of the entities in the game.
     * 
     * @return
     */
    public ArrayList<Entity> getEntities() {
        return null;
    }
    
    /**
     * Notification that the player has died.
     */
    public void notifyDeath() {
        isDone = true;
    }
    
    public IDisplay getDisplay() {
        return theDisplay;
    }
    
    public void setDisplay(IDisplay aDisplay) throws Exception {
        if (aDisplay == null) {
            throw new Exception("Display is null");
        }
        theDisplay = aDisplay;
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
            anOutputStream.writeBoolean(doRestore);
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
