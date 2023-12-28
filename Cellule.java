package bataillenavaleapo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class Cellule allows to create Cellule objects and to execute associated methods
 * A cell is the basic component of a grid
 * @author Arthur Rubio
 */

public class Cellule extends Rectangle{
    public int x,y; // The coordinates are public to access them in main
    private Bateau bateau; // The ship 
    private boolean dejaJoue=false; // A boolean to know if we have already played this cell before
    private Grille grille; // The grid of the cell
    
    /**
     * The Cellule constructor creates a Cellule with 3 parameters. 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param grille the grid on which the cell will be placed
     */
    
    public Cellule(int x, int y, Grille grille){
        super(40,40);
        setx(x);
        sety(y);
        setGrille(grille);
        setFill(Color.WHITESMOKE); // Base color
        setStroke(Color.BLACK);
    }
    
    /** 
     * This getter returns the x coordinate
     * @return x the x coordinate
     */
    public int getx(){ // not getX because it already exists in Rectangle
        return x;
    }

    /**
     * This setter defines the x coordinate by checking its validity
     * @param x2 
     */
    public void setx(int x2){
        if (x2>=0 && x2<10){
            x=x2;
        }
    }

    /** 
     * This getter returns the y coordinate
     * @return y the y coordinate
     */
    public int gety(){
        return y;
    }

    /**
     * This setter defines the y coordinate by checking its validity
     * @param y2 
     */
    public void sety(int y2){
        if (y2>=0 && y2<10){
            y=y2;
        }
    }

    /**
     * This getter returns the Bateau of the cell
     * @return 
     */
    public Bateau getBateau(){
        return bateau;
    }

    /**
     * This setter allows to assign a ship to a cell
     * @param bateau2 
     */
    public void setBateau(Bateau bateau2){
        if (bateau2!=null){
            bateau=bateau2;
        }
    }

    /** 
     * This getter returns true if the cell has already been played
     * @return dejaJoue a boolean
     */
    
    public boolean getDejaJoue(){
        return dejaJoue;
    }

    /**
     * This setter allows to define by a boolean if the cell has already been played
     * @param dejaJoue2 
     */
    
    public void setDejaJoue(boolean dejaJoue2){
        dejaJoue=dejaJoue2;
    }

    /**
     * This getter returns the grid
     * @return la grille 
     */
    public Grille getGrille(){
        return grille;
    }
    
    /**
     * This setter allows to define a grid to the cell
     * @param grille2 
     */    
    public void setGrille(Grille grille2){
        if (grille2!=null){
            grille=grille2;
        }
    }

    /**
     * This method allows to shoot on a cell.
     * She looks if there is a ship on the cell.
     * Color the cell in blue if not.
     * Color the cell in orange if yes.
     * Color all the cells of the ship in red if the ship has been sunk by the shot.
     * @return un boolean true si un bateau a été touché ou coulé 
     */
    public boolean tirerSurCellule(){
        if (getDejaJoue())
                // If True it means that the cell has already been played with the shoot() method
                return false;
        dejaJoue = true;
        setFill(Color.BLUE);
        if (bateau == null){
            System.out.println("--- Too bad, you missed!");
        }
        if (bateau !=null){
            bateau.diminuerLongueurBateau();
            setFill(Color.ORANGE);
            System.out.println("--- Ship hit!");
            
            if (!bateau.bateauEnVie()){
                grille.setNbBateaux(grille.getNbBateaux()-1);
                for (Cellule cellule:bateau.getLcell()){
                    cellule.setFill(Color.RED);
                }
                
                System.out.println("--- A "+bateau.getNom() +" has been sunk!");
                System.out.println("There is "+grille.getNbBateaux()+" ships remaining to sink.");
            }
            return true;
        }
        return false;
    }
}