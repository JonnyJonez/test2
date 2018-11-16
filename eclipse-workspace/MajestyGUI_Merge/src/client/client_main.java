package client;
	
import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


public class client_main extends Application {

	public void start(Stage primaryStage) {
		try {
			client_model model = new client_model();
			client_controller controller = new client_controller(model);
			
			// Laden des FXML (view)
			FXMLLoader loader = new FXMLLoader(getClass().getResource("gui_layout.fxml"));
			loader.setController(controller);
			AnchorPane root = loader.load();
					
			
			// Start sound when opening the game client
			
			 String backgroundSound = "src/sounds/background_sound.mp3";
			 Media musicfile = new Media (new File(backgroundSound).toURI().toString());
			 MediaPlayer mediaplayer = new MediaPlayer(musicfile);
			 mediaplayer.setAutoPlay(true);
			 mediaplayer.setVolume(0.2);
			 mediaplayer.play();
				
			
			Scene scene = new Scene(root,1100,700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
