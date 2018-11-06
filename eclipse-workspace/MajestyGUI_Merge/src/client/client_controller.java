package client;

import java.io.InputStream;
import java.net.URL;

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
	
	public Label lblPlayer1;
	public Label lblPlayer2;
	public Label lblplayer1score;
	public Label lblplayer2score;
	
	public TextArea txtChatArea;
	public TextArea txtChatMessage;
	
	public void clickOnConnect () {
		String ipAddress = txtIpAddress.getText();
		int port = Integer.parseInt(txtPort.getText());
		String name = txtName.getText();
		// lblPlayer1.setText(name);
		lblplayer1score.setText("0");
		lblplayer2score.setText("0");
		model.connect(ipAddress, port, name);
		txtIpAddress.setDisable(true);
		txtPort.setDisable(true);
		txtName.setDisable(true);		
		
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
		
		model.buttonImage.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				setButtonImage(newValue);
			}		
						
		} );
		
		model.newJoin.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				checkJoiner(newValue);
			}		
						
		} );
		
		model.myCoins.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				addSaldo("my", newValue);
			}		
						
		} );
		
		model.otherCoins.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				addSaldo("other", newValue);
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
	
	public void setButtonImage(String imagebutton) {
//		 int posID = Integer.parseInt(String.valueOf(imagebutton.charAt(0)));
//		 String Cardimage = imagebutton.substring(1);
//		 
//		 URL input = ClassLoader.getSystemResource("Brown.jpg");
//		 fx
//		 
//		 //Set image for Character cards
//		 Image Blue = new Image(getClass().getResourceAsStream("/pics/cards/character cards/Blue.jpg"));
//		 Image Brown = new Image(getClass().getResourceAsStream("/pics/cards/character cards/Brown.jpg"));
//		 Image Green = new Image(getClass().getResourceAsStream("/pics/cards/character cards/Green.jpg"));
//		 Image Orange = new Image(getClass().getResourceAsStream("/pics/cards/character cards/Orange.jpg"));
//		 Image Red = new Image(getClass().getResourceAsStream("/pics/cards/character cards/Red.jpg"));
//		 Image Violet = new Image(getClass().getResourceAsStream("/pics/cards/character cards/Violet.jpg"));
//		 Image Yellow = new Image(getClass().getResourceAsStream("/pics/cards/character cards/Yellow.jpg"));
//		 
//		 //Set Imageview for Images
//		 ImageView ViewBlue = new ImageView(Blue);
//		 ImageView ViewBrown = new ImageView(Brown);
//		 ImageView ViewGreen = new ImageView(Green);
//		 ImageView ViewOrange = new ImageView(Orange);
//		 ImageView ViewRed = new ImageView(Red);
//		 ImageView ViewViolet = new ImageView(Violet);
//		 ImageView ViewYellow= new ImageView(Yellow);
//			
//		 
//		 Platform.runLater(new Runnable() {
//			    @Override
//			    public void run() {
//			    	
//					
//					if(posID == 1) {
//						btncard1.setGraphic(ViewBlue);
//					} else if (posID == 2) {
//						btncard2.setGraphic(ViewBrown);
//					} else if (posID == 3) {
//						btncard3.setGraphic(ViewGreen);
//					} else if (posID == 4) {
//						btncard4.setGraphic(ViewOrange);
//					} else if (posID == 5) {
//						btncard5.setGraphic(ViewRed);
//					} else if (posID == 6) {
//						btncard6.setGraphic(ViewRed);
//					}
//			    }
//		 });
		
	}
	
	public void setButtonCardsText(String Value){
		
		String[] cards = Value.split(" ");
		
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
		    	
		    }
		});
	}
	

	public void clickOnSend() {
		model.sendMessage(txtChatMessage.getText());
	}
	
	public void checkJoiner(String joiner) {
		
		Platform.runLater(new Runnable() {
			
			  @Override
		    public void run() {
		        // Update UI here.
		    	if(joiner.equals(txtName.getText())){
					lblPlayer1.setText(joiner);
				} else {
					lblPlayer2.setText(joiner);
				}	
		    	
		    }
		});
	}
	
	public void addSaldo(String type, String saldo) {
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		        // Update UI here.
		    	if(type.equals("my")){
		    		lblplayer1score.setText(saldo);
		    	} else {
					lblplayer2score.setText(saldo);
				}	
		    	
		    }
		});
	}
	
		
		

}
