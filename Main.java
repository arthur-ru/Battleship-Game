package bataillenavaleapo;


import java.util.Random;
import javax.swing.JOptionPane;
import java.util.Scanner;

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
     * @param primaryStage the main scene of the game
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Creation of the scene
        Scene scene = new Scene(creerContenu());
        // Title of the window
        primaryStage.setTitle("BattleShip");
        // Place of the window
        primaryStage.setScene(scene);
        // Full screen window
        primaryStage.setMaximized(true);
        // Resize window
        primaryStage.setResizable(true); 
        // Show window
        primaryStage.show();    
    }
    // Tells if the ships are already placed
    private boolean bateauxDejaPlaces = false;
    // Creates the grids of the players
    private Grille grilleIA, grilleJoueur;
    // Length of the ships to place on each grid
    public int[] tableauLgBat = {2,3,3,4,5};
    // The human player starts
    private boolean auTourDeIADeJouer = false;
    // Generates a random number
    private Random rd = new Random();
    public int compteurBateauxJoueurPlaces = 0;
    public int compteurBateauxIA = 0;
    public int compteurNbTour = 0;
    public Cellule derniereCelluleIA ;
    public int niveau = 1 ;  // Level of the game (1,2 or 3)

    
    /**
     * This method creates the content of the window.
     * The two grids are created, the player's ships are placed.
     * The answers to the mouse clicks on the cells are defined. 
     * The title and texts of the graphical interface are defined.
     * @return Parent 
     */
    private Parent creerContenu() {
        // Create the graphical content of the game window
        BorderPane fenetreContenu = new BorderPane();
        
        grilleIA = new Grille(true, clicSouris -> {
            // In this constructor, we define what to do on the enemy grid according to the event 
            if (!bateauxDejaPlaces) 
                // If the ships are not placed (boolean = false), we do nothing
                return;
            
            // Allows to obtain the source cell where the click originated from
            Cellule cellule = (Cellule) clicSouris.getSource();
            // If true it means that the cell has already been played with the shootOnCell() method
            if (cellule.getDejaJoue())
                return; // We do nothing
            
            // We then make a shootOnCell and look at the result: if True (we touched a ship) the human plays again, if False it is to the IA
            compteurNbTour++;
            System.out.println(" ");
            System.out.println("TURN "+compteurNbTour+ " OF THE GAME");
            System.out.println("*** Player turn *** : ");
            System.out.println("You fired on the lign "+ (cellule.gety()+1)+" and column "+ (cellule.getx()+1));
            cellule.tirerSurCellule();
            System.out.println("********************** ");
            if (niveau==2){
                // Help level 2
                grilleIA.suppCellAdjacentesBateauCoule(grilleIA.getCellule(cellule.getx() ,cellule.gety() ));
            }
            
            auTourDeIADeJouer = true;
            Text ttop1 = new Text ();
            ttop1.setText(" \nGAME IN PROGRESS");
            ttop1.setFont(Font.font ("Arial", 30));
            ttop1.setFill(Color.WHITE);
            Text ttop2 = new Text ();
            ttop2.setText("TURN "+compteurNbTour+ " OF THE GAME "+niveau+"\nRemaining Player ships : "+grilleJoueur.getNbBateaux()+"\nRemaining Bot ships : "+grilleIA.getNbBateaux() );
            ttop2.setFont(Font.font ("Arial", 20));
            ttop2.setFill(Color.WHITE);
            VBox toto = new VBox(40, ttop1, ttop2);
            fenetreContenu.setTop(toto);
            toto.setAlignment(Pos.CENTER);
            
            if (grilleIA.getNbBateaux() == 0) {
                // If no enemy ship remains we print won and exit directly
                System.out.println("The PLAYER WON in "+compteurNbTour+" turns");
                JOptionPane.showMessageDialog(null, "The PLAYER WON in "+compteurNbTour+" turns");
                Platform.exit();
            }
            
            if (auTourDeIADeJouer) {
                if (niveau==1){
                    methodeIANiveau1();
                    // Help level 1
                    grilleIA.suppCellAdjacentesBateauCoule(grilleIA.getCellule(cellule.getx(), cellule.gety()));
                }
                else{
                    methodeIA(); 
                }
                
                // If it is the turn of the enemy we do the method of the enemy turn
                // No help level 3 for the player
            }
            System.out.println("**********************");
            ttop2.setText("TURN "+compteurNbTour+ " OF GAME LEVEL "+niveau+"\nRemaining Player ships : "+grilleJoueur.getNbBateaux()+"\nRemaining Bot ships : "+grilleIA.getNbBateaux() );    
        });
        grilleIA.setNbBateaux(tableauLgBat.length); // Defines the right number of ships
        
        // We define the ships on player's grid
        grilleJoueur = new Grille(false, event -> {
            // If the ships are already placed we do nothing 
            if (bateauxDejaPlaces)
                return;
            
            // Tells which cell was clicked in case of click
            Cellule cellule = (Cellule) event.getSource();
            
            if (grilleJoueur.placerBateau(new Bateau(tableauLgBat[compteurBateauxJoueurPlaces], event.getButton() == MouseButton.PRIMARY), cellule.getx(), cellule.gety())) {
                compteurBateauxJoueurPlaces++;
                
                Text ttop1 = new Text ();
                ttop1.setText(" \nWelcome to the Battleship Board Game !");
                ttop1.setFont(Font.font ("Arial", 30));
                ttop1.setFill(Color.WHITE);
                Text ttop2 = new Text ();
                if (compteurBateauxJoueurPlaces<tableauLgBat.length) {
                ttop2.setText("You have to place a ship of length "+tableauLgBat[compteurBateauxJoueurPlaces]+"\nYou placed "+compteurBateauxJoueurPlaces+"/" +tableauLgBat.length+" ships on the right grid\nLeft click = Vertical placement\nRight clic = Horizontal placement\nYou can find information about ships placement on the Java console\nAfter placing your ships, the AI will proceed with its ship placement\nYou can then play by clicking on a cell in the left grid\n ");
                ttop2.setFont(Font.font ("Arial", 16));
                ttop2.setFill(Color.WHITE);}
                
                VBox vboxtoto = new VBox(20, ttop1, ttop2);
                fenetreContenu.setTop(vboxtoto);
                vboxtoto.setAlignment(Pos.CENTER);
                System.out.println("You placed "+compteurBateauxJoueurPlaces+" ships on a total of "+tableauLgBat.length+" ships to place");

                if(compteurBateauxJoueurPlaces== tableauLgBat.length){
                    System.out.println("You finished to place the ships, the AI will now place its own.");
                    placerBateauxIA();
                    System.out.println("Done, you can now play!\nThe gray cells are counter in the following way: \nThe ligns are from top to bottom from 1 to 10\nThe columns are from left to right from 1 to 10");
                    
                }
            }
        });
        grilleJoueur.setNbBateaux(tableauLgBat.length); // Allows to define the right number of ships

        // Create a vertical box with the 2 grids and 50 in interline 
        HBox hbox = new HBox(40, grilleIA, grilleJoueur);
        // Aligns the 2 grids in the center of the window
        hbox.setAlignment(Pos.CENTER);
        // Place the 2 grids in the center of the window
        fenetreContenu.setCenter(hbox);
        
        Text ttop1z = new Text ();
        ttop1z.setText(" \nWelcome to the Battleship Board Game !");
        ttop1z.setFont(Font.font ("Arial", 30));
        ttop1z.setFill(Color.WHITE);
        Text eeee = new Text ();
        eeee.setText("You have to place a ship of length "+tableauLgBat[compteurBateauxJoueurPlaces]+"\nYou placed "+compteurBateauxJoueurPlaces+"/" +tableauLgBat.length+" ships on the right grid\nLeft click = vertical placement\nRight click = horizontal placement\nYou can find informations about ships placement in the Java Console\nAfter placing your ships, the AI will proceed with its ship placement\nYou can then play by clicking on a cell in the left grid\n ");
        eeee.setFont(Font.font ("Arial", 16));
        eeee.setFill(Color.WHITE);
 
        Text tb2 = new Text ();
        tb2.setText("AI Game Grid                Player Game Grid\n ");
        tb2.setFont(Font.font ("Arial", 40));
        tb2.setFill(Color.WHITE);
        VBox totob = new VBox(20, tb2);
        fenetreContenu.setBottom(totob);
        totob.setAlignment(Pos.CENTER);
        VBox toto2 = new VBox(20, ttop1z, eeee);
        fenetreContenu.setTop(toto2);
        toto2.setAlignment(Pos.CENTER);
        
        fenetreContenu.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Game level to ask before launching the window
        Scanner sc = new Scanner(System.in);
        System.out.println(" \n \nWelcome to the Battleship Board Game!\nTo begin, you must choose your difficulty level:\nLevel 1: Very easy\nLevel 2 : Easy\nLevel 3 : Hard");
        System.out.println("Difficulty evel ? Answer 1,2 ou 3: ");
        try {String str = sc.nextLine();
	int niv = Integer.parseInt(str);	
        System.out.println("You chose the difficulty level: " + str);
        niveau=niv; }
        catch(Exception e1){
			System.out.println("Unknown input, the game will start at level 1");
		}
        
        // Return the content of the window
        return fenetreContenu;
    } 
    
    /**
     * This method defines the turn of the AI by choosing a non-played cell randomly.
     */
    
    private void methodeIANiveau1() {
        System.out.println("*** AI's turn *** :");
        while (auTourDeIADeJouer) {
            // Implement the case of replay to fasten the game
            // Choose a random cell in the player's grid
            int x = rd.nextInt(10);
            int y = rd.nextInt(10);

            Cellule cellule = grilleJoueur.getCellule(x, y);
            // If the cell has already been played, we choose another one
            if (cellule.getDejaJoue()) 
                continue;
            // The AI fires on the cell if it hasn't been played yet
            cellule.tirerSurCellule();
            // The enemy turn ends here
            auTourDeIADeJouer=false; 
            
            if (grilleJoueur.getNbBateaux() == 0) {
                System.out.println("The AI WON in "+compteurNbTour+" turns");
                JOptionPane.showMessageDialog(null, "The AI WON in "+compteurNbTour+" turns");
                Platform.exit();
            }
        }
    }
    
    /**
     * This method defines the strategy of the AI and proceeds to the shooting of a non-played cell according to certain characteristics of the current grid.
     */
    
    public void methodeIA(){
        System.out.println("*** AI's turn *** :");
        auTourDeIADeJouer = true;
        // The x are the columns
        // The y are the lines
        
        bouclecaserandom : // Search for a random non-played cell
        for (int p=0; p<10; p++){
            for (int q=0; q<10; q++){
                if (!(grilleJoueur.getCellule(p,q).getDejaJoue())){
                    derniereCelluleIA = grilleJoueur.getCellule(p, q);
                    // here derniereCelluleIA is a cell not played yet
                    break bouclecaserandom;
                }     
            }
        }
        
        // Create a loop to search for an orange cell
        bouclerechorange:
        for (int p=0; p<10; p++){
            for (int q=0; q<10; q++){
                if (grilleJoueur.getCellule(p,q).getFill().equals(Color.ORANGE)){
                    derniereCelluleIA = grilleJoueur.getCellule(p, q);
                    // Here derniereCelluleIA is a played and orange cell 
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
                System.out.println("Table of neighbouring cells created");
                
                bouclevoisineOrange:
                for(Cellule c : tableauCellulesVoisines){
                    if (c.getFill().equals(Color.ORANGE)){
                        CellVoisOrangeTrouvee = true;
                        orientationBateauEnCoursVertical = c.getx() == derniereCelluleIA.getx();
                        
                        // If the ship is vertical
                        if (orientationBateauEnCoursVertical){
                            System.out.println("Vertical ship spotted");

                            if (c.gety()== derniereCelluleIA.gety()+1){
                                int dy=derniereCelluleIA.gety();
                                int [] t = {dy+2, dy-1, dy+3, dy-2, dy+4};
                                for(int i=0 ;i<t.length ; i++){
                                    
                                    if (grilleJoueur.estPointValide(c.getx(), t[i] ) && !(grilleJoueur.getCellule(c.getx(), t[i]).getDejaJoue()) && (!grilleJoueur.getCellule(c.getx(), t[i]).getFill().equals(Color.BLUE))){
                                        System.out.println("Fire on a cell in lign "+(t[i]+1)+" and column "+(c.getx()+1)+" done");
                                        grilleJoueur.getCellule(c.getx(), t[i]).tirerSurCellule();
                                        grilleJoueur.suppCellAdjacentesBateauCoule(grilleJoueur.getCellule(c.getx(), t[i]));
                                        break bouclevoisineOrange  ;
                                    }
                                }
                            }

                            // If the neighbouring cell is below the lastCellIA
                            if (c.gety()== derniereCelluleIA.gety()-1){
                                int dy=derniereCelluleIA.gety();
                                int [] t = {dy-2, dy+1, dy-3, dy+2, dy-4};
                                for(int i=0 ;i<t.length ; i++){
                                    if (grilleJoueur.estPointValide(c.getx(), t[i] ) && !(grilleJoueur.getCellule(c.getx(), t[i]).getDejaJoue())){
                                        System.out.println("Fire on a cell in lign "+(t[i]+1)+" and column "+(c.getx()+1)+" done");
                                        
                                        grilleJoueur.getCellule(c.getx(), t[i]).tirerSurCellule();
                                        grilleJoueur.suppCellAdjacentesBateauCoule(grilleJoueur.getCellule(c.getx(), t[i]));
                                        break bouclevoisineOrange  ;
                                    }
                                }
                            }
                        }

                        if (!(orientationBateauEnCoursVertical)){
                            // If the ship is horizontal
                            System.out.println("Horizontal ship spotted");
                            
                            // If the neighbouring cell is above lastCellIA
                            if (c.getx()== derniereCelluleIA.getx()+1){
                                int dx=derniereCelluleIA.getx();
                                int [] t = {dx+2, dx-1, dx+3, dx-2, dx+4};
                                for(int i=0 ;i<t.length ; i++){
                                    if (grilleJoueur.estPointValide(t[i], c.gety() ) && !(grilleJoueur.getCellule( t[i], c.gety()).getDejaJoue())){
                                        System.out.println("Fire on a cell in lign "+(c.gety()+1)+" and column "+(t[i]+1)+" done");
                                        grilleJoueur.getCellule( t[i], c.gety()).tirerSurCellule();
                                        grilleJoueur.suppCellAdjacentesBateauCoule(grilleJoueur.getCellule( t[i], c.gety()));
                                        break bouclevoisineOrange ;
                                    }
                                }
                            }
                            
                            // If the neightbouring cell is under lastCellIA
                            if (c.getx()== derniereCelluleIA.getx()-1){
                                int dx=derniereCelluleIA.getx();
                                int [] t = {dx-2, dx+1, dx-3, dx+2, dx-4};
                                for(int i=0 ;i<t.length ; i++){
                                    if (grilleJoueur.estPointValide(t[i], c.gety() ) && !(grilleJoueur.getCellule( t[i], c.gety()).getDejaJoue())){
                                        System.out.println("Fire on a cell in lign "+(c.gety()+1)+" and column "+(t[i]+1)+" done");
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
                    // Return between 0 included and length excluded
                    Cellule celluleRdVoisine = tableauCellulesVoisines[i];
                    while (celluleRdVoisine.getDejaJoue()){
                        i = rd.nextInt(tableauCellulesVoisines.length);
                        celluleRdVoisine = tableauCellulesVoisines[i];  
                    }
                    System.out.println("Fire in lign "+(celluleRdVoisine.gety()+1)+" and column "+(celluleRdVoisine.getx()+1)+
                            " on a random neighbouring cell");
                    
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
                System.out.println("Fire on a random neighbouring cell in lign "+(y+1)+" and column "+(x+1)+" done ");
                cellule.tirerSurCellule();
                grilleJoueur.suppCellAdjacentesBateauCoule(grilleJoueur.getCellule(cellule.getx() ,cellule.gety() ));
                break boucleTOurIABlanc;
            }
        
        if (grilleJoueur.getNbBateaux() == 0) {
                System.out.println("The AI WON in "+compteurNbTour+" turns");
                JOptionPane.showMessageDialog(null, "The AI WON in "+compteurNbTour+" turns");
                Platform.exit();
                }
        
        auTourDeIADeJouer=false;
    }

    /**
     * This method defines the placement of the AI ships
     */
    public void placerBateauxIA(){
        while(compteurBateauxIA<tableauLgBat.length){
            int x = rd.nextInt(10);
            int y = rd.nextInt(10);
            if (grilleIA.placerBateau(new Bateau(tableauLgBat[compteurBateauxIA], Math.random() < 0.5), x, y)) {
                // Place a random ship on the enemy grid
                compteurBateauxIA++;
                System.out.println("L'IA a placé "+compteurBateauxIA+" bateaux sur un total de "+tableauLgBat.length);
            }
        }
        bateauxDejaPlaces = true;
    }
}