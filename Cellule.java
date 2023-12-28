/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bataillenavaleapo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Cette classe Cellule permet de créer des objets Cellule et d'executer les méthodes associées
 * Une cellule est le composant de base d'une grille 
 * @author Arthur Rubio
 */

public class Cellule extends Rectangle{
    public int x,y; //les coordos sont publiques pour y accéder dans main 
    private Bateau bateau; // le bateau 
    private boolean dejaJoue=false; //un boolean pour savoir si on a deja joué cette cellule 
    private Grille grille; //la grille à laquelle appartient la cellule
    
    /**
     * Le constructeur de Cellule crée une Cellule avec 3 paramètres. 
     * @param x la coordo x 
     * @param y la coordo y 
     * @param grille la grille sur laquelle sera placée la cellule
     */
    
    public Cellule(int x, int y, Grille grille){
        super(40,40);
        setx(x);
        sety(y);
        setGrille(grille);
        setFill(Color.WHITESMOKE); //couleur de base Blanc
        setStroke(Color.BLACK);
    }
    
    /** Ce getter renvoie la coordo x 
     * 
     * @return x la coordo x 
     */
    
    public int getx(){ //pas getX car existe déjà dans Rectangle
        return x;
    }
    /**
     * Ce setter definit la coordo x en vérifiant sa validité 
     * @param x2 
     */
    public void setx(int x2){
        if (x2>=0 && x2<10){
            x=x2;
        }
    }
    /** Ce getter renvoie la coordo y 
     * 
     * @return y la coordo
     */
    public int gety(){
        return y;
    }
    /**
     * Ce setter definit la coordo y en vérifiant sa validité 
     * @param y2 
     */
    public void sety(int y2){
        if (y2>=0 && y2<10){
            y=y2;
        }
    }
    /**
     * Ce getter retourne le Bateau de la cellule 
     * @return 
     */
    public Bateau getBateau(){
        return bateau;
    }
    /**
     * Ce setter permet d'attribuer un bateau à une cellule 
     * @param bateau2 
     */
    public void setBateau(Bateau bateau2){
        if (bateau2!=null){
            bateau=bateau2;
        }
    }
    /** 
     * Ce getter renvoie true si la cellule a déjà été jouée 
     * @return dejaJoue un boolean
     */
    
    public boolean getDejaJoue(){
        return dejaJoue;
    }
    /**
     * Ce setter permet de définir par un boolean si la cellule a déjà été jouée
     * @param dejaJoue2 
     */
    
    public void setDejaJoue(boolean dejaJoue2){
        dejaJoue=dejaJoue2;
    }
    /**
     * Ce gettter retourne la grille 
     * @return la grille 
     */
    public Grille getGrille(){
        return grille;
    }
    
    /**
     * Ce setter permet de définir une grille à la cellule
     * @param grille2 
     */    
    public void setGrille(Grille grille2){
        if (grille2!=null){
            grille=grille2;
        }
    }
    /**
     * Cette méthode permet de tirer sur une cellule.
     * Elle regarde s'il y a un bateau sur la cellule.
     * Colore la cellule en bleu si non.
     * Colore la cellule en orange si oui.
     * Colore toutes les cellules du bateau en rouge si le bateau a été coulé par le tir.
     * @return un boolean true si un bateau a été touché ou coulé 
     */
    public boolean tirerSurCellule(){
        if (getDejaJoue())
                //si true ca veut dire que la cellule a deja ete jouee avec la methode shoot()
                return false;
        dejaJoue = true;
        setFill(Color.BLUE);
        //System.out.println("Voyons voir ce qui a été touché");
        if (bateau == null){
            System.out.println("--- Dommage, à l'eau !");
        }
        if (bateau !=null){
            bateau.diminuerLongueurBateau();
            setFill(Color.ORANGE);
            System.out.println("--- Bateau touché");
            
            if (!bateau.bateauEnVie()){
                grille.setNbBateaux(grille.getNbBateaux()-1);
                for (Cellule cellule:bateau.getLcell()){
                    cellule.setFill(Color.RED);
                }
                
                System.out.println("--- Un "+bateau.getNom() +" a été coulé");
                System.out.println("Il reste "+grille.getNbBateaux()+" bateaux à couler sur la grille");
            }
            return true;
           
        }
        
        return false;
    }
}

