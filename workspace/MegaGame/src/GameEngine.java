import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * File: GameEngine.java
 * Authors: B. Adam, C. Buescher, T. Pickens, C. Schmunsler
 * Last Modified By: C. Buescher
 */

/**
 * GameEngine: Implementation of a game loop. Runs a game implementing the
 * IGame interface.
 */
public class GameEngine {
    /** the number of frames per second that we want to run the game at */
    private final int FPS = 60;
    
    /** flag indicating that we're playing the game */
    private boolean playingGame = true;
    
    /** the game to run */
    private Game theGame;
    
    /** a map of physical inputs to names */
    private static HashMap<PhysicalInput, String> inputMap;
    
    /** a list of inputs to listen for */
    private ArrayList<Input> inputs;
    
    /** the number of timer ticks per second */
    private static long timerTicksPerSecond = Sys.getTimerResolution();
    
    /**
     * The time at which the last rendering looped started from the point
     * of view of the game logic
     */
    private long lastLoopTime = getTime();
    
    /** SoundManager to make sound with */
    private static SoundManager soundManager;
    
    /** the display object to use */
    private IDisplay theDisplay = null;

    /** The time since the last record of fps */
    private long                    lastFpsTime;

    /** The recorded fps */
    private int                     fps;
    
    /**
     * Constructor: Constructor for the game engine. Sets up the inputs.
     */
    public GameEngine() {
        initInput();
        initSound();
    }
    
    private void initInput() {
     // initialize input map
        inputMap = new HashMap<PhysicalInput, String>();
        
        // populate input map
        inputMap.put(PhysicalInput.KEYBOARD_BACK, "Keyboard:Back");
        inputMap.put(PhysicalInput.KEYBOARD_TAB, "Keyboard:Tab");
        inputMap.put(PhysicalInput.KEYBOARD_RETURN, "Keyboard:Return");
        inputMap.put(PhysicalInput.KEYBOARD_LEFT_SHIFT,
                "Keyboard:Left Shift");
        inputMap.put(PhysicalInput.KEYBOARD_LEFT_CONTROL,
                "Keyboard:Left Control");
        inputMap.put(PhysicalInput.KEYBOARD_LEFT_ALT, "Keyboard:Left Alt");
        inputMap.put(PhysicalInput.KEYBOARD_PAUSE, "Keyboard:Pause");
        inputMap.put(PhysicalInput.KEYBOARD_CAPS_LOCK,
                "Keyboard:Caps Lock");
        inputMap.put(PhysicalInput.KEYBOARD_ESCAPE, "Keyboard:Escape");
        inputMap.put(PhysicalInput.KEYBOARD_PG_UP, "Keyboard:Pg Up");
        inputMap.put(PhysicalInput.KEYBOARD_PG_DOWN, "Keyboard:Pg Down");
        inputMap.put(PhysicalInput.KEYBOARD_END, "Keyboard:End");
        inputMap.put(PhysicalInput.KEYBOARD_HOME, "Keyboard:Home");
        inputMap.put(PhysicalInput.KEYBOARD_LEFT, "Keyboard:Left");
        inputMap.put(PhysicalInput.KEYBOARD_UP, "Keyboard:Up");
        inputMap.put(PhysicalInput.KEYBOARD_RIGHT, "Keyboard:Right");
        inputMap.put(PhysicalInput.KEYBOARD_DOWN, "Keyboard:Down");
        inputMap.put(PhysicalInput.KEYBOARD_SYSRQ, "Keyboard:SysRq");
        inputMap.put(PhysicalInput.KEYBOARD_INSERT, "Keyboard:Insert");
        inputMap.put(PhysicalInput.KEYBOARD_DELETE, "Keyboard:Delete");
        inputMap.put(PhysicalInput.KEYBOARD_0, "Keyboard:0");
        inputMap.put(PhysicalInput.KEYBOARD_1, "Keyboard:1");
        inputMap.put(PhysicalInput.KEYBOARD_2, "Keyboard:2");
        inputMap.put(PhysicalInput.KEYBOARD_3, "Keyboard:3");
        inputMap.put(PhysicalInput.KEYBOARD_4, "Keyboard:4");
        inputMap.put(PhysicalInput.KEYBOARD_5, "Keyboard:5");
        inputMap.put(PhysicalInput.KEYBOARD_6, "Keyboard:6");
        inputMap.put(PhysicalInput.KEYBOARD_7, "Keyboard:7");
        inputMap.put(PhysicalInput.KEYBOARD_8, "Keyboard:8");
        inputMap.put(PhysicalInput.KEYBOARD_9, "Keyboard:9");
        inputMap.put(PhysicalInput.KEYBOARD_A, "Keyboard:A");
        inputMap.put(PhysicalInput.KEYBOARD_B, "Keyboard:B");
        inputMap.put(PhysicalInput.KEYBOARD_C, "Keyboard:C");
        inputMap.put(PhysicalInput.KEYBOARD_D, "Keyboard:D");
        inputMap.put(PhysicalInput.KEYBOARD_E, "Keyboard:E");
        inputMap.put(PhysicalInput.KEYBOARD_F, "Keyboard:F");
        inputMap.put(PhysicalInput.KEYBOARD_G, "Keyboard:G");
        inputMap.put(PhysicalInput.KEYBOARD_H, "Keyboard:H");
        inputMap.put(PhysicalInput.KEYBOARD_I, "Keyboard:I");
        inputMap.put(PhysicalInput.KEYBOARD_J, "Keyboard:J");
        inputMap.put(PhysicalInput.KEYBOARD_K, "Keyboard:K");
        inputMap.put(PhysicalInput.KEYBOARD_L, "Keyboard:L");
        inputMap.put(PhysicalInput.KEYBOARD_M, "Keyboard:M");
        inputMap.put(PhysicalInput.KEYBOARD_N, "Keyboard:N");
        inputMap.put(PhysicalInput.KEYBOARD_O, "Keyboard:O");
        inputMap.put(PhysicalInput.KEYBOARD_P, "Keyboard:P");
        inputMap.put(PhysicalInput.KEYBOARD_Q, "Keyboard:Q");
        inputMap.put(PhysicalInput.KEYBOARD_R, "Keyboard:R");
        inputMap.put(PhysicalInput.KEYBOARD_S, "Keyboard:S");
        inputMap.put(PhysicalInput.KEYBOARD_T, "Keyboard:T");
        inputMap.put(PhysicalInput.KEYBOARD_U, "Keyboard:U");
        inputMap.put(PhysicalInput.KEYBOARD_V, "Keyboard:V");
        inputMap.put(PhysicalInput.KEYBOARD_W, "Keyboard:W");
        inputMap.put(PhysicalInput.KEYBOARD_X, "Keyboard:X");
        inputMap.put(PhysicalInput.KEYBOARD_Y, "Keyboard:Y");
        inputMap.put(PhysicalInput.KEYBOARD_Z, "Keyboard:Z");
        inputMap.put(PhysicalInput.KEYBOARD_LEFT_WINDOWS,
                "Keyboard:Left Windows");
        inputMap.put(PhysicalInput.KEYBOARD_RIGHT_WINDOWS,
                "Keyboard:Right Windows");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_0, "Keyboard:Num 0");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_1, "Keyboard:Num 1");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_2, "Keyboard:Num 2");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_3, "Keyboard:Num 3");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_4, "Keyboard:Num 4");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_5, "Keyboard:Num 5");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_6, "Keyboard:Num 6");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_7, "Keyboard:Num 7");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_8, "Keyboard:Num 8");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_9, "Keyboard:Num 9");
        inputMap.put(PhysicalInput.KEYBOARD_MULTIPLY, "Keyboard:Multiply");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_PLUS, "Keyboard:Num +");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_MINUS, "Keyboard:Num -");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_DOT, "Keyboard:Num .");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_DIVIDE, "Keyboard:Num /");
        inputMap.put(PhysicalInput.KEYBOARD_F1, "Keyboard:F1");
        inputMap.put(PhysicalInput.KEYBOARD_F2, "Keyboard:F2");
        inputMap.put(PhysicalInput.KEYBOARD_F3, "Keyboard:F3");
        inputMap.put(PhysicalInput.KEYBOARD_F4, "Keyboard:F4");
        inputMap.put(PhysicalInput.KEYBOARD_F5, "Keyboard:F5");
        inputMap.put(PhysicalInput.KEYBOARD_F6, "Keyboard:F6");
        inputMap.put(PhysicalInput.KEYBOARD_F7, "Keyboard:F7");
        inputMap.put(PhysicalInput.KEYBOARD_F8, "Keyboard:F8");
        inputMap.put(PhysicalInput.KEYBOARD_F9, "Keyboard:F9");
        inputMap.put(PhysicalInput.KEYBOARD_F10, "Keyboard:F10");
        inputMap.put(PhysicalInput.KEYBOARD_F11, "Keyboard:F11");
        inputMap.put(PhysicalInput.KEYBOARD_F12, "Keyboard:F12");
        inputMap.put(PhysicalInput.KEYBOARD_F13, "Keyboard:F13");
        inputMap.put(PhysicalInput.KEYBOARD_F14, "Keyboard:F14");
        inputMap.put(PhysicalInput.KEYBOARD_F15, "Keyboard:F15");
        inputMap.put(PhysicalInput.KEYBOARD_NUM_LOCK, "Keyboard:Num Lock");
        inputMap.put(PhysicalInput.KEYBOARD_SCROLL_LOCK,
                "Keyboard:Scroll Lock");
        inputMap.put(PhysicalInput.KEYBOARD_COMMA, "Keyboard:,");
        inputMap.put(PhysicalInput.KEYBOARD_DOT, "Keyboard:.");
        inputMap.put(PhysicalInput.KEYBOARD_TILDE, "Keyboard:~");
        inputMap.put(PhysicalInput.KEYBOARD_LEFT_BRACKET, "Keyboard:[");
        inputMap.put(PhysicalInput.KEYBOARD_RIGHT_BRACKET, "Keyboard:]");
        inputMap.put(PhysicalInput.MOUSE_X, "Mouse:x");
        inputMap.put(PhysicalInput.MOUSE_Y, "Mouse:y");
        inputMap.put(PhysicalInput.MOUSE_Z, "Mouse:z");
        inputMap.put(PhysicalInput.MOUSE_LEFT, "Mouse:Left");
        inputMap.put(PhysicalInput.MOUSE_RIGHT, "Mouse:Right");
        inputMap.put(PhysicalInput.MOUSE_MIDDLE, "Mouse:Middle");
    }
    
    private void initSound() {
        soundManager = new SoundManager();
        soundManager.initialize(8);
    }
    
    /**
     * setDisplay: Sets the display
     * 
     * @param aDisplay
     *            the display to use, if not null
     * @throws Exception
     *             if the display is null
     */
    private void setDisplay(IDisplay aDisplay) throws Exception {
        if (aDisplay == null) {
            throw new Exception("Null display");
        }
        theDisplay = aDisplay;
    }
    
    /**
     * run: runs the game loop on a game implementing the IGame interface.
     * 
     * @param aGame
     *            the game to run.
     * @throws Exception
     */
    public void run(Game aGame) throws Exception {
        theGame = aGame;
        setDisplay(theGame.getDisplay());
        theDisplay.init();
        inputs = theGame.init();
        gameLoop();
        theGame.shutdown();
    }
    
    /**
     * gameLoop: a timer-based game loop to run the game
     */
    private void gameLoop() {
        // Flag to tell us if we need to calculate the delta differently
        boolean firstLoop = true;
        
        // Game loop runs while the player is playing
        while (playingGame) {
            // clear screen
            theDisplay.reset();
            
            // Get the input
            getInput();
            
            // Calculate time delta since last update
            if (firstLoop) {
                lastLoopTime = getTime();
                firstLoop = false;
            }
            long delta = getTime() - lastLoopTime;
            lastLoopTime = getTime();
            lastFpsTime += delta;
            fps++;
            
            if (lastFpsTime > 1000) {
                Display.setTitle("(FPS: " + fps + ")");
                lastFpsTime = 0;
                fps = 0;
            }
            
            // Update the world
            theGame.update(delta);
            
            // Paint the graphics
            render();
            
            // update window contents
            theDisplay.update();
            if (theGame.isDone()) {
                playingGame = false;
            }
        }
        
        // clean up
        soundManager.destroy();
        
        // Close Game window
        theDisplay.quit();
    }
    int waitPoll = 0;
    
    /**
     * getInput: Get a list of the input components to track
     */
    private void getInput() {
        for (Input i : inputs) {
            //System.out.println("i = "+i);
            //I don't know why I have to do this for mouse to work
            //long startTime = System.currentTimeMillis();
            //while (System.currentTimeMillis() - startTime < 1) {
            //}
            boolean hasDown = false;
            float pollValue = 0.0f;
            String deviceType = "";
            for (PhysicalInput p : i.getPhysicalInputs()) {
                String physinputname = inputMap.get(p);
                String inputname = physinputname.substring(physinputname.indexOf(':') + 1).toLowerCase();
                String inputType = physinputname.substring(0, physinputname.indexOf(':')).toLowerCase();
                ControllerEnvironment ce = ControllerEnvironment
                        .getDefaultEnvironment();
                Controller[] controllerList = ce.getControllers();
                ArrayList<Controller> gameControllerList = new ArrayList<Controller>();
                for (Controller c : controllerList) {
                    // Why is this hard coded?
                        // Add controller to the list of controllers we
                        // want to check for game input`
                        gameControllerList.add(c);
                    //}
                }
                // Check each controller for input
                for (Controller controller : gameControllerList) {
                    if (controller.getName().toLowerCase().contains("keyboard")) {
                        deviceType = "keyboard";
                    } else if (controller.getName().toLowerCase().contains("mouse")) {
                        deviceType = "mouse";
                    } else {
                        deviceType = "unknown";
                    }
                    if (controller != null && hasDown == false) {
                        Component[] components = controller
                                .getComponents();
                        Component component = null;
                        for (Component com : components) {
                            if (com.getName().toLowerCase().equals(inputname) && deviceType.equals(inputType)) {
                                component = com;
                            }
                        }
                        if (component != null) {
                            //seems to be an issue with not getting poll data > 0 for the mouse if you poll too often
                            //so I'm only polling every 5 loops
                            waitPoll++;
                            if (waitPoll > 5) {
                                controller.poll();
                                waitPoll = 0;
                            }
                            float polldata = component.getPollData();
                            if (i instanceof Button) {
                                if (polldata == 1.0f) {
                                    hasDown = true;
                                }
                            } else {
                                //((Axis) i).setValue(polldata);
                                if (polldata != 0) {
                                    pollValue = polldata;
                                }
                            }
                        }
                    }
                }
                
            }
            if (i instanceof Button) {
                if (hasDown) {
                    ((Button) i).setDown(true);
                } else {
                    ((Button) i).setDown(false);
                }
            }else if (i instanceof Axis) {
                ((Axis) i).setValue(pollValue);
            }
        }
    }
    
    /**
     * Get the high resolution time in milliseconds
     * 
     * @return The high resolution time in milliseconds
     */
    public static long getTime() {
        // we get the "timer ticks" from the high resolution timer
        // multiply by 1000 so our end result is in milliseconds
        // then divide by the number of ticks in a second giving
        // us a nice clear time in milliseconds
        return (Sys.getTime() * 1000) / timerTicksPerSecond;
    }
    
    /**
     * render: Syncs the display to FPS
     */
    public void render() {
        theDisplay.sync(FPS);
        drawEntities();
    }
    
    /**
     * drawEntities: calls draw on all the entities in the Game
     */
    public void drawEntities() {
        for (Entity ent : theGame.getEntities()) {
            ent.draw();
        }
    }

    public static int addSound(String fileName) {
        return soundManager.addSound(fileName);
    }
    
    public static void playSound(int soundNum) {
        soundManager.playEffect(soundNum);
    }
    
    public static void playMusic(int soundNum) {
        soundManager.playSound(soundNum);
    }
    
    public static void setMouseHidden(boolean mouseHidden) {
        Mouse.setGrabbed(mouseHidden);
    }
    
    /**
     * collisionBetween: detects collisions between 2 entities, using Java
     * Rectangles
     * 
     * @param e1
     *            the first entity
     * @param e2
     *            the second entity
     * @return true if there's a collision, false otherwise
     */
    static boolean collisionBetween(Entity e1, Entity e2) {
        Rectangle ent1 = new Rectangle();
        Rectangle ent2 = new Rectangle();
        
        ent1.setBounds((int) e1.getMaxX(), (int) e1.getMaxY(), e1
                .getSprite().getWidth(), e1.getSprite().getHeight());
        ent2.setBounds((int) e2.getMaxX(), (int) e2.getMaxY(), e2
                .getSprite().getWidth(), e2.getSprite().getHeight());
        
        boolean ret = ent1.intersects(ent2);
        return ret;
    }
    
    static Config getConfig(String filename) throws IOException {
        return new Config(filename, false);
    }
}
