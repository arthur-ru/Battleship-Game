/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * La classe Grille permet de créer des grilles à partir de cellules 
 * @author Arthur Rubio
 */

public class Grille extends Parent {
    public VBox lignes = new VBox();
    public boolean IA = false;
    //booleen pour IA ou joueur 
    private int nbBateaux = 5;
    
    /**
     * Le constructeur de Grille permet de créer une grille. 
     * Elle prend 2 paramètres 
     * @param IA un boolean qui indique si c'est la grille de l'IA 
     * @param clicSouris un évènement de la souris 
     */
    public Grille(boolean IA, EventHandler<? super MouseEvent> clicSouris){
        this.IA= IA;
        //on crée la grille avec des cellules
        for (int y=0 ; y<10 ; y++){
            HBox colonnesSurUneLigne = new HBox();
            for (int x=0 ; x<10 ; x++){
                Cellule c = new Cellule(x,y,this);
                //le this se réfère à la grille évoquée
                c.setOnMouseClicked(clicSouris);
                //on attend ce qui est cliqué
                colonnesSurUneLigne.getChildren().add(c);
            }
            lignes.getChildren().add(colonnesSurUneLigne);
        }
        
        getChildren().add(lignes);
    }
    /**
     * Ce getter permet de savoir si c'est la grille de l'IA 
     * @return IA un boolean 
     */
    public boolean getIA(){
        return IA;
    }
    /**
     * Ce setter définit si c'est l'IA par un boolean 
     * @param IA2 un boolean indiquant si c'est l'IA 
     */
    public void setIA(boolean IA2){
        IA=IA2;
    }
    /**
     * Ce getter permet de retourner le nb de bateaux de la grille 
     * @return nbBateaux de la grille 
     */
    
    public int getNbBateaux(){
        return nbBateaux;
    }
    /**
     * Ce setter définit le nombre de bateaux de la grille 
     * @param nbBateaux2 le nombre de bateaux 
     */
    public void setNbBateaux(int nbBateaux2){
        if (nbBateaux2>=0){
            nbBateaux=nbBateaux2;
        }
    }
    /**
     * Cette méthode renvoie true si le bateau est placé 
     * @param bateau avec sa longueur et son orientation
     * @param x la coordo x 
     * @param y la coordo y 
     * @return true si bateau placé sur la grille 
     */ 
    
    public boolean placerBateau(Bateau bateau, int x, int y){
        if (peutPlacerBateau(bateau,x,y)){
            int longueur=bateau.getLongueurinit();
            bateau.setLcell(new ArrayList<>());
            
            if (bateau.getVertical()){
                for (int i=y; i<y+longueur; i++){
                    Cellule cellule = getCellule(x,i);
                    cellule.setBateau(bateau);
                    bateau.getLcell().add(cellule); //ajout cellule dans liste de cellules
                    if (!IA){
                        //permet de ne remplir que sur la grille du joueur 
                        cellule.setFill(Color.BLACK);
                        cellule.setStroke(Color.GREEN);
                    }
                }
            }
            else {
                for(int i=x; i<x+longueur; i++){
                    Cellule cellule = getCellule(i,y);
                    cellule.setBateau(bateau); 
                    bateau.getLcell().add(cellule); //ajout cellule dans liste de cellules
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
     * @param x la coordo x 
     * @param y la coordo y 
     * @return la cellule à ces coordos 
     */
    public Cellule getCellule(int x, int y){
        return (Cellule) ( (HBox) lignes.getChildren().get(y)).getChildren().get(x);
        
    }
    /**
     * Cette méthode renvoie un tableau de cellules voisines 
     * @param x coordo x 
     * @param y coordo y 
     * @return tableau de Cellule[] 
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
     * Cette méthode renvoie un tableau de cellules voisines non bleues
     * @param x coordo x 
     * @param y coordo y 
     * @return tableau de Cellule[] non bleues
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
     * @param bateau le bateau avec ses attributs de longueur et orientation 
     * @param x la coordo x 
     * @param y la coordo y 
     * @return true si on peut placer le bateau à ces coordos. 
     */
    private boolean peutPlacerBateau(Bateau bateau, int x, int y) {
        //retourne un boolean true si on peut placer un bateau
        int longueur = bateau.getLongueurinit();

        if (bateau.getVertical()) {
            //cas bateau vertical 
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
        else {
            //cas bateau horizontal 
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

        return true;
        //si on peut le placer 
    }
    
    /** 
     * Cette méthode renvoie true si le point est valide 
     * @param point le point considéré 
     * @return boolean true si le point est valide 
     */
    
    public boolean estPointValide(Point2D point) {
        //retourne true si le point est valide 
        return estPointValide(point.getX(), point.getY());
    }
    /**
     * Cette méthode renvoie true si les coordos du point sont valides
     * @param x la coordo x 
     * @param y la coordo y 
     * @return true si valide 
     */

    public boolean estPointValide(double x, double y) {
        //permet de dire si le point saisi est dans les limites de la grille 
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }
    
    /**
     * Cette méthode permet d'éliminer les cellules adjacentes   à un bateau coulé  ne pouvant contenir un bateau. 
     * @param c cellule 
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
                        //on élimine des cellules qui ne peuvent pas contenir de bateaux
                            }
                        }
                    }
                }
            }
        }
    }
    
}
    