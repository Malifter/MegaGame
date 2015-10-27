
/**
 * 
 * ShipEntity: ship entity used for space invaders game. 
 * 
 */
public class ShipEntity extends Entity {
    Game theGame;
    
    public ShipEntity(Game g, String file, int x, int y) {
        super(g, file, x, y);
        image = "ship.gif";
        this.theGame = g;
    }
    
    public void destroy() {
        theGame.removeEntity(this);
        theGame.notifyDeath();
    }
}
