import static org.lwjgl.opengl.GL11.*;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * File: JLWGLDisplay.java
 * Authors: B. Adam, C. Buescher, T. Pickens, C. Schmunsler
 * Last Modified By: C. Buescher
 */

/**
 * JLWGLDisplay: implementation of the {@link IDisplay} interface for the
 * Java Lightweight Game Library
 */
public class JLWGLDisplay implements IDisplay {
    private String theWindowTitle = "";
    private int theWidth = 800;
    private int theHeight = 600;
    private TextureLoader theTextureLoader = new TextureLoader();
    
    public JLWGLDisplay(String aWindowTitle, int aWidth, int aHeight) {
        if (aWindowTitle != null) {
            theWindowTitle = aWindowTitle;
        }
        
        if (aWidth != 0) {
            theWidth = aWidth;
        }
        
        if (aHeight != 0) {
            theHeight = aHeight;
        }
    }
    
    @Override
    public boolean init() {
        // initialize the window beforehand
        try {
            setDisplayMode();
            Display.setTitle(theWindowTitle);
            Display.create();
            
            // enable textures since we're going to use these for our
            // sprites
            glEnable(GL_TEXTURE_2D);
            // disable the OpenGL depth test since we're rendering 2D
            // graphics
            //glDisable(GL_DEPTH_TEST);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, theWidth, theHeight, 0, -1, 1);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glViewport(0, 0, theWidth, theHeight);
            
            glClearColor(0.f, 0.f, 0.f, 0.f);
            glEnable (GL_BLEND);
            glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            
            // System.out.println("Init theTextureLoader");
            // theTextureLoader = new TextureLoader();
            
        } catch (LWJGLException le) {
            System.out
                    .println("Game exiting - exception in initialization:");
            le.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /**
     * Sets the display mode for fullscreen mode
     */
    private boolean setDisplayMode() {
        try {
            // get modes
            DisplayMode[] dm = org.lwjgl.util.Display
                    .getAvailableDisplayModes(theWidth, theHeight, -1, -1,
                            -1, -1, 60, 60);
            
            org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
                    "width=" + theWidth,
                    "height=" + theHeight,
                    "freq=" + 60,
                    "bpp="
                            + org.lwjgl.opengl.Display.getDisplayMode()
                                    .getBitsPerPixel() });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out
                    .println("Unable to enter fullscreen, continuing in windowed mode");
        }
        
        return false;
    }
    
    @Override
    public void update() {
        Display.update();
    }
    
    @Override
    public void quit() {
        Display.destroy();
    }
    
    @Override
    public void sync(int fps) {
        Display.sync(fps);
    }
    
    @Override
    public void reset() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }
    
    @Override
    public Sprite getSprite(String name) {
        return new Sprite(theTextureLoader, name);
    }
    
    @Override
    public void setTitle(String aTitle) {
        Display.setTitle(theWindowTitle + " | " + aTitle);
    }
}
