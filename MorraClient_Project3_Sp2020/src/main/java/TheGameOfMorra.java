//import com.sun.corba.se.spi.activation.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;



public class TheGameOfMorra extends Application{


    // Initialzie Variables
    TextField ipTextField, portTextField;  
    Label labelForIp, labelForPort, labelForScore;
    Label typeInGuess;
    TextField guess;
    Button startGame,playAgain, Quit, sendPort, sendIP;
    HashMap<String, Scene> sceneMap;
    VBox designClient;
    Scene startScene;
    Client clientConnection;
    Button submitGuess;
    
    // Delcare the images
    Image one = new Image("1.png");
    Image two = new Image("2.png");
    Image three = new Image("3.png");
    Image four = new Image("4.png");
    Image five = new Image("5.png");
    ImageView oneView = new ImageView(one);
    ImageView twoView = new ImageView(two);
    ImageView threeView = new ImageView(three);
    ImageView fourView = new ImageView(four);
    ImageView fiveView = new ImageView(five);

    // Pane for the main screen
    Pane startStage;

    //list view
    ListView<String> listItems;

     int portNum;
     String ip;

     player p;


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub

        // Editing the Lables
        labelForPort = new Label("Enter Port #: ");
        labelForPort.setTextFill(Color.WHITE);
        labelForPort.setFont(new Font("Arial", 18));
        
        // Adding the text field
        portTextField = new TextField("");
        
        // Adding the button
        sendPort = new Button("Submit");

        //initializing logging in ip address
        labelForIp = new Label("Enter IP Address: ");
        labelForIp.setTextFill(Color.WHITE);
        labelForIp.setFont(new Font("Arial", 18));
        ipTextField = new TextField("");
        sendIP = new Button("Submit");

        // Notifying the Client
        primaryStage.setTitle("This is Client");


        // Start Game Button
        this.startGame = new Button("Start Game");

        // Send the IP address
        sendIP.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                ip = ipTextField.getText();
                startGame.setDisable(false);
                
            }
        });
        
        // Send the Port #
        sendPort.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                portNum = Integer.parseInt(portTextField.getText());
                sendIP.setDisable(false);
            }
        });

        // Start game Button
        this.startGame.setOnAction(e-> {primaryStage.setScene(sceneMap.get("client"));
            clientConnection = new Client(
                    data -> {Platform.runLater(()->{listItems.getItems().add(data.toString());});

                    }, portNum, ip);
            clientConnection.start();
        });


      
        // Relocations
        labelForPort.relocate(250, 200);
        labelForIp.relocate(210, 260);
        portTextField.relocate(360, 200);
        sendPort.relocate(410, 230);
        ipTextField.relocate(360, 260);
        sendIP.relocate(410, 290);
        startGame.relocate(400, 380);
        
        sendIP.setDisable(true);
        startGame.setDisable(true);
        
        
        
        
        //startGame.resize(400, 200);
        
        // Declare new Pane
        startStage = new Pane();
        startStage.getChildren().addAll(portTextField);
        startStage.getChildren().addAll(sendPort);
        startStage.getChildren().addAll(ipTextField);
        startStage.getChildren().addAll(sendIP);
        startStage.getChildren().addAll(startGame);
        startStage.getChildren().addAll(labelForPort);
        startStage.getChildren().addAll(labelForIp);
        
        startScene = new Scene(startStage, 880, 495);



        sceneMap = new HashMap<String, Scene>();
        
        // Create Client
        sceneMap.put("client",  createClientGui(primaryStage));

        // Background
        Image image = new Image("background.jpg");
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
		startStage.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,BackgroundPosition.CENTER,bSize)));



        primaryStage.setScene(startScene);
        primaryStage.show();

    }

    public Scene createClientGui(Stage primaryStage) throws IOException, ClassNotFoundException {

        // Create Buttons
    	Quit = new Button("Quit");
        playAgain = new Button("Play Again");
        typeInGuess = new Label("      Type guess first!: ");
        typeInGuess.setTextFill(Color.WHITE);
        typeInGuess.setFont(new Font("Arial", 16));
        Label pad = new Label("    ");
        submitGuess = new Button("Submit Guess");
        guess = new TextField();
        
        // HBox for Layout
        HBox temp = new HBox(playAgain, pad, Quit, typeInGuess, guess, submitGuess);

        // Disable the Images
        oneView.setDisable(true);
        twoView.setDisable(true);
        threeView.setDisable(true);
        fourView.setDisable(true);
        fiveView.setDisable(true);
        playAgain.setDisable(true);

        // Set up List View to add information
        listItems = new ListView<String>();
        listItems.setCellFactory(lst ->
                new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        
                        if(clientConnection.temp.equals("Please wait..."))
                        {
                            oneView.setDisable(true);
                            twoView.setDisable(true);
                            threeView.setDisable(true);
                            fourView.setDisable(true);
                            fiveView.setDisable(true);
                            oneView.setStyle("-fx-opacity: 0.50");
                            twoView.setStyle("-fx-opacity: 0.50");
                            threeView.setStyle("-fx-opacity: 0.50");
                            fourView.setStyle("-fx-opacity: 0.50");
                            fiveView.setStyle("-fx-opacity: 0.50");
                        }
                        if(clientConnection.temp.equals("It's your turn..."))
                        {
                            oneView.setStyle("-fx-opacity: 1.00");
                            twoView.setStyle("-fx-opacity: 1.00");
                            threeView.setStyle("-fx-opacity: 1.00");
                            fourView.setStyle("-fx-opacity: 1.00");
                            fiveView.setStyle("-fx-opacity: 1.00");
                            oneView.setDisable(false);
                            twoView.setDisable(false);
                            threeView.setDisable(false);
                            fourView.setDisable(false);
                            fiveView.setDisable(false);
                        }
                        
                        // Play again
                        if(clientConnection.temp.equals("Play again?"))
                        {
                            playAgain.setDisable(false);
                        }
                        super.updateItem(item, empty);
                        if (empty) {
                            setPrefHeight(45.0);
                            setText(null);
                        } else {
                            setPrefHeight(Region.USE_COMPUTED_SIZE);
                            setText(item);
                        }
                    }
                });



        // Add to HBox
        HBox numViews = new HBox(5, oneView, twoView, threeView, fourView, fiveView);
        
        // Change the opacity
        oneView.setStyle("-fx-opacity: 0.50");
        oneView.setDisable(true);
        twoView.setStyle("-fx-opacity: 0.50");
        twoView.setDisable(true);
        threeView.setStyle("-fx-opacity: 0.50");
        threeView.setDisable(true);
        fourView.setStyle("-fx-opacity: 0.50");
        fourView.setDisable(true);
        fiveView.setStyle("-fx-opacity: 0.50");
        fiveView.setDisable(true);
        
        // Default player
        p = new player();
        
        // Submit the guess
        submitGuess.setOnAction(e -> {
    		
    		p.setGuess(Integer.parseInt((guess.getText())));
    		oneView.setStyle("-fx-opacity: 1.00");
    		oneView.setDisable(false);
    		twoView.setStyle("-fx-opacity: 1.00");
    		twoView.setDisable(false);
    		threeView.setStyle("-fx-opacity: 1.00");
    		threeView.setDisable(false);
    		fourView.setStyle("-fx-opacity: 1.00");
    		fourView.setDisable(false);
    		fiveView.setStyle("-fx-opacity: 1.00");
    		fiveView.setDisable(false);


    });

        //sending data to the server
        oneView.setOnMouseClicked(e -> {
        		p.setTurn(1);
        		p.setNextPlay(1);
                clientConnection.send(p);
                
             // Reset P
                p = new player();
                oneView.setStyle("-fx-opacity: 0.50");
                oneView.setDisable(true);
                twoView.setStyle("-fx-opacity: 0.50");
                twoView.setDisable(true);
                threeView.setStyle("-fx-opacity: 0.50");
                threeView.setDisable(true);
                fourView.setStyle("-fx-opacity: 0.50");
                fourView.setDisable(true);
                fiveView.setStyle("-fx-opacity: 0.50");
                fiveView.setDisable(true);

        });
        twoView.setOnMouseClicked(e -> {
        		p.setTurn(2);
        		p.setNextPlay(1);
                clientConnection.send(p);
                
             // Reset P
                p = new player();
                oneView.setStyle("-fx-opacity: 0.50");
                oneView.setDisable(true);
                twoView.setStyle("-fx-opacity: 0.50");
                twoView.setDisable(true);
                threeView.setStyle("-fx-opacity: 0.50");
                threeView.setDisable(true);
                fourView.setStyle("-fx-opacity: 0.50");
                fourView.setDisable(true);
                fiveView.setStyle("-fx-opacity: 0.50");
                fiveView.setDisable(true);

        });
        threeView.setOnMouseClicked(e -> {
        	p.setTurn(3);
        	p.setNextPlay(1);
            clientConnection.send(p);
            
         // Reset P
            p = new player();
            oneView.setStyle("-fx-opacity: 0.50");
            oneView.setDisable(true);
            twoView.setStyle("-fx-opacity: 0.50");
            twoView.setDisable(true);
            threeView.setStyle("-fx-opacity: 0.50");
            threeView.setDisable(true);
            fourView.setStyle("-fx-opacity: 0.50");
            fourView.setDisable(true);
            fiveView.setStyle("-fx-opacity: 0.50");
            fiveView.setDisable(true);
        });
        fourView.setOnMouseClicked(e -> {
        	p.setTurn(4);
        	p.setNextPlay(1);
            clientConnection.send(p);
            
         // Reset P
            p = new player();
            oneView.setStyle("-fx-opacity: 0.50");
            oneView.setDisable(true);
            twoView.setStyle("-fx-opacity: 0.50");
            twoView.setDisable(true);
            threeView.setStyle("-fx-opacity: 0.50");
            threeView.setDisable(true);
            fourView.setStyle("-fx-opacity: 0.50");
            fourView.setDisable(true);
            fiveView.setStyle("-fx-opacity: 0.50");
            fiveView.setDisable(true);
        });
        fiveView.setOnMouseClicked(e -> {
        	p.setTurn(5);
        	p.setNextPlay(1);
            clientConnection.send(p);
            
         // Reset P
            p = new player();
            oneView.setStyle("-fx-opacity: 0.50");
            oneView.setDisable(true);
            twoView.setStyle("-fx-opacity: 0.50");
            twoView.setDisable(true);
            threeView.setStyle("-fx-opacity: 0.50");
            threeView.setDisable(true);
            fourView.setStyle("-fx-opacity: 0.50");
            fourView.setDisable(true);
            fiveView.setStyle("-fx-opacity: 0.50");
            fiveView.setDisable(true);
        });
        
        playAgain.setDisable(false);
        
        // Play Again Button
        playAgain.setOnAction(event -> {
        		p = new player();
        		p.setNextPlay(999);
                clientConnection.send(p);
        });

        // Quit button
        Quit.setOnAction(e -> {
        		
        		p.setNextPlay(0);
                clientConnection.send(p);
                primaryStage.close();


        });
        

        
        
        

        // Design
        designClient = new VBox(10, temp, numViews, listItems);
        Image image = new Image("background.jpg");
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
		designClient.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,BackgroundPosition.CENTER,bSize)));
        return new Scene(designClient);

    }



}
