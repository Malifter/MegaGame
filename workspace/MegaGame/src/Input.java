import java.io.Serializable;

/**
 * File: Input.java
 * Authors: B. Adam, C. Buescher, T. Pickens, C. Schmunsler
 * Last Modified By: C. Buescher
 */

/**
 * Input: A base class to map input devices to buttons or axes
 */
public abstract class Input implements Serializable {
    
    private PhysicalInput[] p;
    
    public Input(PhysicalInput[] p) {
        this.p = p;
    }
    
    public PhysicalInput[] getPhysicalInputs() {
        return p;
    }
    
    public void setPhysicalInputs(PhysicalInput[] p) {
        this.p = p;
    }
}
