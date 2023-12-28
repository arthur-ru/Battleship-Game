package bataillenavaleapo;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * The Grille class allows to create grids from cells
 * @author Arthur Rubio
 */

public class Grille extends Parent {
    public VBox ligns = new VBox();
    // Bolean to know if it's the IA's grid or not
    public boolean ai = false;
    private int nbShips = 5;
    
    /**
     * The Grid constructor allows to create a grid.
     * It takes 2 parameters
     * @param ai a boolean that indicates if it's the IA's grid
     * @param mouseClick a mouse event
     */
    public Grille(boolean ai, EventHandler<? super MouseEvent> mouseClick){
        // We create the grid with cells
        this.ai= ai;
        for (int y=0 ; y<10 ; y++){
            HBox columnsOnOneLign = new HBox();
            for (int x=0 ; x<10 ; x++){
                // This refers to the grid mentioned
                Cellule c = new Cellule(x,y,this);
                // We add the mouse event
                c.setOnMouseClicked(mouseClick);
                columnsOnOneLign.getChildren().add(c);
            }
            ligns.getChildren().add(columnsOnOneLign);
        }
        
        getChildren().add(ligns);
    }
    /**
     * This getter allows to know if it's the IA's grid
     * @return IA a boolean 
     */
    public boolean getAi(){
        return ai;
    }

    /**
     * This setter defines if it's the IA's grid using a boolean
     * @param ai2 a boolean that indicates if it's the IA's grid
     */
    public void setAi(boolean ai2){
        ai=ai2;
    }

    /**
     * Ce getter permet de retourner le nb de bateaux de la grille 
     * This getter allows to know the number of ships in the grid
     * @return nbBateaux number of ships in the grid
     */
    
    public int getNbShips(){
        return nbShips;
    }

    /**
     * This setter defines the number of ships in the grid
     * @param nbShips2 the number of ships in the grid
     */
    public void setNbShips(int nbShips2){
        if (nbShips2>=0){
            nbShips=nbShips2;
        }
    }

    /**
     * Cette méthode renvoie true si le bateau est placé 
     * This method returns true if the ship is placed
     * @param ship with his length and orientation
     * @param x the x coordinate
     * @param y the y coordinate
     * @return True if the ship is placed on the grid
     */
    
    public boolean placeShips(Bateau ship, int x, int y){
        if (canPlaceShip(ship,x,y)){
            int longueur=ship.getInitialLen();
            ship.setLcell(new ArrayList<>());
            
            if (ship.getVertical()){
                for (int i=y; i<y+longueur; i++){
                    Cellule cellule = getCell(x,i);
                    cellule.setShip(ship);
                    // We add the cell to the list of cells
                    ship.getLcell().add(cellule);
                    if (!ai){
                        // Allows to only fill on the player's grid
                        cellule.setFill(Color.BLACK);
                        cellule.setStroke(Color.GREEN);
                    }
                }
            }
            else {
                for(int i=x; i<x+longueur; i++){
                    Cellule cellule = getCell(i,y);
                    cellule.setShip(ship); 
                    // We add the cell to the list of cells
                    ship.getLcell().add(cellule);
                    if (!ai){
                        cellule.setFill(Color.BLACK);
                        cellule.setStroke(Color.GREEN);
                    }
                }
            }
            return true;
        }
        return false; 
    }
    
    /**
     * Cette méthode renvoie une cellule située aux coordos x,y
     * This method returns a cell located at the coordinates x, y
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the cell at these coordinates
     */
    public Cellule getCell(int x, int y){
        return (Cellule) ( (HBox) ligns.getChildren().get(y)).getChildren().get(x);
        
    }

    /**
     * This method returns an array of neighboring cells
     * @param x the x coordinate 
     * @param y the y coordinate
     * @return array of Cellule[]
     */
    public Cellule[] getNeighbringCells(int x, int y){
        Point2D[] points = new Point2D[]{
            new Point2D(x-1, y),
            new Point2D(x+1 , y ),
            new Point2D(x, y-1),
            new Point2D(x, y+1)};
        List<Cellule> voisins = new ArrayList<Cellule> ();
        
        for (Point2D p : points){
            if (isValidPoint(p)){
                voisins.add(getCell((int)p.getX(), (int)p.getY()));
                
            }
        
        }
        return voisins.toArray(new Cellule[0]);
    }
    
    
    /**
     * This method returns an array of non-blue neighboring cells
     * @param x the x coordinate
     * @param y the y coordinate
     * @return array of Cellule[] not blue
     */
    public Cellule[] getNeighbringCellsNonBlue(int x, int y){
        Point2D[] points = new Point2D[]{
            new Point2D(x-1, y),
            new Point2D(x+1 , y ),
            new Point2D(x, y-1),
            new Point2D(x, y+1)};
        List<Cellule> voisins = new ArrayList<Cellule> ();
        for (Point2D p : points){
            if (isValidPoint(p) && ! (getCell((int)p.getX(),(int)p.getY()).getFill().equals(Color.BLUE) )){
                voisins.add(getCell((int)p.getX(), (int)p.getY()));
                
            }
        
        }
        return voisins.toArray(new Cellule[0]);
    }

    
    
    
    /**
     * Cette methode verifie si on peut placer un bateau à un certain emplacement x,y.
     * This method checks if we can place a ship at a certain location x, y. 
     * @param bateau the ship with its length and orientation
     * @param x the x coordinate
     * @param y the y coordinate
     * @return True if we can place the ship at these coordinates
     */
    private boolean canPlaceShip(Bateau bateau, int x, int y) {
        // Return True if we can place the ship at these coordinates
        int longueur = bateau.getInitialLen();

        // If the ship is vertical
        if (bateau.getVertical()) {
            for (int i = y; i < y + longueur; i++) {
                if (!isValidPoint(x, i))
                    return false;

                Cellule cellule = getCell(x, i);
                if (cellule.getShip() != null)
                    return false;

                for (Cellule voisin : getNeighbringCells(x, i)) {
                    if (!isValidPoint(x, i))
                        return false;

                    if (voisin.getShip() != null)
                        return false;
                }
            }
        }
        // If the ship is horizontal
        else {
            for (int i = x; i < x + longueur; i++) {
                if (!isValidPoint(i, y))
                    return false;

                Cellule cellule = getCell(i, y);
                if (cellule.getShip() != null)
                    return false;

                for (Cellule voisin : getNeighbringCells(i, y)) {
                    if (!isValidPoint(i, y))
                        return false;

                    if (voisin.getShip() != null)
                        return false;
                }
            }
        }
        // If we can place the ship at these coordinates
        return true; 
    }
    
    /** 
     * This method returns true if the point is valid
     * @param point the point considered
     * @return True boolean if the point is valid
     */
    
    public boolean isValidPoint(Point2D point) {
        // Return True if the point is valid
        return isValidPoint(point.getX(), point.getY());
    }
    /**
     * This method returns True if the coordinates of the point are valid
     * @param x the x coordinate
     * @param y the y coordinate
     * @return True boolean if the coordinates of the point are valid
     */

    // Allows to know if the coordinates of the point are valid within the limits of the grid
    public boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }
    
    /**
     * This method allows to eliminate the adjacent cells to a sunk ship that cannot contain a ship.
     * @param c cell
     */
    public void delCellNeighborShipSunk(Cellule c){
        if (c.getShip()!=null){
            if (!c.getShip().shipAlive()){
                List<Cellule> cellulesBateauCoule = c.getShip().getLcell();
                for( Cellule coule : cellulesBateauCoule){
                    if (!coule.getShip().shipAlive()){
                        Cellule[] tableauCellulesVoisines = getNeighbringCellsNonBlue(coule.getx(),coule.gety());
                        for(Cellule v : tableauCellulesVoisines){
                            if (!v.getAlreadyPlayed()){
                                v.setFill(Color.GREEN);
                                v.setAlreadyPlayed(true);
                        // We eliminae cells that cannot contain ships
                            }
                        }
                    }
                }
            }
        }
    }
    
}