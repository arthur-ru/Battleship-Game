/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bataillenavaleapo;

import java.util.List;
import javafx.scene.Parent;

/**
 * La classe Bateau permet de définir des bateaux et de faire appel à des méthodes associées.
 * Elle permet de créer des objets de la classe Bateau et d'éxecuter les méthodes associées. 
 * 
 * @author Arthur Rubio
 */

public class Bateau extends Parent{
    
    /**
     * L'attribut longueurinit est la longueur initiale du bateau lors de sa création
     */
    private int longueurinit;
    /**
     * Le boolean vertical est vrai si le bateau est vertical, faux s'il est horizontal 
    */
    private boolean vertical = true;
    /**
     * L'attribut longueurrestante est la longueur restante du bateau comprise entre 0 et longueurinit
     * @see Bateau#diminuerLongueurBateau() 
     * @see Bateau#getLongueurrestante() 
    */
    private int longueurrestante;
    /**
     * La chaine de caractères nom est le nom du bateau 
     */
    private String nom;
    private List<Cellule> lcell; //liste de cellules
    
    /**
     * Ceci est le constructeur de Bateau. 
     * Il prend en paramètres : 
     * @param longueurinit description du paramètre longueurinit
     * @param vertical un boolean qui indique vertical ou horizontal 
     * 
     */
    public Bateau(int longueurinit, boolean vertical){
        
        //code a executer lors du constructeur
        setLongueurinit(longueurinit); //permet de passer par la vérification de setLongueurinit
        setVertical(vertical); 
        setLongueurrestante(longueurinit);
        nommerBateau(longueurinit);
        lcell=null; //liste de cellules vide
    }
    
    /**
     * Ce getter permet de retourner la longueur initiale
     * @return La longueur initiale
     */
    
    public int getLongueurinit(){
        return longueurinit;
    }
    
    /**
     * Ce setter permet d'affecter une valeur à la longueur initiale du bateau 
     * @param longueurinit2 
     */
    
    public void setLongueurinit(int longueurinit2){
        if (longueurinit2>=2 && longueurinit2<=5){
            //prend une longueur entre 2 et 5 
            longueurinit=longueurinit2;
        }
    }
    /**
     * Ce getter permet de retourner un boolean indiquant si le bateau est vertical
     * @return 
     */
    public boolean getVertical(){
        return vertical;
    }
    /**
     * Ce setter permet de définir si le bateau est vertical 
     * @param vertical2 un boolean indiquant si  le bateau est vertical 
     */
    public void setVertical(boolean vertical2){
        vertical=vertical2;
    }
    /**
     * Ce getter permet de renvoyer la longueur restante du bateau 
     * @return longueur restante du bateau 
     */
    public int getLongueurRestante(){
        return longueurrestante;
    }
    
    /** Ce setter permet de définir la longueur restante d'un bateau 
     * en vérifiant que la longueur est comprise entre 2 et 5. 
     * @param longueurrestante2 
     */
    
    public void setLongueurrestante(int longueurrestante2){
        if (longueurrestante2>=2 && longueurrestante2<=5){
            longueurrestante=longueurrestante2;
        }
    }
    /** 
     * Ce getter permet de renvoyer le nom du bateau 
     * @return nom , le nom du bateau 
     */
    
    public String getNom(){
        return nom;
    }
    /** Ce setter permet de définit le nom du bateau 
     * @param nom2 
     */
    
    public void setNom(String nom2){
        if (nom2!=null && nom2.length()>0){
            nom=nom2;
        }
    }
    /**
     * Ce getter renvoie une liste de cellules composant le bateau 
     * @return 
     */
    public List<Cellule> getLcell(){
        return lcell;
    }
    /**
     * Ce setter permet d'attribuer une liste de cellules à un bateau 
     * @param lcell2 la liste de cellules
     */
    
    public void setLcell(List<Cellule> lcell2){
        if (lcell2!=null){
            lcell=lcell2;
        }
    }
    /**
     * Cette méthode permet de décrémenter la longueur d'un Bateau
     * 
     */
    
    public void diminuerLongueurBateau(){
        longueurrestante--;
    }
    /** 
     * Cette méthode permet de vérifier si un bateau a une longueur restante non nulle 
     * @return true si le bateau a une longueur restante non nulle 
     */
    public boolean bateauEnVie(){
        return longueurrestante>0;
    }
    /**
     * Cette méthode permet d'attribuer une chaine nom à un bateau selon sa longueur i 
     * @param i la longueur du bateau 
     */
    
    public void nommerBateau(int i){ //pas besoin de default car pas possible que longueur soit différente de 2, 3, 4 ou 5 grâce au setter
        switch(i){
            case 2:
                setNom("torpilleur de 2 cases");
                break;
            case 3:
                setNom("contre-torpilleur de 3 cases");
                break;
            case 4:
                setNom("croiseur de 4 cases");
                break;
            case 5:
                setNom("porte-avions de 5 cases");
                break;
            default :
                setNom("bateau non conventionnel");
                break;
        }
    }
}

