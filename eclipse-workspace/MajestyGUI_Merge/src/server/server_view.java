package server;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class server_view {
	protected Stage stage;
	private server_model model;

	// Top controls
	Label lblPort = new Label("Port");
	TextField txtPort = new TextField();
	Region topSpacer = new Region();
	Button btnStart = new Button("Start");
	
	// Client area
	TextArea txtClientArea = new TextArea();
	
	public server_view(Stage stage, server_model model) {
		this.stage = stage;
		this.model = model;
		
		// Prevent labels and button from shrinking below their preferred size
		lblPort.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		btnStart.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		
		// Set sizes for top TextFields
		txtPort.setMinWidth(60); txtPort.setPrefWidth(60);
		
		HBox topBox = new HBox(lblPort, txtPort, topSpacer, btnStart);
		topBox.getStyleClass().add("hbox"); // Class for styling
		HBox.setHgrow(topSpacer,  Priority.ALWAYS);
		
		BorderPane root = new BorderPane();
		root.getStyleClass().add("root"); // Class for styling
		
		root.setTop(topBox);
		root.setCenter(txtClientArea);
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setTitle("Majesty Server");
	}
	
	protected void start() {
		stage.show();
		
		// Prevent resizing below initial size
		stage.setMinWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
		this.txtPort.setText("7777");
	}
	
	protected void updateClients() {
		StringBuffer sb = new StringBuffer();
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		e.printStackTrace();
		}
		
		for (player p : model.players) {
			sb.append(p.toString());
			sb.append("\n");
		}
		txtClientArea.setText(sb.toString());
	}
}