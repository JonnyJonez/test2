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
	public Label lblPlayer1table;
	public Label lblPlayer2;
	public Label lblPlayer2table;
	public Label lblplayer1score;
	public Label lblplayer2score;
	
	public Label lblPlayer1muehle;
	public Label lblPlayer2muehle;
	public Label lblPlayer1brauerei;
	public Label lblPlayer2brauerei;
	public Label lblPlayer1hexenhaus;
	public Label lblPlayer2hexenhaus;
	public Label lblPlayer1wachturm;
	public Label lblPlayer2wachturm;
	public Label lblPlayer1kaserne;
	public Label lblPlayer2kaserne;	
	public Label lblPlayer1taverne;
	public Label lblPlayer2taverne;
	public Label lblPlayer1schloss;
	public Label lblPlayer2schloss;
	public Label lblPlayer1lazarett;
	public Label lblPlayer2lazarett;
	
	public TextArea txtChatArea;
	public TextArea txtChatMessage;
	
	public void clickOnConnect () {
		String ipAddress = txtIpAddress.getText();
		int port = Integer.parseInt(txtPort.getText());
		String name = txtName.getText();
		// lblPlayer1.setText(name);
		
		lblplayer1score.setText("0");
		lblplayer2score.setText("0");
		lblPlayer1muehle.setText("0");
		lblPlayer1brauerei.setText("0");
		lblPlayer1hexenhaus.setText("0");
		lblPlayer1wachturm.setText("0");
		lblPlayer1kaserne.setText("0");
		lblPlayer1taverne.setText("0");
		lblPlayer1schloss.setText("0");
		lblPlayer2muehle.setText("0");
		lblPlayer2brauerei.setText("0");
		lblPlayer2hexenhaus.setText("0");
		lblPlayer2wachturm.setText("0");
		lblPlayer2kaserne.setText("0");
		lblPlayer2taverne.setText("0");
		lblPlayer2schloss.setText("0");
		lblPlayer1lazarett.setText("0");
		lblPlayer2lazarett.setText("0");
		
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
		
		model.myCardTaken.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				increaseCardCount("my", newValue);
			}		
						
		} );
		
		model.otherCardTaken.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				increaseCardCount("other", newValue);
			}		
						
		} );
		
		model.myCardAction.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				myCardAction(newValue);
			}		
						
		} );
		
		model.otherCardAction.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				otherCardAction(newValue);
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
		 int posID = Integer.parseInt(String.valueOf(imagebutton.charAt(0)));
		 String Cardimage = imagebutton.substring(1);
//		 
//		 URL input = ClassLoader.getSystemResource("Brown.jpg");
//		 fx
//		 
//		 //Set image for Character cards
		
 		 Image Blue = new Image(getClass().getResourceAsStream("../character_cards/Blue.jpg")); // FOLDER BELOW NOT ACCESSABLE
		 
//		 Image Blue = new Image(getClass().getResourceAsStream("@../../pics/cards/character cards/Blue.jpg"));
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
		 Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			    	
//					
					if(posID == 1) {
						// btncard1.setGraphic(ViewBlue);
						btncard1.setGraphic(new ImageView(Blue)); }
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
			    }
		 });
		
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
					lblPlayer1table.setText(joiner);
				} else {
					lblPlayer2.setText(joiner);
					lblPlayer2table.setText(joiner);
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
	
	public void setNameInGUI(String name){
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		       txtName.setText(name);		    	
		    }
		});
	}
	
	public void myCardAction(String s){
		
		String[] parts = s.split("\\|");
				
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		       	if(parts[0].equals("attack")){
		       		model.mylazarett++;
		       		lblPlayer1lazarett.setText("" + model.mylazarett);
		       		decreaseCardCount("my", parts[1]);
		    	} else if (parts[0].equals("heal")) {
		    		model.mylazarett--;
		    		lblPlayer1lazarett.setText("" + model.mylazarett);
		    		increaseCardCount("my", parts[1]);
		    	}
		    }
		});
	}
	
	public void otherCardAction(String s){
		
		String[] parts = s.split("\\|");
				
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		       	if(parts[0].equals("attack")){
		       		model.otherlazarett++;
		       		lblPlayer2lazarett.setText("" + model.otherlazarett);
		       		decreaseCardCount("other", parts[1]);
		    	} else if (parts[0].equals("heal")) {
		    		model.otherlazarett--;
		    		lblPlayer2lazarett.setText("" + model.otherlazarett);
		    		increaseCardCount("other", parts[1]);
		    	}
		    }
		});
	}
	
	public void increaseCardCount(String type, String card){
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	
		    	if(type.equals("my")){
		    		
		    		if(card.equals("Muehle")){
			    		model.mymuehle++;
			    		lblPlayer1muehle.setText("" + model.mymuehle);
			    	} else if (card.equals("Brauerei")) {
			    		model.mybrauerei++;
			    		lblPlayer1brauerei.setText("" + model.mybrauerei);
			    	} else if (card.equals("Hexenhaus")) {
			    		model.myhexenhaus++;
			    		lblPlayer1hexenhaus.setText("" + model.myhexenhaus);
			    	} else if (card.equals("Wachturm")) {
			    		model.mywachturm++;
			    		lblPlayer1wachturm.setText("" + model.mywachturm);
			    	} else if (card.equals("Kaserne")) {
			    		model.mykaserne++;
			    		lblPlayer1kaserne.setText("" + model.mykaserne);
			    	} else if (card.equals("Taverne")) {
			    		model.mytaverne++;
			    		lblPlayer1taverne.setText("" + model.mytaverne);
			    	} else if (card.equals("Schloss")) {
			    		model.myschloss++;
			    		lblPlayer1schloss.setText("" + model.myschloss);
			    	} 

		    	} else {
		    		
		    		if(card.equals("Muehle")){
			    		model.othermuehle++;
			    		lblPlayer2muehle.setText("" + model.othermuehle);
		    		} else if (card.equals("Brauerei")) {
			    		model.otherbrauerei++;
			    		lblPlayer2brauerei.setText("" + model.otherbrauerei);
			    	} else if (card.equals("Hexenhaus")) {
			    		model.otherhexenhaus++;
			    		lblPlayer2hexenhaus.setText("" + model.otherhexenhaus);
			    	} else if (card.equals("Wachturm")) {
			    		model.otherwachturm++;
			    		lblPlayer2wachturm.setText("" + model.otherwachturm);
			    	} else if (card.equals("Kaserne")) {
			    		model.otherkaserne++;
			    		lblPlayer2kaserne.setText("" + model.otherkaserne);
			    	} else if (card.equals("Taverne")) {
			    		model.othertaverne++;
			    		lblPlayer2taverne.setText("" + model.othertaverne);
			    	} else if (card.equals("Schloss")) {
			    		model.otherschloss++;
			    		lblPlayer2schloss.setText("" + model.otherschloss);
			    	} 
				}
		         	
		    }
		});
	}
	
	public void decreaseCardCount(String type, String card){
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	
		    	if(type.equals("my")){
		    		
		    		if(card.equals("Muehle")){
			    		model.mymuehle--;
			    		lblPlayer1muehle.setText("" + model.mymuehle);
			    	} else if (card.equals("Brauerei")) {
			    		model.mybrauerei--;
			    		lblPlayer1brauerei.setText("" + model.mybrauerei);
			    	} else if (card.equals("Hexenhaus")) {
			    		model.myhexenhaus--;
			    		lblPlayer1hexenhaus.setText("" + model.myhexenhaus);
			    	} else if (card.equals("Wachturm")) {
			    		model.mywachturm--;
			    		lblPlayer1wachturm.setText("" + model.mywachturm);
			    	} else if (card.equals("Kaserne")) {
			    		model.mykaserne--;
			    		lblPlayer1kaserne.setText("" + model.mykaserne);
			    	} else if (card.equals("Taverne")) {
			    		model.mytaverne--;
			    		lblPlayer1taverne.setText("" + model.mytaverne);
			    	} else if (card.equals("Schloss")) {
			    		model.myschloss--;
			    		lblPlayer1schloss.setText("" + model.myschloss);
			    	} 

		    	} else {
		    		
		    		if(card.equals("Muehle")){
			    		model.othermuehle--;
			    		lblPlayer2muehle.setText("" + model.othermuehle);
		    		} else if (card.equals("Brauerei")) {
			    		model.otherbrauerei--;
			    		lblPlayer2brauerei.setText("" + model.otherbrauerei);
			    	} else if (card.equals("Hexenhaus")) {
			    		model.otherhexenhaus--;
			    		lblPlayer2hexenhaus.setText("" + model.otherhexenhaus);
			    	} else if (card.equals("Wachturm")) {
			    		model.otherwachturm--;
			    		lblPlayer2wachturm.setText("" + model.otherwachturm);
			    	} else if (card.equals("Kaserne")) {
			    		model.otherkaserne--;
			    		lblPlayer2kaserne.setText("" + model.otherkaserne);
			    	} else if (card.equals("Taverne")) {
			    		model.othertaverne--;
			    		lblPlayer2taverne.setText("" + model.othertaverne);
			    	} else if (card.equals("Schloss")) {
			    		model.otherschloss--;
			    		lblPlayer2schloss.setText("" + model.otherschloss);
			    	} 
				}
		         	
		    }
		});
	}
		
		

}
