package server;

import javafx.collections.ListChangeListener;

/**
 * Credit: Prof Dr. Bradley Richards
 */	

public class server_controller {
	private server_model model;
	private server_view view;

	public server_controller(server_model model, server_view view) {
		this.model = model;
		this.view = view;

		view.btnStart.setOnAction(event -> {
			view.btnStart.setDisable(true);
			int port = Integer.parseInt(view.txtPort.getText());
			model.startServer(port);
		});

		view.stage.setOnCloseRequest(event -> model.stopServer());

		model.players.addListener((ListChangeListener) (event -> view.updateClients()));
	}
}


