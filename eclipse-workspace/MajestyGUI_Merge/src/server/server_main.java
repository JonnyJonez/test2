package server;


import server.server_controller;
import server.server_model;
import server.server_view;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Credit: Prof Dr. Bradley Richards
 */	

public class server_main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		server_model model = new server_model();
		server_view view = new server_view(primaryStage, model);
		server_controller controller = new server_controller(model, view);
		view.start();
	}

}
