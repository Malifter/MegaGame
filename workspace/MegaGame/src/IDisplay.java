/**
 * File: IDisplay.java
 * Authors: B. Adam, C. Buescher, T. Pickens, C. Schmunsler
 * Last Modified By: C. Buescher
 */

/**
 * IDisplay: an interface for graphics functionality for the game
 * engine.
 */
public interface IDisplay {
    /**
     * init: initialize and setup display
     */
    public boolean init();
    
    /**
     * update: update the display
     */
    public void update();
    
    /**
     * quit: quit the graphics
     */
    public void quit();
    
    /**
     * sync: sync to a given frame rate
     * 
     * @param fps
     *            the desired frame rate in frames/sec
     */
    public void sync(int fps);
    
    /**
     * reset: reset the display
     */
    public void reset();
    
    /**
     * getSprite: gets a sprite based on filename
     * 
     * @param aFilename
     *            the file to get
     * @return a Sprite object
     */
    public Sprite getSprite(String aFilename);
    
    /**
     * setTitle: Set the title of the display
     * 
     * @param aTitle
     */
    public void setTitle(String aTitle);
}
