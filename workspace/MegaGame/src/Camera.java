/**
 * File: Camera.java
 * Authors: MegaGame team
 * Last Modified By: 
 */

import static org.lwjgl.util.glu.GLU.gluLookAt;

import org.lwjgl.opengl.GL11;

/**
 * Camera 
 */
public class Camera {
    
    private static final int TILESIZE = 16;
    private static final int screenHeight = 256;
    private static final int levelWidth = 680;
    Entity focus;
    float posX, posY, posZ;
    float upX, upY;
    float offsetX, offsetY;
    MegaManGame game;
    boolean mode = true;
    boolean switchMode = false;
    
    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public Camera(Game g) {
        game = (MegaManGame)g;
        offsetX = (screenHeight/2)+34; //focus' half width - window half width
        offsetY = -(levelWidth/2); //focus' half height - window half height
    }
    
    public void setFocusEntity(Entity e) {
        focus = e;
    }
    
    public void setOrientation(float posX, float posY, float posZ, float upX, float upY) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.upX = upX;
        this.upY = upY;
    }
    
    public void setMode(boolean m) {
        mode = m;
    }
    
    public void update() {
        if(game.cameraMode.isDown() && !switchMode) {
            mode = !mode;
            switchMode = true;
        } else if (!game.cameraMode.isDown() && switchMode) {
            switchMode = false;
            
        }
        
        if(mode) {
            if(focus != null) {
                posX = focus.minX-offsetX;
                if(posX <= 0) {
                    posX = 0;
                } else if (posX >= levelWidth) {
                    posX = levelWidth;
                }
                posY = ((float)((int)focus.midY/screenHeight)*screenHeight)+(TILESIZE/2);//+offsetY;
            }
            GL11.glLoadIdentity();
            GL11.glScaled(3, 3, 0);
            GL11.glTranslatef(-posX, -posY, 0);
        }
        else {
            posY = 0;
            posX = 0;
        }
        //gluLookAt(posX,posY,.1f,posX,posY,-.7f,upX,upY,0f);
    }
    
    public float getPosX() {
        return posX;
    }
    
    public float getPosY() {
        return posY;
    }

//    void lookAt(Vector, Vector, Vector);
//    void lookAt(Vector, Object*, Vector, bool follow);
//    void lookRelativeAt(Vector, Object*, Vector, bool follow);
//    void setEye(Vector);
//    void setCenter(Vector);
//    void setUp(Vector);
//    void setAnchor(Object*);
//    void setFollow(bool follow = true);
//
//    void setOrtho(double  left,  double  right,  double  bottom,  double  top,  double  nearVal,  double  farVal);
//    void setPerspective(double  fovy,  double  aspect,  double  zNear,  double  zFar);
    
}
