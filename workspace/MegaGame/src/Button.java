import java.io.Serializable;

/**
 * 
 * Button
 */
public class Button extends Input implements Serializable {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8076775535697065560L;
    private boolean down;
    
    public Button(PhysicalInput[] p) {
        super(p);
        down = false;
    }
    
    public boolean isDown() {
        return down;
    }
    
    public void setDown(boolean down) {
        this.down = down;
    }
    
}
