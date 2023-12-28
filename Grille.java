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
    public VBox lignes = new VBox();
    // Bolean to know if it's the IA's grid or not
    public boolean IA = false;
    private int nbBateaux = 5;
    
    /**
     * The Grid constructor allows to create a grid.
     * It takes 2 parameters
     * @param IA a boolean that indicates if it's the IA's grid
     * @param clicSouris a mouse event
     */
    public Grille(boolean IA, EventHandler<? super MouseEvent> clicSouris){
        // We create the grid with cells
        this.IA= IA;
        for (int y=0 ; y<10 ; y++){
            HBox colonnesSurUneLigne = new HBox();
            for (int x=0 ; x<10 ; x++){
                // This refers to the grid mentioned
                Cellule c = new Cellule(x,y,this);
                // We add the mouse event
                c.setOnMouseClicked(clicSouris);
                colonnesSurUneLigne.getChildren().add(c);
            }
            lignes.getChildren().add(colonnesSurUneLigne);
        }
        
        getChildren().add(lignes);
    }
    /**
     * This getter allows to know if it's the IA's grid
     * @return IA a boolean 
     */
    public boolean getIA(){
        return IA;
    }

    /**
     * This setter defines if it's the IA's grid using a boolean
     * @param IA2 a boolean that indicates if it's the IA's grid
     */
    public void setIA(boolean IA2){
        IA=IA2;
    }

    /**
     * Ce getter permet de retourner le nb de bateaux de la grille 
     * This getter allows to know the number of ships in the grid
     * @return nbBateaux number of ships in the grid
     */
    
    public int getNbBateaux(){
        return nbBateaux;
    }

    /**
     * This setter defines the number of ships in the grid
     * @param nbBateaux2 the number of ships in the grid
     */
    public void setNbBateaux(int nbBateaux2){
        if (nbBateaux2>=0){
            nbBateaux=nbBateaux2;
        }
    }

    /**
     * Cette méthode renvoie true si le bateau est placé 
     * This method returns true if the ship is placed
     * @param bateau with his length and orientation
     * @param x the x coordinate
     * @param y the y coordinate
     * @return True if the ship is placed on the grid
     */
    
    public boolean placerBateau(Bateau bateau, int x, int y){
        if (peutPlacerBateau(bateau,x,y)){
            int longueur=bateau.getLongueurinit();
            bateau.setLcell(new ArrayList<>());
            
            if (bateau.getVertical()){
                for (int i=y; i<y+longueur; i++){
                    Cellule cellule = getCellule(x,i);
                    cellule.setBateau(bateau);
                    // We add the cell to the list of cells
                    bateau.getLcell().add(cellule);
                    if (!IA){
                        // Allows to only fill on the player's grid
                        cellule.setFill(Color.BLACK);
                        cellule.setStroke(Color.GREEN);
                    }
                }
            }
            else {
                for(int i=x; i<x+longueur; i++){
                    Cellule cellule = getCellule(i,y);
                    cellule.setBateau(bateau); 
                    // We add the cell to the list of cells
                    bateau.getLcell().add(cellule);
                    if (!IA){
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
    public Cellule getCellule(int x, int y){
        return (Cellule) ( (HBox) lignes.getChildren().get(y)).getChildren().get(x);
        
    }

    /**
     * This method returns an array of neighboring cells
     * @param x the x coordinate 
     * @param y the y coordinate
     * @return array of Cellule[]
     */
    public Cellule[] getCellulesVoisines(int x, int y){
        Point2D[] points = new Point2D[]{
            new Point2D(x-1, y),
            new Point2D(x+1 , y ),
            new Point2D(x, y-1),
            new Point2D(x, y+1)};
        List<Cellule> voisins = new ArrayList<Cellule> ();
        
        for (Point2D p : points){
            if (estPointValide(p)){
                voisins.add(getCellule((int)p.getX(), (int)p.getY()));
                
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
    public Cellule[] getCellulesVoisinesNonBleues(int x, int y){
        Point2D[] points = new Point2D[]{
            new Point2D(x-1, y),
            new Point2D(x+1 , y ),
            new Point2D(x, y-1),
            new Point2D(x, y+1)};
        List<Cellule> voisins = new ArrayList<Cellule> ();
        for (Point2D p : points){
            if (estPointValide(p) && ! (getCellule((int)p.getX(),(int)p.getY()).getFill().equals(Color.BLUE) )){
                voisins.add(getCellule((int)p.getX(), (int)p.getY()));
                
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
    private boolean peutPlacerBateau(Bateau bateau, int x, int y) {
        // Return True if we can place the ship at these coordinates
        int longueur = bateau.getLongueurinit();

        // If the ship is vertical
        if (bateau.getVertical()) {
            for (int i = y; i < y + longueur; i++) {
                if (!estPointValide(x, i))
                    return false;

                Cellule cellule = getCellule(x, i);
                if (cellule.getBateau() != null)
                    return false;

                for (Cellule voisin : getCellulesVoisines(x, i)) {
                    if (!estPointValide(x, i))
                        return false;

                    if (voisin.getBateau() != null)
                        return false;
                }
            }
        }
        // If the ship is horizontal
        else {
            for (int i = x; i < x + longueur; i++) {
                if (!estPointValide(i, y))
                    return false;

                Cellule cellule = getCellule(i, y);
                if (cellule.getBateau() != null)
                    return false;

                for (Cellule voisin : getCellulesVoisines(i, y)) {
                    if (!estPointValide(i, y))
                        return false;

                    if (voisin.getBateau() != null)
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
    
    public boolean estPointValide(Point2D point) {
        // Return True if the point is valid
        return estPointValide(point.getX(), point.getY());
    }
    /**
     * This method returns True if the coordinates of the point are valid
     * @param x the x coordinate
     * @param y the y coordinate
     * @return True boolean if the coordinates of the point are valid
     */

    // Allows to know if the coordinates of the point are valid within the limits of the grid
    public boolean estPointValide(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }
    
    /**
     * This method allows to eliminate the adjacent cells to a sunk ship that cannot contain a ship.
     * @param c cell
     */
    public void suppCellAdjacentesBateauCoule(Cellule c){
        if (c.getBateau()!=null){
            if (!c.getBateau().bateauEnVie()){
                List<Cellule> cellulesBateauCoule = c.getBateau().getLcell();
                for( Cellule coule : cellulesBateauCoule){
                    if (!coule.getBateau().bateauEnVie()){
                        Cellule[] tableauCellulesVoisines = getCellulesVoisinesNonBleues(coule.getx(),coule.gety());
                        for(Cellule v : tableauCellulesVoisines){
                            if (!v.getDejaJoue()){
                                v.setFill(Color.GREEN);
                                v.setDejaJoue(true);
                        // We eliminae cells that cannot contain ships
                            }
                        }
                    }
                }
            }
        }
    }
    
}