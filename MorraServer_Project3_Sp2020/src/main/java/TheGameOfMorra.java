import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.awt.*;
import java.util.HashMap;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;



public class TheGameOfMorra extends Application{


    // Initialize Variables
    TextField getPortNum;
    Button serverChoice;
    Button Start, choosePort, chooseClient;
    HashMap<String, Scene> sceneMap;
    Scene startScene;
    BorderPane startPane;
    Server serverConnection;
    Label gameMessage;
    Pane startStage;
    
    // ListView
    ListView<String> listItems;


    // Port Number variable
    int portNum;


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("This is the Server Gui");
        
        // Declare new Pane
        startStage = new Pane();
        
        //Initializing the port
        gameMessage = new Label("Enter Port Number, then hit Choose Port...");
        gameMessage.setTextFill(Color.WHITE);
        gameMessage.setFont(new Font("Arial", 30));
        gameMessage.relocate(200, 160);
        
        getPortNum = new TextField("");
        getPortNum.relocate(390, 210);
                
        
        
		// Button for Port
		choosePort = new Button("Choose Port");
		choosePort.relocate(420, 250);

        
        
        

        serverChoice = new Button("Start Server");
        serverChoice.relocate(420, 290);
        serverChoice.setDisable(true);


        //Setting the listView
        listItems = new ListView<String>();

        // Add to the List
        listItems.setCellFactory(lst ->
                new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) 
                    {
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

        // Button to add to the server
        this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
            serverConnection = new Server(
                    data -> {Platform.runLater(()->{listItems.getItems().add(data.toString());});

            }, portNum);
        });

        
        // Button to choose the port
        choosePort.setOnAction(new EventHandler<ActionEvent>(){ 
            public void handle(ActionEvent event){
            	serverChoice.setDisable(false);
                portNum = Integer.parseInt(getPortNum.getText());
                gameMessage.setText("Hit Start to begin the server...");
                gameMessage.relocate(270, 160);
                choosePort.setDisable(true);
            }
        });

        startStage.getChildren().addAll(choosePort);
        startStage.getChildren().addAll(gameMessage);
        startStage.getChildren().addAll(getPortNum);
        startStage.getChildren().addAll(serverChoice);
		//startStage.getChildren().addAll(getPortNum);

        //startScene = new Scene(startPane, 750, 560);

        // For the background
	    Image image = new Image("background.jpg");
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
		startStage.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,BackgroundPosition.CENTER,bSize)));

		// Create scene map
        sceneMap = new HashMap<String, Scene>();
        
        // Add to scene map
        sceneMap.put("server",  createServerGui());



       // Create new scene
        Scene scene = new Scene(startStage,880,495);
        
        // Show
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public Scene createServerGui() {

        // Create the BorderPane & edit background
        BorderPane pane = new BorderPane();
        
        Image image = new Image("background.jpg");
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
		pane.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,BackgroundPosition.CENTER,bSize)));
		
        
        // Add Padding
        pane.setPadding(new Insets(65));
        
        // Show the List
        pane.setCenter(listItems);
       
        return new Scene(pane, 600, 600);


    }

}
