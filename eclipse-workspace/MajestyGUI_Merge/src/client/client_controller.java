package client;

import client.client_model;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	}
	public void clickOnCard2() {
		model.takeCard(btncard2.getText());
	}
	public void clickOnCard3() {
		model.takeCard(btncard3.getText());
	}
	public void clickOnCard4() {
		model.takeCard(btncard4.getText());
	}
	public void clickOnCard5() {
		model.takeCard(btncard5.getText());
	}
	public void clickOnCard6() {
		model.takeCard(btncard6.getText());
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
		
		 String posID = String.valueOf(Value.charAt(0));
		 String cardname = Value.substring(1);
	
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		        // Update UI here.
		    	if(Integer.parseInt(posID) == 1){
					 btncard1.setText(cardname);				 
				 } else if(Integer.parseInt(posID) == 2){
					 btncard2.setText(cardname);
				 } else if(Integer.parseInt(posID) == 3){
					 btncard3.setText(cardname);
				 } else if(Integer.parseInt(posID) == 4){
					 btncard4.setText(cardname);
				 } else if(Integer.parseInt(posID) == 5){
					 btncard5.setText(cardname);
				 } else if(Integer.parseInt(posID) == 6){
					 btncard6.setText(cardname);
				 }
		    }
		
		});
	}
	

	public void clickOnSend() {
		model.sendMessage(txtChatMessage.getText());
	}
	
}
