/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bataillenavaleapo;


import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;
import java.util.Scanner;
import javafx.application.*;

/**
 * This class defines the battleship game and the JavaFX interface
 * @author Arthur Rubio
 */

public class Main extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Application.launch(args);
        
    }
    
    /**
     * This method creates the window with its title and its scene 
     * @param primaryStage la scene principale 
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(creerContenu());
        //crée la fenêtre
        primaryStage.setTitle("Bataille Navale - projet APO 2019-2020");
        //titre de la fenêtre
        primaryStage.setScene(scene);
        //place la fenêtre 
        primaryStage.setMaximized(true);
        //fenêtre en plein écran 
        primaryStage.setResizable(true);
        //redimensionner la fenêtre ? 
        primaryStage.show();
        //montrer la fenetre        
    }
    
    private boolean bateauxDejaPlaces = false;
    //dit si le jeu de l'iA est en cours 
    private Grille grilleIA, grilleJoueur;
    //cree les grilles des joueurs
    public int[] tableauLgBat = {2,3,3,4,5};
    //longueur des bateaux a placer sur chaque grille 
    private boolean auTourDeIADeJouer = false;
    // au debut le joueur commence 
    private Random rd = new Random();
    //generer un nb aleatoire 
    public int compteurBateauxJoueurPlaces = 0;
    public int compteurBateauxIA = 0;
    public int compteurNbTour = 0;
    public Cellule derniereCelluleIA ;
    public int niveau = 1 ;  //niveau de jeu 1,2,3

    
    /**
     * Cette méthode crée le contenu de la fenêtre.
     * Les 2 grilles sont créées, les bateaux du joueur sont placés. 
     * Les réponses à apporter aux clics de la souris sur les cellules sont définies.  
     * Les titres et textes de l'interface graphique sont définis. 
     * @return Parent 
     */
    private Parent creerContenu() {
        //creerlecontenu graphique du jeu 
        
        BorderPane fenetreContenu = new BorderPane();
        
        grilleIA = new Grille(true, clicSouris -> {
            //dans ce constructeur ennemi on définit les trucs a faire sur la grille ennemie en fonction de l'event 
            if (!bateauxDejaPlaces)
                //si bateauxdejaplaces est false on fait rien 
                return;

            Cellule cellule = (Cellule) clicSouris.getSource();
            //permet d'obtenir la cellule source du clic 
            if (cellule.getDejaJoue())
                //si true ca veut dire que la cellule a deja ete jouee avec la methode tirerSurCellule()
                return; //on fait rien 
            
            //on fait ensuite un tirerSurCellule et on regarde le resultat : si True ce n'est pas à l'iA, si False c'est à l'IA
            
            compteurNbTour++;
            System.out.println(" ");
            System.out.println("TOUR "+compteurNbTour+ " DU JEU ");
            System.out.println("*** Tour du Joueur *** : ");
            System.out.println("Vous avez tiré en ligne "+ (cellule.gety()+1)+" et en colonne "+ (cellule.getx()+1));
            cellule.tirerSurCellule();
            System.out.println("********************** ");
            if (niveau==2){
                grilleIA.suppCellAdjacentesBateauCoule(grilleIA.getCellule(cellule.getx() ,cellule.gety() ));
                //aide au niveau 2 
            }
            
            //JOptionPane.showMessageDialog(null, "Vous avez appuyé en colonne x ="+ cellule.x +" et ligne y="+ cellule.y);
            //default title and icon
            
            //on tire sur la cellule
            // a faire //auTourDeIADeJouer = !cellule.tirerSurCellule();
            auTourDeIADeJouer = true;
            Text ttop1 = new Text ();
            ttop1.setText(" \nPARTIE EN COURS");
            ttop1.setFont(Font.font ("Arial", 30));
            ttop1.setFill(Color.WHITE);
            Text ttop2 = new Text ();
            ttop2.setText("TOUR "+compteurNbTour+ " DU JEU NIVEAU "+niveau+"\nBateaux restants Joueur : "+grilleJoueur.getNbBateaux()+"\nBateaux restants IA : "+grilleIA.getNbBateaux() );
            ttop2.setFont(Font.font ("Arial", 20));
            ttop2.setFill(Color.WHITE);
            VBox toto = new VBox(40, ttop1, ttop2);
            fenetreContenu.setTop(toto);
            toto.setAlignment(Pos.CENTER);
            
            if (grilleIA.getNbBateaux() == 0) {
                //si aucun bateau ennemi ne reste on print gagné et on exit directement 
                System.out.println("Le JOUEUR a GAGNÉ en "+compteurNbTour+" tours");
                JOptionPane.showMessageDialog(null, "Le JOUEUR a GAGNÉ en "+compteurNbTour+" tours");
                //System.exit(0);
                Platform.exit();
            }
            
            if (auTourDeIADeJouer) {
                if (niveau==1){
                    methodeIANiveau1();
                    grilleIA.suppCellAdjacentesBateauCoule(grilleIA.getCellule(cellule.getx(), cellule.gety()));
                    //aide au niveau 1 
                    
                }
                else{
                    methodeIA(); 
                }
                
                //si c'est au tour de lennemi on fai la methode du  tour ennemi 
                
                //pas d'aide niveau 3 pour le joueur
            }
            System.out.println("**********************");
            ttop2.setText("TOUR "+compteurNbTour+ " DU JEU NIVEAU "+niveau+"\nBateaux restants Joueur : "+grilleJoueur.getNbBateaux()+"\nBateaux restants IA : "+grilleIA.getNbBateaux() );
            //MAJ
            
        });
        grilleIA.setNbBateaux(tableauLgBat.length); //permet de définir le bon nombre de bateaux 

        grilleJoueur = new Grille(false, event -> {
            //on definit les bateaux sur la grille joueur 
            if (bateauxDejaPlaces)
                //si les bateaux sont placés on ne touche plus à la grille du joueur affichee
                return;

            Cellule cellule = (Cellule) event.getSource();
            //permet de dire quelle cellule a ete cliquee en cas de clic 
            
            if (grilleJoueur.placerBateau(new Bateau(tableauLgBat[compteurBateauxJoueurPlaces], event.getButton() == MouseButton.PRIMARY), cellule.getx(), cellule.gety())) {
                compteurBateauxJoueurPlaces++;
                
                Text ttop1 = new Text ();
                ttop1.setText(" \nBienvenue au jeu de la Bataille Navale !");
                ttop1.setFont(Font.font ("Arial", 30));
                ttop1.setFill(Color.WHITE);
                Text ttop2 = new Text ();
                if (compteurBateauxJoueurPlaces<tableauLgBat.length) {
                ttop2.setText("Vous devez placer un bateau de longueur "+tableauLgBat[compteurBateauxJoueurPlaces]+"\nVous avez placé "+compteurBateauxJoueurPlaces+"/" +tableauLgBat.length+" bateaux sur la grille de droite\nClic gauche = placement vertical\nClic droit = placement horizontal\nDes informations sur les placements des bateaux sont disponibles dans la console Java\nUne fois vos bateaux placés, l'IA procédera à son placement de bateaux \nVous pouvez ensuite jouer en cliquant sur une cellule de la grille de gauche\n ");
                ttop2.setFont(Font.font ("Arial", 16));
                ttop2.setFill(Color.WHITE);}
                
                VBox vboxtoto = new VBox(20, ttop1, ttop2);
                fenetreContenu.setTop(vboxtoto);
                vboxtoto.setAlignment(Pos.CENTER);
                System.out.println("Vous avez placé "+compteurBateauxJoueurPlaces+" bateaux sur un total de "+tableauLgBat.length+" bateaux à placer");

                if(compteurBateauxJoueurPlaces== tableauLgBat.length){
                    System.out.println("Vous avez fini de placer les bateaux, l'IA va maintenant placer les siens");
                    placerBateauxIA();
                    System.out.println("Voilà, c'est fait, vous pouvez maintenant jouer\nLes cellules des grilles sont comptées de la manière suivante : \nLes lignes sont de haut en bas de 1 à 10\nLes colonnes sont de gauche à droite de 1 à 10");
                    
                }
            }
        });
        grilleJoueur.setNbBateaux(tableauLgBat.length); //permet de définir le bon nb de bateaux 

        HBox hbox = new HBox(40, grilleIA, grilleJoueur);
        //cree une vertical box avec les 2 grilels et 50 en interligne 
        hbox.setAlignment(Pos.CENTER);
        //aligne au centre 
        fenetreContenu.setCenter(hbox);
        //place les 2 grilles au centre de la fenetre 
        //root.setLeft(new Text("Grille de l'IA"));
        //root.setRight(new Text("Grille du joueur"));
        
        Text ttop1z = new Text ();
        ttop1z.setText(" \nBienvenue au jeu de la Bataille Navale !");
        ttop1z.setFont(Font.font ("Arial", 30));
        ttop1z.setFill(Color.WHITE);
        Text eeee = new Text ();
        eeee.setText("Vous devez placer un bateau de longueur "+tableauLgBat[compteurBateauxJoueurPlaces]+"\nVous avez placé "+compteurBateauxJoueurPlaces+"/" +tableauLgBat.length+" bateaux sur la grille de droite\nClic gauche = placement vertical\nClic droit = placement horizontal\nDes informations sur les placements des bateaux sont disponibles dans la console Java\nUne fois vos bateaux placés, l'IA procédera à son placement de bateaux \nVous pouvez ensuite jouer en cliquant sur une cellule de la grille de gauche\n ");
        eeee.setFont(Font.font ("Arial", 16));
        eeee.setFill(Color.WHITE);
 
        Text tb2 = new Text ();
        tb2.setText("Grille de jeu de l'IA                Grille de jeu du joueur\n ");
        tb2.setFont(Font.font ("Arial", 40));
        tb2.setFill(Color.WHITE);
        VBox totob = new VBox(20, tb2);
        fenetreContenu.setBottom(totob);
        totob.setAlignment(Pos.CENTER);
        VBox toto2 = new VBox(20, ttop1z, eeee);
        fenetreContenu.setTop(toto2);
        toto2.setAlignment(Pos.CENTER);
        
        fenetreContenu.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        //Niveau du jeu à demander avant de lancer la fenetre
        Scanner sc = new Scanner(System.in);
        System.out.println(" \n \nBienvenue à la Bataille Navale\nPour commencer, vous devez choisir votre niveau de difficulté :\nNiveau 1 : très facile\nNiveau 2 : facile\nNiveau 3 : difficile");
        System.out.println("Niveau ? Répondez 1,2 ou 3 : ");
        try {String str = sc.nextLine();
	int niv = Integer.parseInt(str);	
        System.out.println("Vous avez choisi le niveau : " + str);
        niveau=niv; }
        catch(Exception e1){
			System.out.println("Mauvaise saisie, vous entrez en mode très facile");
			//Traitement
		}
        
        
        return fenetreContenu;
        

        //retourne la fenetre
    } 
    
    /**
     * Cette méthode définit le tour de l'IA en choisissant une cellule non jouée de manière aléatoire .
     */
    
    private void methodeIANiveau1() {
        System.out.println("*** Tour de l'IA *** :");
        while (auTourDeIADeJouer) {
            //permet d'implémenter le cas de rejouer pour accelelrer le jeu 
            int x = rd.nextInt(10);
            int y = rd.nextInt(10);
            //choisit une case au hasard dans la grille joueur 

            Cellule cellule = grilleJoueur.getCellule(x, y);
            if (cellule.getDejaJoue()) 
                //si la case a deja ete jouee avant on choisit une autre case (on peut faire un while aussi ) 
                continue;
            //il est obligatoire davoir un while sinon il ny a pas dautre recherche de case valide non jouee
            cellule.tirerSurCellule();
            //JOptionPane.showMessageDialog(null, "L'ia a appuyé en colonne x ="+ cellule.x +" et ligne y="+ cellule.y);
            //l'IA tire sur la cellule au hasard
            auTourDeIADeJouer=false;
            
            //tour de lennemi se termine ici 
            
            if (grilleJoueur.getNbBateaux() == 0) {
                System.out.println("L'IA a GAGNÉ en "+compteurNbTour+" tours");
                JOptionPane.showMessageDialog(null, "L'IA a GAGNÉ en "+compteurNbTour+" tours");
                //System.exit(0);
                Platform.exit();
            }
        }
    }
    
    /**
     * Cette méthode définit la stratégie de l'IA et procède au tir d'une cellule non jouée selon certaines caractéristiques de la grille en cours. 
     */
    
    public void methodeIA(){
        System.out.println("*** Tour de l'IA *** :");
        auTourDeIADeJouer = true;
        //les x sont les colonnes 
        //les y sont les lignes 
        
        bouclecaserandom : // recherche d'une case random pas déjà jouée
        for (int p=0; p<10; p++){
            for (int q=0; q<10; q++){
                if (!(grilleJoueur.getCellule(p,q).getDejaJoue())){
                    derniereCelluleIA = grilleJoueur.getCellule(p, q);
                    //ici derniereCelluleIA est une cellule pas jouee 
                    break bouclecaserandom;
                }     
            }
        }
        
        //cree une boucle de recherche de case orange
        bouclerechorange:
        for (int p=0; p<10; p++){
            for (int q=0; q<10; q++){
                if (grilleJoueur.getCellule(p,q).getFill().equals(Color.ORANGE)){
                    derniereCelluleIA = grilleJoueur.getCellule(p, q);
                    //ici derniereCelluleIA est une cellule deja jouée et orange 
                    System.out.println("Cellule orange trouvée en colonne="+(p+1)+" et ligne="+ (q+1));
                    break bouclerechorange;
                }     
            }
        }
        
        boolean orientationBateauEnCoursVertical;
        boolean CellVoisOrangeTrouvee= false;
        Cellule[] tableauCellulesVoisines;

        boucleTourIAcasOrange:
            if (derniereCelluleIA.getFill().equals(Color.ORANGE)){
                tableauCellulesVoisines = grilleJoueur.getCellulesVoisinesNonBleues(derniereCelluleIA.getx(),derniereCelluleIA.gety());
                System.out.println("Tableau de cellules voisines créé");
                
                bouclevoisineOrange:
                for(Cellule c : tableauCellulesVoisines){
                    if (c.getFill().equals(Color.ORANGE)){
                        CellVoisOrangeTrouvee = true;
                        orientationBateauEnCoursVertical = c.getx() == derniereCelluleIA.getx();

                        if (orientationBateauEnCoursVertical){
                            System.out.println("Bateau vertical trouvé");
                            //si le bateau est vertical
                            
                            if (c.gety()== derniereCelluleIA.gety()+1){
                                int dy=derniereCelluleIA.gety();
                                int [] t = {dy+2, dy-1, dy+3, dy-2, dy+4};
                                for(int i=0 ;i<t.length ; i++){
                                    
                                    if (grilleJoueur.estPointValide(c.getx(), t[i] ) && !(grilleJoueur.getCellule(c.getx(), t[i]).getDejaJoue()) && (!grilleJoueur.getCellule(c.getx(), t[i]).getFill().equals(Color.BLUE))){
                                        System.out.println("Tir sur une cellule en ligne "+(t[i]+1)+" et colonne "+(c.getx()+1)+" effectué");
                                        grilleJoueur.getCellule(c.getx(), t[i]).tirerSurCellule();
                                        grilleJoueur.suppCellAdjacentesBateauCoule(grilleJoueur.getCellule(c.getx(), t[i]));
                                        break bouclevoisineOrange  ;
                                    }
                                }
                            }
                            
                            if (c.gety()== derniereCelluleIA.gety()-1){
                                //cas ou la cellule voisine est au dessous de derniereCelluleIA
                                int dy=derniereCelluleIA.gety();
                                int [] t = {dy-2, dy+1, dy-3, dy+2, dy-4};
                                for(int i=0 ;i<t.length ; i++){
                                    if (grilleJoueur.estPointValide(c.getx(), t[i] ) && !(grilleJoueur.getCellule(c.getx(), t[i]).getDejaJoue())){
                                        System.out.println("Tir sur une cellule en ligne "+(t[i]+1)+" et colonne "+(c.getx()+1)+" effectué");
                                        
                                        grilleJoueur.getCellule(c.getx(), t[i]).tirerSurCellule();
                                        grilleJoueur.suppCellAdjacentesBateauCoule(grilleJoueur.getCellule(c.getx(), t[i]));
                                        break bouclevoisineOrange  ;
                                    }
                                }
                            }
                        }

                        if (!(orientationBateauEnCoursVertical)){
                            System.out.println("Bateau horizontal trouvé");
                            //si le bateau est horizontal
                            
                            if (c.getx()== derniereCelluleIA.getx()+1){
                                //cas ou la cellule voisine est au dessous de derniereCelluleIA
                                int dx=derniereCelluleIA.getx();
                                int [] t = {dx+2, dx-1, dx+3, dx-2, dx+4};
                                for(int i=0 ;i<t.length ; i++){
                                    if (grilleJoueur.estPointValide(t[i], c.gety() ) && !(grilleJoueur.getCellule( t[i], c.gety()).getDejaJoue())){
                                        System.out.println("Tir sur une cellule en ligne "+(c.gety()+1)+" et colonne "+(t[i]+1)+" effectué");
                                        grilleJoueur.getCellule( t[i], c.gety()).tirerSurCellule();
                                        grilleJoueur.suppCellAdjacentesBateauCoule(grilleJoueur.getCellule( t[i], c.gety()));
                                        break bouclevoisineOrange ;
                                    }
                                }
                            }
                            
                            if (c.getx()== derniereCelluleIA.getx()-1){
                                //cas ou la cellule voisine est au dessous de derniereCelluleIA
                                int dx=derniereCelluleIA.getx();
                                int [] t = {dx-2, dx+1, dx-3, dx+2, dx-4};
                                for(int i=0 ;i<t.length ; i++){
                                    if (grilleJoueur.estPointValide(t[i], c.gety() ) && !(grilleJoueur.getCellule( t[i], c.gety()).getDejaJoue())){
                                        System.out.println("Tir sur une cellule en ligne "+(c.gety()+1)+" et colonne "+(t[i]+1)+" effectué");
                                        grilleJoueur.getCellule( t[i], c.gety()).tirerSurCellule();
                                        grilleJoueur.suppCellAdjacentesBateauCoule(grilleJoueur.getCellule( t[i], c.gety()));
                                        break bouclevoisineOrange ;
                                    }
                                }
                            } 
                        }
                    }
                }
                if (CellVoisOrangeTrouvee==false){ 
                    int i = rd.nextInt(tableauCellulesVoisines.length);
                    //retourne entre O inclus et length exclus 
                    Cellule celluleRdVoisine = tableauCellulesVoisines[i];
                    while (celluleRdVoisine.getDejaJoue()){
                        i = rd.nextInt(tableauCellulesVoisines.length);
                        celluleRdVoisine = tableauCellulesVoisines[i];  
                    }
                    System.out.println("Tir en ligne "+(celluleRdVoisine.gety()+1)+" et colonne "+(celluleRdVoisine.getx()+1)+
                            " sur une cellule voisine aléatoire");
                    
                    celluleRdVoisine.tirerSurCellule();
                    grilleJoueur.suppCellAdjacentesBateauCoule(grilleJoueur.getCellule(celluleRdVoisine.getx() ,celluleRdVoisine.gety() ));
                    break boucleTourIAcasOrange;
                }
            }
        boucleTOurIABlanc:
            if (!(derniereCelluleIA.getFill().equals(Color.ORANGE)) && !(derniereCelluleIA.getFill().equals(Color.RED)) ){
                int x = rd.nextInt(10);
                int y = rd.nextInt(10);
                Cellule cellule = grilleJoueur.getCellule(x, y);
                while(cellule.getDejaJoue()){
                    x = rd.nextInt(10);
                    y = rd.nextInt(10);
                    cellule = grilleJoueur.getCellule(x, y);
                }
                System.out.println("Tir sur une cellule aléatoire en ligne "+(y+1)+" et colonne "+(x+1)+" effectué ");
                cellule.tirerSurCellule();
                grilleJoueur.suppCellAdjacentesBateauCoule(grilleJoueur.getCellule(cellule.getx() ,cellule.gety() ));
                break boucleTOurIABlanc;
            }
        
       
        if (grilleJoueur.getNbBateaux() == 0) {
                System.out.println("L'IA a GAGNÉ en "+compteurNbTour+" tours");
                JOptionPane.showMessageDialog(null, "L'IA a GAGNÉ en "+compteurNbTour+" tours");
                //System.exit(0);
                Platform.exit();
                }
        
        auTourDeIADeJouer=false;
        
    }

    /**
     * Cette méthode définit le placement des bateaux de l'IA 
     */
    public void placerBateauxIA(){
        while(compteurBateauxIA<tableauLgBat.length){
            int x = rd.nextInt(10);
            int y = rd.nextInt(10);
            if (grilleIA.placerBateau(new Bateau(tableauLgBat[compteurBateauxIA], Math.random() < 0.5), x, y)) {
                //place un bateau random sur l'ennemi board 
                compteurBateauxIA++;
                System.out.println("L'IA a placé "+compteurBateauxIA+" bateaux sur un total de "+tableauLgBat.length);
            }
        }
        bateauxDejaPlaces = true;
    }
}

