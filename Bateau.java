package bataillenavaleapo;

import java.util.List;
import javafx.scene.Parent;

/**
 * The Bateau class allows to create ships and to call associated methods.
 * She allows to create objects of the Bateau class and to execute associated methods.
 * 
 * @author Arthur Rubio
 */

public class Bateau extends Parent{
    
    // The attributes longueurinit is the original length of the ship when it is created
    private int initialLen;
    // The boolean vertical is True if the ship is vertical, False if it is horizontal
    private boolean vertical = true;
    /**
     * The attribute longueurrestante is the remaining length of the ship between 0 and longueurinit
     * @see Bateau#diminuateLenShip() 
     * @see Bateau#getLongueurrestante() 
    */
    private int remainingLen;
    // The string nom is the name of the ship
    private String name;
    private List<Cellule> lcell; // Cell list
    
    /**
     * This is the Bateau constructor.
     * He takes 2 parameters :
     * @param initialLen description of the longueurinit parameter
     * @param vertical boolean that indicates vertical or horizontal
     */
    public Bateau(int initialLen, boolean vertical){
        setInitialLen(initialLen); // Allows to go through the verification of setLongueurinit
        setVertical(vertical); 
        setRemainingLen(initialLen);
        shipName(initialLen);
        lcell=null; // List of empty cells
    }
    
    /**
     * This getter allows to return the initial length
     * @return The initial length
     */
    
    public int getInitialLen(){
        return initialLen;
    }
    
    /**
     * This setter allows to assign a value to the initial length of the ship
     * @param initialLen2 
     */
    
    public void setInitialLen(int initialLen2){
        // Takes a length between 2 and 5
        if (initialLen2>=2 && initialLen2<=5){
            initialLen=initialLen2;
        }
    }
    /**
     * This getter allows to return a boolean indicating if the ship is vertical
     * @return vertical
     */
    public boolean getVertical(){
        return vertical;
    }

    /**
     * This setter allows to define if the ship is vertical
     * @param vertical2 a boolean indicating if the ship is vertical
     */
    public void setVertical(boolean vertical2){
        vertical=vertical2;
    }

    /**
     * This getter allows to return the remaining length of the ship
     * @return remaining length of the ship
     */
    public int getRemainingLen(){
        return remainingLen;
    }
    
    /**
     * This setter allows to define the remaining length of a ship
     * verifiying that the length is between 2 and 5.
     * @param remainingLen2 
     */
    
    public void setRemainingLen(int remainingLen2){
        if (remainingLen2>=2 && remainingLen2<=5){
            remainingLen=remainingLen2;
        }
    }

    /** 
     * This getter allows to return the name of the ship
     * @return name, the name of the ship
     */
    
    public String getName(){
        return name;
    }

    /** 
     * This setter allows to define the name of the ship
     * @param name2 
     */
    
    public void setName(String name2){
        if (name2!=null && name2.length()>0){
            name=name2;
        }
    }

    /**
     * This getter returns a list of cells composing the ship
     * @return 
     */
    public List<Cellule> getLcell(){
        return lcell;
    }

    /**
     * This setter allows to assign a list of cells to a ship
     * @param lcell2 the list of cells
     */
    
    public void setLcell(List<Cellule> lcell2){
        if (lcell2!=null){
            lcell=lcell2;
        }
    }

    /**
     * This method allows to decrement the length of a ship
     */
    public void diminuateLenShip(){
        remainingLen--;
    }

    /** 
     * This method allows to check if a ship has a non-zero remaining length 
     * @return True if the ship has a non-zero remaining length 
     */
    public boolean shipAlive(){
        return remainingLen>0;
    }

    /**
     * Cette méthode permet d'attribuer une chaine nom à un bateau selon sa longueur i 
     * This method allows to assign a name to a ship according to its length i
     * @param i length of the ship
     */
    
    // No need of default because it is not possible that the length is different from 2, 3, 4 or 5 thanks to the setter
    public void shipName(int i){
        switch(i){
            case 2:
                setName("Torpedo ship of 2 squares");
                break;
            case 3:
                setName("Destroyer of 3 squares");
                break;
            case 4:
                setName("Cruiser of 4 squares");
                break;
            case 5:
                setName("Aircraft carrier of 5 squares");
                break;
            default :
                setName("Unconventional ship");
                break;
        }
    }
}