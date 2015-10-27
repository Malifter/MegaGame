

/**
 * 
 * Axis: 
 */
public class Axis extends Input {
	
	
	private float value; // should be between -1 and +1, or whatever
	
	public Axis (PhysicalInput[] p) {
		super(p);
		value = 0;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	
	public float getValue() {
		return this.value;
	}
}
