package client;

import client.client_model;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class client_controller {
	private client_model model;
	
	public Button btnConnect;
	public Button btnSend;
	
	public Button btncard1;
	public Button btncard2;
	public Button btncard3;
	public Button btncard4;
	public Button btncard5;
	public Button btncard6;
	Button[] buttons = {btncard1, btncard2, btncard3, btncard4, btncard5, btncard6}; 
	
	public TextField txtIpAddress;
	public TextField txtPort;
	public TextField txtName;
	;
	
	public Label lblPlayer1;
	public Label lblPlayer2;
	
	public TextArea txtChatArea;
	public TextArea txtChatMessage;
	

	
	public void clickOnConnect () {
		String ipAddress = txtIpAddress.getText();
		int port = Integer.parseInt(txtPort.getText());
		String name = txtName.getText();
		lblPlayer1.setText(name);
		model.connect(ipAddress, port, name);
	}
	
	public void clickOnCard1() {
		model.takeCard(btncard1.getText());
		model.takenCard(1);
	}
	public void clickOnCard2() {
		model.takeCard(btncard2.getText());
		model.takenCard(2);
	}
	public void clickOnCard3() {
		model.takeCard(btncard3.getText());
		model.takenCard(3);
	}
	public void clickOnCard4() {
		model.takeCard(btncard4.getText());
		model.takenCard(4);
	}
	public void clickOnCard5() {
		model.takeCard(btncard5.getText());
		model.takenCard(5);
	}
	public void clickOnCard6() {
		model.takeCard(btncard6.getText());
		model.takenCard(6);
	}	
	
	
	public client_controller(client_model model) {
		this.model = model;
		
		model.newestMessage.addListener( (o, oldValue, newValue) -> {
			if (!newValue.isEmpty()) // Ignore empty messages
				txtChatArea.appendText(newValue + "\n");
		} );
		
		model.buttonsVis.addListener( (o, oldValue, newValue) -> {
			if(newValue.equals("false"))
				setButtonsInvisible();
			else 
				setButtonsVisible();	
						
		} );
		
		model.buttonsText.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				setButtonCardsText(newValue);	
			}		
						
		} );
	}
	
	
public void setButtonsInvisible(){
		
		btncard1.setDisable(true);
		btncard2.setDisable(true);
		btncard3.setDisable(true);
		btncard4.setDisable(true);
		btncard5.setDisable(true);
		btncard6.setDisable(true);
//		btncard7.setDisable(true);
		
	}
	
	public void setButtonsVisible(){
		
		btncard1.setDisable(false);
		btncard2.setDisable(false);
		btncard3.setDisable(false);
		btncard4.setDisable(false);
		btncard5.setDisable(false);
		btncard6.setDisable(false);
//		view.btncard7.setDisable(false);
		
	}
	
	public void setButtonCardsText(String Value){
		
		String[] cards = Value.split(" ");
		
		//Does not get the right image
//		Image Blue = new Image(getClass().getResourceAsStream("Blue.jpg"));
//		Image Brown = new Image(getClass().getResourceAsStream("Brown.jpg"));
//		Image Green = new Image(getClass().getResourceAsStream("Green.jpg"));
//		Image Orange = new Image(getClass().getResourceAsStream("Orange.jpg"));
//		Image Red = new Image(getClass().getResourceAsStream("Red.jpg"));
//		Image Violet = new Image(getClass().getResourceAsStream("Violet.jpg"));
//		Image Yellow = new Image(getClass().getResourceAsStream("Yellow.jpg"));
		
		
		
		
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		        // Update UI here.
		    	btncard1.setText(cards[0]);
		    	btncard2.setText(cards[1]);
		    	btncard3.setText(cards[2]);
		    	btncard4.setText(cards[3]);
		    	btncard5.setText(cards[4]);
		    	btncard6.setText(cards[5]);	    	
		    	
// Does not get image -> Exception
//		    	for(Button button :buttons) {
//					if(button.getText().equals("Muehle")) {
//						button.setGraphic(new ImageView(Orange));
//					} else if(button.getText().equals("Brauerei")){
//						button.setGraphic(new ImageView(Brown));
//					} else if(button.getText().equals("Hexenhaus")){
//						button.setGraphic(new ImageView(Green));
//					} else if(button.getText().equals("Wachturm")){
//						button.setGraphic(new ImageView(Blue));
//					} else if(button.getText().equals("Kaserne")){
//						button.setGraphic(new ImageView(Red));
//					} else if(button.getText().equals("Taverne")){
//						button.setGraphic(new ImageView(Yellow));
//					} else if(button.getText().equals("Schloss")){
//						button.setGraphic(new ImageView(Violet));
//					}
//				}
		    	
		    }
		
		});
	}
	

	public void clickOnSend() {
		model.sendMessage(txtChatMessage.getText());
	}
	
}
