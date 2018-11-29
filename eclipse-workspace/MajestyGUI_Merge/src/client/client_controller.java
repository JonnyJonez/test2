package client;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

public class client_controller {
	private client_model model;
	
	// Initiate buttons, labels and text fields
	public Button btnConnect;
	public Button btnSend;
	public Button btnMute;
	
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
	
	public Label lblMeeple1;
	public Label lblMeeple2;
	public Label lblMeeple3;
	public Label lblMeeple4;
	public Label lblMeeple5;
	public Label lblMeeplePlayer1;
	public Label lblMeeplePlayer2;
	
	public Label lblWinner;
	
	public Ellipse ellWinner;
	
	public TextArea txtChatArea;
	public TextArea txtChatMessage;
	
	//	 Set image for Character cards
	 
	Image Wachturm = new Image(getClass().getResourceAsStream("../character_cards/Blue.jpg"));
	Image Brauerei = new Image(getClass().getResourceAsStream("../character_cards/Brown.jpg"));
	Image Hexenhaus = new Image(getClass().getResourceAsStream("../character_cards/Green.jpg"));
	Image Muehle = new Image(getClass().getResourceAsStream("../character_cards/Orange.jpg"));
	Image Kaserne = new Image(getClass().getResourceAsStream("../character_cards/Red.jpg"));
	Image Schloss = new Image(getClass().getResourceAsStream("../character_cards/Violet.jpg"));
	Image Taverne = new Image(getClass().getResourceAsStream("../character_cards/Yellow.jpg"));
	
	// Action on connect button
	
	public void clickOnConnect () {
		
		String ipAddress = txtIpAddress.getText();
		int port = Integer.parseInt(txtPort.getText());
		String name = txtName.getText();

		// set default text for labels
		
		
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
		
		// disable labels and buttons
		
		txtIpAddress.setDisable(true);
		txtPort.setDisable(true);
		txtName.setDisable(true);
		btnConnect.setDisable(false);
		
		
		
	}
	
	
	// Action for taking cards
	
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
		
		// Add listeners
		
		// Listener for log area		
		model.newestMessage.addListener( (o, oldValue, newValue) -> {
			if (!newValue.isEmpty()) 
				txtChatArea.appendText(newValue + "\n");
		} );
		
		// Listener for button visibility
		model.buttonsVis.addListener( (o, oldValue, newValue) -> {
			if(newValue.equals("false"))
				setButtonsInvisible();
			else 
				setButtonsVisible();	
						
		} );
		
		// Listener for button texts		
		model.buttonsText.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				setButtonCardsText(newValue);	
			}		
						
		} );
		
		// Listener for button images
		model.buttonImage.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				setButtonImage(newValue);
			}		
						
		} );
		
		// Listener for new joiner in the game
		model.newJoin.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				checkJoiner(newValue);
			}		
						
		} );
		
		// Listener for rewards concerning myself to update labels
		model.myCoins.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				addSaldo("my", newValue);
			}		
						
		} );
		
		// Listener for rewards concerning others to update labels
		model.otherCoins.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				addSaldo("other", newValue);
			}		
						
		} );
		
		// Listener for cards to update labels concerning myself
		model.myCardTaken.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				increaseCardCount("my", newValue);
			}		
						
		} );
		
		// Listener for cards to update labels concerning others
		model.otherCardTaken.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				increaseCardCount("other", newValue);
			}		
						
		} );
		
		// Listener for special actions (attack, heal) on my cards
		model.myCardAction.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				myCardAction(newValue);
			}		
						
		} );
		
		// Listener for special actions (attack, heal) on other cards
		model.otherCardAction.addListener( (o, oldValue, newValue) -> {
			if(!newValue.isEmpty()) {
				otherCardAction(newValue);
			}		
						
		} );
		
		// Listener for setting winner label & ellipse		
				model.winnerVis.addListener( (o, oldValue, newValue) -> {
					if(!newValue.isEmpty()) {
						settingWinner(newValue);	
					}						
				} );
		
	}
	
	// Set all card buttons to invisible
	
	public void setButtonsInvisible(){
			
			btncard1.setDisable(true);
			btncard2.setDisable(true);
			btncard3.setDisable(true);
			btncard4.setDisable(true);
			btncard5.setDisable(true);
			btncard6.setDisable(true);
		
	}
	
	// Set all card buttons to visible
	
	public void setButtonsVisible(){
		
		btncard1.setDisable(false);
		btncard2.setDisable(false);
		btncard3.setDisable(false);
		btncard4.setDisable(false);
		btncard5.setDisable(false);
		btncard6.setDisable(false);
		}
	
	// Set card images to the buttons
	
	public void setButtonImage(String imagebutton) {
		 int posID = Integer.parseInt(String.valueOf(imagebutton.charAt(0)));
		 String Cardimage = imagebutton.substring(1);
 
		 // Define Imageview of the Images
		 
		 ImageView ViewWachturm = new ImageView(Wachturm);
		 ViewWachturm.setFitWidth(70);
		 ViewWachturm.setFitHeight(140);
		 
		 ImageView ViewBrauerei = new ImageView(Brauerei);
		 ViewBrauerei.setFitWidth(70);
		 ViewBrauerei.setFitHeight(140);
		 
		 ImageView ViewHexenhaus = new ImageView(Hexenhaus);
		 ViewHexenhaus.setFitWidth(70);
		 ViewHexenhaus.setFitHeight(140);
		 
		 ImageView ViewMuehle = new ImageView(Muehle);
		 ViewMuehle.setFitWidth(70);
		 ViewMuehle.setFitHeight(140);
		 
		 ImageView ViewKaserne = new ImageView(Kaserne);
		 ViewKaserne.setFitWidth(70);
		 ViewKaserne.setFitHeight(140);
		 
		 ImageView ViewSchloss = new ImageView(Schloss);
		 ViewSchloss.setFitWidth(70);
		 ViewSchloss.setFitHeight(140);
		 
		 ImageView ViewTaverne = new ImageView(Taverne);
		 ViewTaverne.setFitWidth(70);
		 ViewTaverne.setFitHeight(140);


		 // Set Image to Button 
		 
		 Platform.runLater(new Runnable() {
			     public void run() {
				
					if(posID == 1) {
						switch (Cardimage) {
						case "Wachturm":
							btncard1.setGraphic(ViewWachturm);							
							break;
						case "Brauerei":
							btncard1.setGraphic(ViewBrauerei);
							break;
						case "Hexenhaus":
							btncard1.setGraphic(ViewHexenhaus);
							break;
						case "Muehle":
							btncard1.setGraphic(ViewMuehle);
							break;
						case "Kaserne":
							btncard1.setGraphic(ViewKaserne);
							break;
						case "Schloss":
							btncard1.setGraphic(ViewSchloss);
							break;
						case "Taverne":
							btncard1.setGraphic(ViewTaverne);
							break;
						default:
							break;
						}
					} else if(posID == 2) {
							switch (Cardimage) {
							case "Wachturm":
								btncard2.setGraphic(ViewWachturm);							
								break;
							case "Brauerei":
								btncard2.setGraphic(ViewBrauerei);
								break;
							case "Hexenhaus":
								btncard2.setGraphic(ViewHexenhaus);
								break;
							case "Muehle":
								btncard2.setGraphic(ViewMuehle);
								break;
							case "Kaserne":
								btncard2.setGraphic(ViewKaserne);
								break;
							case "Schloss":
								btncard2.setGraphic(ViewSchloss);
								break;
							case "Taverne":
								btncard2.setGraphic(ViewTaverne);
								break;
							default:
								break;
							} 
					} else if(posID == 3) {
							switch (Cardimage) {
							case "Wachturm":
								btncard3.setGraphic(ViewWachturm);							
								break;
							case "Brauerei":
								btncard3.setGraphic(ViewBrauerei);
								break;
							case "Hexenhaus":
								btncard3.setGraphic(ViewHexenhaus);
								break;
							case "Muehle":
								btncard3.setGraphic(ViewMuehle);
								break;
							case "Kaserne":
								btncard3.setGraphic(ViewKaserne);
								break;
							case "Schloss":
								btncard3.setGraphic(ViewSchloss);
								break;
							case "Taverne":
								btncard3.setGraphic(ViewTaverne);
								break;
							default:
								break;
								} 
					}else if(posID == 4) {
							switch (Cardimage) {
							case "Wachturm":
								btncard4.setGraphic(ViewWachturm);							
								break;
							case "Brauerei":
								btncard4.setGraphic(ViewBrauerei);
								break;
							case "Hexenhaus":
								btncard4.setGraphic(ViewHexenhaus);
								break;
							case "Muehle":
								btncard4.setGraphic(ViewMuehle);
								break;
							case "Kaserne":
								btncard4.setGraphic(ViewKaserne);
								break;
							case "Schloss":
								btncard4.setGraphic(ViewSchloss);
								break;
							case "Taverne":
								btncard4.setGraphic(ViewTaverne);
								break;
							default:
								break;
							}
					}else if(posID == 5) {
							switch (Cardimage) {
							case "Wachturm":
								btncard5.setGraphic(ViewWachturm);							
								break;
							case "Brauerei":
								btncard5.setGraphic(ViewBrauerei);
								break;
							case "Hexenhaus":
								btncard5.setGraphic(ViewHexenhaus);
								break;
							case "Muehle":
								btncard5.setGraphic(ViewMuehle);
								break;
							case "Kaserne":
								btncard5.setGraphic(ViewKaserne);
								break;
							case "Schloss":
								btncard5.setGraphic(ViewSchloss);
								break;
							case "Taverne":
								btncard5.setGraphic(ViewTaverne);
								break;
							default:
								break;
							}
					}else if(posID == 6) {
							switch (Cardimage) {
							case "Wachturm":
								btncard6.setGraphic(ViewWachturm);							
								break;
							case "Brauerei":
								btncard6.setGraphic(ViewBrauerei);
								break;
							case "Hexenhaus":
								btncard6.setGraphic(ViewHexenhaus);
								break;
							case "Muehle":
								btncard6.setGraphic(ViewMuehle);
								break;
							case "Kaserne":
								btncard6.setGraphic(ViewKaserne);
								break;
							case "Schloss":
								btncard6.setGraphic(ViewSchloss);
								break;
							case "Taverne":
								btncard6.setGraphic(ViewTaverne);
								break;
							default:
								break;
							}
					}
			    }
		 });
		
	}
	
	// Set text to buttons
	
	public void setButtonCardsText(String Value){
		
		String[] cards = Value.split(" ");
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	
		    	// Update button text
		    	
		    	btncard1.setText(cards[0]);
		    	btncard2.setText(cards[1]);
		    	btncard3.setText(cards[2]);
		    	btncard4.setText(cards[3]);
		    	btncard5.setText(cards[4]);
		    	btncard6.setText(cards[5]);	 	
		    	
		    }
		});
	}
	

	// Send Chat message
	
	public void clickOnSend() {
		model.sendMessage(txtChatMessage.getText());
	}
	
	
	// Set joiner name to labels
	
	public void checkJoiner(String joiner) {
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	
		    	// Check if the joiner is myself
		    	
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
	
	
	// Add a new saldo to the labels
	
	public void addSaldo(String type, String saldo) {
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	
		    	// Check for which player the update is

		    	if(type.equals("my")){
		    		lblplayer1score.setText(saldo);
		    	} else {
					lblplayer2score.setText(saldo);
				}	
		    	
		    }
		});
	}
	
	// Set a player name in the user interface
	
	public void setNameInGUI(String name){
		
		Platform.runLater(new Runnable() {
		    public void run() {
		       txtName.setText(name);		    	
		    }
		});
	}
	
	// React on actions concerning myself
	
	public void myCardAction(String s){
		
		String[] parts = s.split("\\|");
				
		Platform.runLater(new Runnable() {
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
	
	// React on actions concerning others
	
	public void otherCardAction(String s){
		
		String[] parts = s.split("\\|");
				
		Platform.runLater(new Runnable() {
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
	
	// Increase card counter and update labels
	
	public void increaseCardCount(String type, String card){
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	
		    	// Increase my card counter
		    	
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
		    		
		    	// Increase others card counter

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
	
	// Decrease card counter and update labels
	
	public void decreaseCardCount(String type, String card){
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	
		    	
		    	// Increase my card counter
		    	
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
		    		
		    	// Increase others card counter

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
	
	public void settingWinner(String winner) {
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	ellWinner.setVisible(true);
				lblWinner.setText(winner+"ist König von Westeros!");
				lblWinner.setVisible(true);		    	
		    }
		});
	}
	
		

}
