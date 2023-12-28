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
        Application.launch(args); // Launches the JavaFX application
    }
    
    /**
     * This method creates the window with its title and its scene 
     * @param primaryStage the main scene of the game
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Creation of the scene
        Scene scene = new Scene(createContent());
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
    private boolean shipsAlreadyPlaced = false;
    // Creates the grids of the players
    private Grille aiGrid, playerGrid;
    // Length of the ships to place on each grid
    public int[] arrayLenShip = {2,3,3,4,5};
    // The human player starts
    private boolean aiTurn = false;
    // Generates a random number
    private Random rd = new Random();
    public int counterPlayerShipPlaced = 0;
    public int counterAiShips = 0;
    public int counterTurns = 0;
    public Cellule lastAiCell ;
    public int level = 1 ;  // Level of the game (1,2 or 3)

    
    /**
     * This method creates the content of the window.
     * The two grids are created, the player's ships are placed.
     * The answers to the mouse clicks on the cells are defined. 
     * The title and texts of the graphical interface are defined.
     * @return Parent 
     */
    private Parent createContent() {
        // Create the graphical content of the game window
        BorderPane windowContent = new BorderPane();
        
        aiGrid = new Grille(true, mouseClick -> {
            // In this constructor, we define what to do on the enemy grid according to the event 
            if (!shipsAlreadyPlaced) 
                // If the ships are not placed (boolean = false), we do nothing
                return;
            
            // Allows to obtain the source cell where the click originated from
            Cellule cell = (Cellule) mouseClick.getSource();
            // If true it means that the cell has already been played with the shootOnCell() method
            if (cell.getAlreadyPlayed())
                return; // We do nothing
            
            // We then make a shootOnCell and look at the result: if True (we touched a ship) the human plays again, if False it is to the IA
            counterTurns++;
            System.out.println(" ");
            System.out.println("TURN "+counterTurns+ " OF THE GAME");
            System.out.println("*** Player turn *** : ");
            System.out.println("You fired on the lign "+ (cell.gety()+1)+" and column "+ (cell.getx()+1));
            cell.shootOnCell();
            System.out.println("********************** ");
            if (level==2){
                // Help level 2
                aiGrid.delCellNeighborShipSunk(aiGrid.getCell(cell.getx() ,cell.gety() ));
            }
            
            aiTurn = true;
            Text ttop1 = new Text ();
            ttop1.setText(" \nGAME IN PROGRESS");
            ttop1.setFont(Font.font ("Arial", 30));
            ttop1.setFill(Color.WHITE);
            Text ttop2 = new Text ();
            ttop2.setText("TURN "+counterTurns+ " OF THE GAME "+level+"\nRemaining Player ships : "+playerGrid.getNbShips()+"\nRemaining Bot ships : "+aiGrid.getNbShips() );
            ttop2.setFont(Font.font ("Arial", 20));
            ttop2.setFill(Color.WHITE);
            VBox toto = new VBox(40, ttop1, ttop2);
            windowContent.setTop(toto);
            toto.setAlignment(Pos.CENTER);
            
            if (aiGrid.getNbShips() == 0) {
                // If no enemy ship remains we print won and exit directly
                System.out.println("The PLAYER WON in "+counterTurns+" turns");
                JOptionPane.showMessageDialog(null, "The PLAYER WON in "+counterTurns+" turns");
                Platform.exit();
            }
            
            if (aiTurn) {
                if (level==1){
                    methodAiLevel1();
                    // Help level 1
                    aiGrid.delCellNeighborShipSunk(aiGrid.getCell(cell.getx(), cell.gety()));
                }
                else{
                    methodAi(); 
                }
                
                // If it is the turn of the enemy we do the method of the enemy turn
                // No help level 3 for the player
            }
            System.out.println("**********************");
            ttop2.setText("TURN "+counterTurns+ " OF GAME LEVEL "+level+"\nRemaining Player ships : "+playerGrid.getNbShips()+"\nRemaining Bot ships : "+aiGrid.getNbShips() );    
        });
        aiGrid.setNbShips(arrayLenShip.length); // Defines the right number of ships
        
        // We define the ships on player's grid
        playerGrid = new Grille(false, event -> {
            // If the ships are already placed we do nothing 
            if (shipsAlreadyPlaced)
                return;
            
            // Tells which cell was clicked in case of click
            Cellule cell = (Cellule) event.getSource();
            
            if (playerGrid.placeShips(new Bateau(arrayLenShip[counterPlayerShipPlaced], event.getButton() == MouseButton.PRIMARY), cell.getx(), cell.gety())) {
                counterPlayerShipPlaced++;
                
                Text ttop1 = new Text ();
                ttop1.setText(" \nWelcome to the Battleship Board Game !");
                ttop1.setFont(Font.font ("Arial", 30));
                ttop1.setFill(Color.WHITE);
                Text ttop2 = new Text ();
                if (counterPlayerShipPlaced<arrayLenShip.length) {
                ttop2.setText("You have to place a ship of length "+arrayLenShip[counterPlayerShipPlaced]+"\nYou placed "+counterPlayerShipPlaced+"/" +arrayLenShip.length+" ships on the right grid\nLeft click = Vertical placement\nRight clic = Horizontal placement\nYou can find information about ships placement on the Java console\nAfter placing your ships, the AI will proceed with its ship placement\nYou can then play by clicking on a cell in the left grid\n ");
                ttop2.setFont(Font.font ("Arial", 16));
                ttop2.setFill(Color.WHITE);}
                
                VBox vboxtoto = new VBox(20, ttop1, ttop2);
                windowContent.setTop(vboxtoto);
                vboxtoto.setAlignment(Pos.CENTER);
                System.out.println("You placed "+counterPlayerShipPlaced+" ships on a total of "+arrayLenShip.length+" ships to place");

                if(counterPlayerShipPlaced== arrayLenShip.length){
                    System.out.println("You finished to place the ships, the AI will now place its own.");
                    placeShipsAi();
                    System.out.println("Done, you can now play!\nThe gray cells are counter in the following way: \nThe ligns are from top to bottom from 1 to 10\nThe columns are from left to right from 1 to 10");
                    
                }
            }
        });
        playerGrid.setNbShips(arrayLenShip.length); // Allows to define the right number of ships

        // Create a vertical box with the 2 grids and 50 in interline 
        HBox hbox = new HBox(40, aiGrid, playerGrid);
        // Aligns the 2 grids in the center of the window
        hbox.setAlignment(Pos.CENTER);
        // Place the 2 grids in the center of the window
        windowContent.setCenter(hbox);
        
        Text ttop1z = new Text ();
        ttop1z.setText(" \nWelcome to the Battleship Board Game !");
        ttop1z.setFont(Font.font ("Arial", 30));
        ttop1z.setFill(Color.WHITE);
        Text eeee = new Text ();
        eeee.setText("You have to place a ship of length "+arrayLenShip[counterPlayerShipPlaced]+"\nYou placed "+counterPlayerShipPlaced+"/" +arrayLenShip.length+" ships on the right grid\nLeft click = vertical placement\nRight click = horizontal placement\nYou can find informations about ships placement in the Java Console\nAfter placing your ships, the AI will proceed with its ship placement\nYou can then play by clicking on a cell in the left grid\n ");
        eeee.setFont(Font.font ("Arial", 16));
        eeee.setFill(Color.WHITE);
 
        Text tb2 = new Text ();
        tb2.setText("AI Game Grid                Player Game Grid\n ");
        tb2.setFont(Font.font ("Arial", 40));
        tb2.setFill(Color.WHITE);
        VBox totob = new VBox(20, tb2);
        windowContent.setBottom(totob);
        totob.setAlignment(Pos.CENTER);
        VBox toto2 = new VBox(20, ttop1z, eeee);
        windowContent.setTop(toto2);
        toto2.setAlignment(Pos.CENTER);
        
        windowContent.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Game level to ask before launching the window
        Scanner sc = new Scanner(System.in);
        System.out.println(" \n \nWelcome to the Battleship Board Game!\nTo begin, you must choose your difficulty level:\nLevel 1: Very easy\nLevel 2 : Easy\nLevel 3 : Hard");
        System.out.println("Difficulty evel ? Answer 1,2 ou 3: ");
        try {String str = sc.nextLine();
	int niv = Integer.parseInt(str);	
        System.out.println("You chose the difficulty level: " + str);
        level=niv; }
        catch(Exception e1){
			System.out.println("Unknown input, the game will start at level 1");
		}
        
        // Return the content of the window
        return windowContent;
    } 
    
    /**
     * This method defines the turn of the AI by choosing a non-played cell randomly.
     */
    
    private void methodAiLevel1() {
        System.out.println("*** AI's turn *** :");
        while (aiTurn) {
            // Implement the case of replay to fasten the game
            // Choose a random cell in the player's grid
            int x = rd.nextInt(10);
            int y = rd.nextInt(10);

            Cellule cellule = playerGrid.getCell(x, y);
            // If the cell has already been played, we choose another one
            if (cellule.getAlreadyPlayed()) 
                continue;
            // The AI fires on the cell if it hasn't been played yet
            cellule.shootOnCell();
            // The enemy turn ends here
            aiTurn=false; 
            
            if (playerGrid.getNbShips() == 0) {
                System.out.println("The AI WON in "+counterTurns+" turns");
                JOptionPane.showMessageDialog(null, "The AI WON in "+counterTurns+" turns");
                Platform.exit();
            }
        }
    }
    
    /**
     * This method defines the strategy of the AI and proceeds to the shooting of a non-played cell according to certain characteristics of the current grid.
     */
    
    public void methodAi(){
        System.out.println("*** AI's turn *** :");
        aiTurn = true;
        // The x are the columns
        // The y are the lines
        
        bouclecaserandom : // Search for a random non-played cell
        for (int p=0; p<10; p++){
            for (int q=0; q<10; q++){
                if (!(playerGrid.getCell(p,q).getAlreadyPlayed())){
                    lastAiCell = playerGrid.getCell(p, q);
                    // here derniereCelluleIA is a cell not played yet
                    break bouclecaserandom;
                }     
            }
        }
        
        // Create a loop to search for an orange cell
        bouclerechorange:
        for (int p=0; p<10; p++){
            for (int q=0; q<10; q++){
                if (playerGrid.getCell(p,q).getFill().equals(Color.ORANGE)){
                    lastAiCell = playerGrid.getCell(p, q);
                    // Here derniereCelluleIA is a played and orange cell 
                    System.out.println("Orange cell found at column="+(p+1)+" lign="+ (q+1));
                    break bouclerechorange;
                }     
            }
        }
        
        boolean currentShipOrientationVertical;
        boolean cellNeighbrOrangeFound= false;
        Cellule[] arrayNeighbrCells;

        boucleTourIAcasOrange:
            if (lastAiCell.getFill().equals(Color.ORANGE)){
                arrayNeighbrCells = playerGrid.getNeighbringCellsNonBlue(lastAiCell.getx(),lastAiCell.gety());
                System.out.println("Table of neighbouring cells created");
                
                bouclevoisineOrange:
                for(Cellule c : arrayNeighbrCells){
                    if (c.getFill().equals(Color.ORANGE)){
                        cellNeighbrOrangeFound = true;
                        currentShipOrientationVertical = c.getx() == lastAiCell.getx();
                        
                        // If the ship is vertical
                        if (currentShipOrientationVertical){
                            System.out.println("Vertical ship spotted");

                            if (c.gety()== lastAiCell.gety()+1){
                                int dy=lastAiCell.gety();
                                int [] t = {dy+2, dy-1, dy+3, dy-2, dy+4};
                                for(int i=0 ;i<t.length ; i++){
                                    
                                    if (playerGrid.isValidPoint(c.getx(), t[i] ) && !(playerGrid.getCell(c.getx(), t[i]).getAlreadyPlayed()) && (!playerGrid.getCell(c.getx(), t[i]).getFill().equals(Color.BLUE))){
                                        System.out.println("Fire on a cell in lign "+(t[i]+1)+" and column "+(c.getx()+1)+" done");
                                        playerGrid.getCell(c.getx(), t[i]).shootOnCell();
                                        playerGrid.delCellNeighborShipSunk(playerGrid.getCell(c.getx(), t[i]));
                                        break bouclevoisineOrange  ;
                                    }
                                }
                            }

                            // If the neighbouring cell is below the lastCellIA
                            if (c.gety()== lastAiCell.gety()-1){
                                int dy=lastAiCell.gety();
                                int [] t = {dy-2, dy+1, dy-3, dy+2, dy-4};
                                for(int i=0 ;i<t.length ; i++){
                                    if (playerGrid.isValidPoint(c.getx(), t[i] ) && !(playerGrid.getCell(c.getx(), t[i]).getAlreadyPlayed())){
                                        System.out.println("Fire on a cell in lign "+(t[i]+1)+" and column "+(c.getx()+1)+" done");
                                        
                                        playerGrid.getCell(c.getx(), t[i]).shootOnCell();
                                        playerGrid.delCellNeighborShipSunk(playerGrid.getCell(c.getx(), t[i]));
                                        break bouclevoisineOrange  ;
                                    }
                                }
                            }
                        }

                        if (!(currentShipOrientationVertical)){
                            // If the ship is horizontal
                            System.out.println("Horizontal ship spotted");
                            
                            // If the neighbouring cell is above lastCellIA
                            if (c.getx()== lastAiCell.getx()+1){
                                int dx=lastAiCell.getx();
                                int [] t = {dx+2, dx-1, dx+3, dx-2, dx+4};
                                for(int i=0 ;i<t.length ; i++){
                                    if (playerGrid.isValidPoint(t[i], c.gety() ) && !(playerGrid.getCell( t[i], c.gety()).getAlreadyPlayed())){
                                        System.out.println("Fire on a cell in lign "+(c.gety()+1)+" and column "+(t[i]+1)+" done");
                                        playerGrid.getCell( t[i], c.gety()).shootOnCell();
                                        playerGrid.delCellNeighborShipSunk(playerGrid.getCell( t[i], c.gety()));
                                        break bouclevoisineOrange ;
                                    }
                                }
                            }
                            
                            // If the neightbouring cell is under lastCellIA
                            if (c.getx()== lastAiCell.getx()-1){
                                int dx=lastAiCell.getx();
                                int [] t = {dx-2, dx+1, dx-3, dx+2, dx-4};
                                for(int i=0 ;i<t.length ; i++){
                                    if (playerGrid.isValidPoint(t[i], c.gety() ) && !(playerGrid.getCell( t[i], c.gety()).getAlreadyPlayed())){
                                        System.out.println("Fire on a cell in lign "+(c.gety()+1)+" and column "+(t[i]+1)+" done");
                                        playerGrid.getCell( t[i], c.gety()).shootOnCell();
                                        playerGrid.delCellNeighborShipSunk(playerGrid.getCell( t[i], c.gety()));
                                        break bouclevoisineOrange ;
                                    }
                                }
                            } 
                        }
                    }
                }
                if (cellNeighbrOrangeFound==false){ 
                    int i = rd.nextInt(arrayNeighbrCells.length);
                    // Return between 0 included and length excluded
                    Cellule cellRedNeighbr = arrayNeighbrCells[i];
                    while (cellRedNeighbr.getAlreadyPlayed()){
                        i = rd.nextInt(arrayNeighbrCells.length);
                        cellRedNeighbr = arrayNeighbrCells[i];  
                    }
                    System.out.println("Fire in lign "+(cellRedNeighbr.gety()+1)+" and column "+(cellRedNeighbr.getx()+1)+
                            " on a random neighbouring cell");
                    
                    cellRedNeighbr.shootOnCell();
                    playerGrid.delCellNeighborShipSunk(playerGrid.getCell(cellRedNeighbr.getx() ,cellRedNeighbr.gety() ));
                    break boucleTourIAcasOrange;
                }
            }
        boucleTOurIABlanc:
            if (!(lastAiCell.getFill().equals(Color.ORANGE)) && !(lastAiCell.getFill().equals(Color.RED)) ){
                int x = rd.nextInt(10);
                int y = rd.nextInt(10);
                Cellule cellule = playerGrid.getCell(x, y);
                while(cellule.getAlreadyPlayed()){
                    x = rd.nextInt(10);
                    y = rd.nextInt(10);
                    cellule = playerGrid.getCell(x, y);
                }
                System.out.println("Fire on a random neighbouring cell in lign "+(y+1)+" and column "+(x+1)+" done ");
                cellule.shootOnCell();
                playerGrid.delCellNeighborShipSunk(playerGrid.getCell(cellule.getx() ,cellule.gety() ));
                break boucleTOurIABlanc;
            }
        
        if (playerGrid.getNbShips() == 0) {
                System.out.println("The AI WON in "+counterTurns+" turns");
                JOptionPane.showMessageDialog(null, "The AI WON in "+counterTurns+" turns");
                Platform.exit();
                }
        
        aiTurn=false;
    }

    /**
     * This method defines the placement of the AI ships
     */
    public void placeShipsAi(){
        while(counterAiShips<arrayLenShip.length){
            int x = rd.nextInt(10);
            int y = rd.nextInt(10);
            if (aiGrid.placeShips(new Bateau(arrayLenShip[counterAiShips], Math.random() < 0.5), x, y)) {
                // Place a random ship on the enemy grid
                counterAiShips++;
                System.out.println("The AI placed "+counterAiShips+" ships on a total of "+arrayLenShip.length);
            }
        }
        shipsAlreadyPlaced = true;
    }
}