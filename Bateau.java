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
    private int longueurinit;
    // The boolean vertical is True if the ship is vertical, False if it is horizontal
    private boolean vertical = true;
    /**
     * The attribute longueurrestante is the remaining length of the ship between 0 and longueurinit
     * @see Bateau#diminuerLongueurBateau() 
     * @see Bateau#getLongueurrestante() 
    */
    private int longueurrestante;
    // The string nom is the name of the ship
    private String nom;
    private List<Cellule> lcell; // Cell list
    
    /**
     * This is the Bateau constructor.
     * He takes 2 parameters :
     * @param longueurinit description of the longueurinit parameter
     * @param vertical boolean that indicates vertical or horizontal
     */
    public Bateau(int longueurinit, boolean vertical){
        setLongueurinit(longueurinit); // Allows to go through the verification of setLongueurinit
        setVertical(vertical); 
        setLongueurrestante(longueurinit);
        nommerBateau(longueurinit);
        lcell=null; // List of empty cells
    }
    
    /**
     * This getter allows to return the initial length
     * @return The initial length
     */
    
    public int getLongueurinit(){
        return longueurinit;
    }
    
    /**
     * This setter allows to assign a value to the initial length of the ship
     * @param longueurinit2 
     */
    
    public void setLongueurinit(int longueurinit2){
        // Takes a length between 2 and 5
        if (longueurinit2>=2 && longueurinit2<=5){
            longueurinit=longueurinit2;
        }
    }
    /**
     * This getter allows to return a boolean indicating if the ship is vertical
     * @return 
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
    public int getLongueurRestante(){
        return longueurrestante;
    }
    
    /**
     * This setter allows to define the remaining length of a ship
     * verifiying that the length is between 2 and 5.
     * @param longueurrestante2 
     */
    
    public void setLongueurrestante(int longueurrestante2){
        if (longueurrestante2>=2 && longueurrestante2<=5){
            longueurrestante=longueurrestante2;
        }
    }

    /** 
     * This getter allows to return the name of the ship
     * @return nom, the name of the ship
     */
    
    public String getNom(){
        return nom;
    }

    /** 
     * This setter allows to define the name of the ship
     * @param nom2 
     */
    
    public void setNom(String nom2){
        if (nom2!=null && nom2.length()>0){
            nom=nom2;
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
    public void diminuerLongueurBateau(){
        longueurrestante--;
    }

    /** 
     * This method allows to check if a ship has a non-zero remaining length 
     * @return True if the ship has a non-zero remaining length 
     */
    public boolean bateauEnVie(){
        return longueurrestante>0;
    }

    /**
     * Cette méthode permet d'attribuer une chaine nom à un bateau selon sa longueur i 
     * This method allows to assign a name to a ship according to its length i
     * @param i length of the ship
     */
    
    // No need of default because it is not possible that the length is different from 2, 3, 4 or 5 thanks to the setter
    public void nommerBateau(int i){
        switch(i){
            case 2:
                setNom("Torpedo ship of 2 squares");
                break;
            case 3:
                setNom("Destroyer of 3 squares");
                break;
            case 4:
                setNom("Cruiser of 4 squares");
                break;
            case 5:
                setNom("Aircraft carrier of 5 squares");
                break;
            default :
                setNom("Unconventional ship");
                break;
        }
    }
}