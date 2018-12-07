package client;
	
import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * Credit: Prof. Dr. Bradley Richards
 */

public class client_main extends Application {

	public void start(Stage primaryStage) {
		try {
			client_model model = new client_model();
			client_controller controller = new client_controller(model);
			
			/**
			 * Implementing a FXML-File instead of a view class
			 * Design and FXML made by J.Arnold
			 * @author J.Arnold
			 */
			FXMLLoader loader = new FXMLLoader(getClass().getResource("gui_layout.fxml"));
			loader.setController(controller);
			AnchorPane root = loader.load();
					
			
			/**
			 * For a nicer game experience a background sound is implemented
			 * It starts by starting the client a volume of 20%
			 * @author J.Arnold
			 */
			 String backgroundSound = "src/sounds/background_sound.mp3";
			 Media musicfile = new Media (new File(backgroundSound).toURI().toString());
			 MediaPlayer mediaplayer = new MediaPlayer(musicfile);
			 mediaplayer.setAutoPlay(true);
			 mediaplayer.setVolume(0.2);
			 mediaplayer.play();
				
			 /**
			 * Setting the scene to a fixed size
			 * the two background pictures are implemented with the application.css
			 * @author J.Arnold
			 */
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
