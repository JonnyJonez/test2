package client;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Ellipse;

/**
 * Credit: Prof. Dr. Bradley Richards
 */

public class client_controller {
	private client_model model;
	
	/**
	 * Initiating game elements
	 * 
	 * @author J.Arnold
	 */
	public Button btnConnect;
	public Button btnSend;
	public Button btnMute;
	
	public Button btnCard1;
	public Button btnCard2;
	public Button btnCard3;
	public Button btnCard4;
	public Button btnCard5;
	public Button btnCard6;
	Button[] buttons = {btnCard1, btnCard2, btnCard3, btnCard4, btnCard5, btnCard6}; 
		
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
	
	public Label lblLoserScore;
	public Label lblWinnerScore;
	public Label lblLoser;	
	public Label lblWinner;
	
	public Ellipse ellWinner;
	
	public TextArea txtChatArea;
	public TextArea txtChatMessage;
	
	Image Wachturm = new Image(getClass().getResourceAsStream("../character_cards/Blue.jpg"));
	Image Brauerei = new Image(getClass().getResourceAsStream("../character_cards/Brown.jpg"));
	Image Hexenhaus = new Image(getClass().getResourceAsStream("../character_cards/Green.jpg"));
	Image Muehle = new Image(getClass().getResourceAsStream("../character_cards/Orange.jpg"));
	Image Kaserne = new Image(getClass().getResourceAsStream("../character_cards/Red.jpg"));
	Image Schloss = new Image(getClass().getResourceAsStream("../character_cards/Violet.jpg"));
	Image Taverne = new Image(getClass().getResourceAsStream("../character_cards/Yellow.jpg"));
	
	/**
	 * Action when clicking the ConnectButton
	 * Creates a connection with the server via the method connect in server_model
	 * 
	 * @author J.Arnold
	 */
public void clickOnConnect () {
		
		String ipAddress = txtIpAddress.getText();
		int port = Integer.parseInt(txtPort.getText());
		String name = txtName.getText();

		// set default text for labels
		
		Label [] labels = new Label[]{
				lblplayer1score,
				lblplayer2score,
				lblPlayer1muehle,
				lblPlayer1brauerei,
				lblPlayer1hexenhaus,
				lblPlayer1wachturm,
				lblPlayer1kaserne,
				lblPlayer1taverne,
				lblPlayer1schloss,
				lblPlayer2muehle, 
				lblPlayer2brauerei, 
				lblPlayer2hexenhaus, 
				lblPlayer2wachturm, 
				lblPlayer2kaserne, 
				lblPlayer2taverne, 
				lblPlayer2schloss, 
				lblPlayer1lazarett, 
				lblPlayer2lazarett
		};
		
		for (int i=0;i<labels.length;i++)
			labels[i].setText("0");
		
		model.connect(ipAddress, port, name);
		
		// disable labels and buttons
		
		txtIpAddress.setDisable(true);
		txtPort.setDisable(true);
		txtName.setDisable(true);
		btnConnect.setDisable(false);	
	}

	
	/** Action for taking cards by click
	 *  Opens the method takenCard in client_model
	 *  
	 * @author J.Arnold
	 */
	public void clickOnCard1() {
		model.takeCard(btnCard1.getText());
		model.takenCard(1);
	}	
	public void clickOnCard2() {
		model.takeCard(btnCard2.getText());
		model.takenCard(2);
	}
	public void clickOnCard3() {
		model.takeCard(btnCard3.getText());
		model.takenCard(3);
	}
	public void clickOnCard4() {
		model.takeCard(btnCard4.getText());
		model.takenCard(4);
	}
	public void clickOnCard5() {
		model.takeCard(btnCard5.getText());
		model.takenCard(5);
	}
	public void clickOnCard6() {
		model.takeCard(btnCard6.getText());
		model.takenCard(6);
	}	
	
	/** Constructor
	 * @author E.Thammavongsa
	 * @author P.Mächler
	 * @author J.Arnold
	 */
	public client_controller(client_model model) {
		this.model = model;
		
		
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
			if(newValue != null) {
				increaseCardCount("my", newValue);
			}		
						
		} );
		
		// Listener for cards to update labels concerning others
		model.otherCardTaken.addListener( (o, oldValue, newValue) -> {
			if(newValue != null) {
				increaseCardCount("other", newValue);
			}		
						
		} );
		
		// Listener for special actions (attack, heal) on my cards
		model.myCardAction.addListener( (o, oldValue, newValue) -> {
			if(newValue != null) {
				myCardAction(newValue);
			}		
						
		} );
		
		// Listener for special actions (attack, heal) on other cards
		model.otherCardAction.addListener( (o, oldValue, newValue) -> {
			if(newValue != null) {
				otherCardAction(newValue);
			}		
						
		} );
		
		// Listener for setting winner label & ellipse		
			model.winnerVis.addListener( (o, oldValue, newValue) -> {
				if(!newValue.isEmpty()) {
					settingWinner(newValue);	
				}						
			} );
			
		// Listener for setting winner label & ellipse		
			model.loserVis.addListener( (o, oldValue, newValue) -> {
				if(!newValue.isEmpty()) {
					settingLoser(newValue);	
				}						
			} );
		
		// Listener for setting winner label & ellipse		
			model.drawVis.addListener( (o, oldValue, newValue) -> {
				if(!newValue.isEmpty()) {
					settingDraw(newValue);	
				}						
			} );

	}
	
	/** Set all card buttons to invisible
	 * 
	 * @author J.Arnold
	 */
	
	public void setButtonsInvisible(){
		btnCard1.setDisable(true);
		btnCard2.setDisable(true);
		btnCard3.setDisable(true);
		btnCard4.setDisable(true);
		btnCard5.setDisable(true);
		btnCard6.setDisable(true);

	}
	
	/** Set all card buttons to visible
	 * 
	 * @author J.Arnold
	 */
	
	public void setButtonsVisible(){
		
		btnCard1.setDisable(false);
		btnCard2.setDisable(false);
		btnCard3.setDisable(false);
		btnCard4.setDisable(false);
		btnCard5.setDisable(false);
		btnCard6.setDisable(false);
		
	}
	
	/**
	 * Set card images to the buttons
	 * 
	 * @author E.Thammavongsa
	 */
	public void setButtonImage(String imagebutton) {
		//Get which position is being set range: 1-6
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
							btnCard1.setGraphic(ViewWachturm);							
							break;
						case "Brauerei":
							btnCard1.setGraphic(ViewBrauerei);
							break;
						case "Hexenhaus":
							btnCard1.setGraphic(ViewHexenhaus);
							break;
						case "Muehle":
							btnCard1.setGraphic(ViewMuehle);
							break;
						case "Kaserne":
							btnCard1.setGraphic(ViewKaserne);
							break;
						case "Schloss":
							btnCard1.setGraphic(ViewSchloss);
							break;
						case "Taverne":
							btnCard1.setGraphic(ViewTaverne);
							break;
						default:
							break;
						}
					} else if(posID == 2) {
							switch (Cardimage) {
							case "Wachturm":
								btnCard2.setGraphic(ViewWachturm);							
								break;
							case "Brauerei":
								btnCard2.setGraphic(ViewBrauerei);
								break;
							case "Hexenhaus":
								btnCard2.setGraphic(ViewHexenhaus);
								break;
							case "Muehle":
								btnCard2.setGraphic(ViewMuehle);
								break;
							case "Kaserne":
								btnCard2.setGraphic(ViewKaserne);
								break;
							case "Schloss":
								btnCard2.setGraphic(ViewSchloss);
								break;
							case "Taverne":
								btnCard2.setGraphic(ViewTaverne);
								break;
							default:
								break;
							} 
					} else if(posID == 3) {
							switch (Cardimage) {
							case "Wachturm":
								btnCard3.setGraphic(ViewWachturm);							
								break;
							case "Brauerei":
								btnCard3.setGraphic(ViewBrauerei);
								break;
							case "Hexenhaus":
								btnCard3.setGraphic(ViewHexenhaus);
								break;
							case "Muehle":
								btnCard3.setGraphic(ViewMuehle);
								break;
							case "Kaserne":
								btnCard3.setGraphic(ViewKaserne);
								break;
							case "Schloss":
								btnCard3.setGraphic(ViewSchloss);
								break;
							case "Taverne":
								btnCard3.setGraphic(ViewTaverne);
								break;
							default:
								break;
								} 
					}else if(posID == 4) {
							switch (Cardimage) {
							case "Wachturm":
								btnCard4.setGraphic(ViewWachturm);							
								break;
							case "Brauerei":
								btnCard4.setGraphic(ViewBrauerei);
								break;
							case "Hexenhaus":
								btnCard4.setGraphic(ViewHexenhaus);
								break;
							case "Muehle":
								btnCard4.setGraphic(ViewMuehle);
								break;
							case "Kaserne":
								btnCard4.setGraphic(ViewKaserne);
								break;
							case "Schloss":
								btnCard4.setGraphic(ViewSchloss);
								break;
							case "Taverne":
								btnCard4.setGraphic(ViewTaverne);
								break;
							default:
								break;
							}
					}else if(posID == 5) {
							switch (Cardimage) {
							case "Wachturm":
								btnCard5.setGraphic(ViewWachturm);							
								break;
							case "Brauerei":
								btnCard5.setGraphic(ViewBrauerei);
								break;
							case "Hexenhaus":
								btnCard5.setGraphic(ViewHexenhaus);
								break;
							case "Muehle":
								btnCard5.setGraphic(ViewMuehle);
								break;
							case "Kaserne":
								btnCard5.setGraphic(ViewKaserne);
								break;
							case "Schloss":
								btnCard5.setGraphic(ViewSchloss);
								break;
							case "Taverne":
								btnCard5.setGraphic(ViewTaverne);
								break;
							default:
								break;
							}
					}else if(posID == 6) {
							switch (Cardimage) {
							case "Wachturm":
								btnCard6.setGraphic(ViewWachturm);							
								break;
							case "Brauerei":
								btnCard6.setGraphic(ViewBrauerei);
								break;
							case "Hexenhaus":
								btnCard6.setGraphic(ViewHexenhaus);
								break;
							case "Muehle":
								btnCard6.setGraphic(ViewMuehle);
								break;
							case "Kaserne":
								btnCard6.setGraphic(ViewKaserne);
								break;
							case "Schloss":
								btnCard6.setGraphic(ViewSchloss);
								break;
							case "Taverne":
								btnCard6.setGraphic(ViewTaverne);
								break;
							default:
								break;
							}
					}
			    }
		 });
		
	}
	
	
	
	/**
	 * Set text to buttons
	 * 
	 * @author J.Arnold
	 */	
	public void setButtonCardsText(String Value){
		
		String[] cards = Value.split(" ");
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	    	
		    	btnCard1.setText(cards[0]);
		    	btnCard2.setText(cards[1]);
		    	btnCard3.setText(cards[2]);
		    	btnCard4.setText(cards[3]);
		    	btnCard5.setText(cards[4]);
		    	btnCard6.setText(cards[5]);
		    }
		});
	}
	

	/**
	 * Sends the written message by clicking on the button to the server via socket
	 * 
	 * @author J.Arnold
	 */
	public void clickOnSend() {
		model.sendMessage(txtChatMessage.getText());
		txtChatMessage.clear();
	}
		
	/**
	 * Sets the first joined and the second joined player
	 *
	 * 
	 * @author J.Arnold
	 */
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
	
	
	/**
	 * Add a new saldo to the labels
	 * 
	 * @author J.Arnold
	 */
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
	
	/**
	 * React on actions concerning myself
	 * 
	 * @author P.Mächler
	 */
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
	
	/**
	 * React on actions concerning others
	 * 
	 * @author P.Mächler
	 */
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
		    		increaseCardCount("other",parts[1]);
		    	}
		    }
		});
	}

	/**
	 * Increase card counter and update labels
	 * 
	 * @author J.Arnold
	 */
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
	
	/**
	 * To add the card back after the card has been healed
	 * @author E.Thammavongsa
	 */	
	
	public void checkHealerIncrease(String type, String card) {
		if(type.equals("my")){
    		
    		if(card.equals("Muehle")){		    			
	    		model.mymuehle++;
	    	} else if (card.equals("Brauerei")) {			    		
	    		model.mybrauerei++;
	    	} else if (card.equals("Hexenhaus")) {			    		
	    		model.myhexenhaus++;
	    	} else if (card.equals("Wachturm")) {			    		
	    		model.mywachturm++;
	    	} else if (card.equals("Kaserne")) {			    		
	    		model.mykaserne++;
	    	} else if (card.equals("Taverne")) {			    		
	    		model.mytaverne++;
	    	} else if (card.equals("Schloss")) {			    		
	    		model.myschloss++;
	    	} 
    		
    	// Increase others card counter

    	} else {
    		
    		if(card.equals("Muehle")){			    		
	    		model.othermuehle++;
    		} else if (card.equals("Brauerei")) {
	    		model.otherbrauerei++;
	    	} else if (card.equals("Hexenhaus")) {			    		
	    		model.otherhexenhaus++;
	    	} else if (card.equals("Wachturm")) {			    		
	    		model.otherwachturm++;
	    	} else if (card.equals("Kaserne")) {			    	
	    		model.otherkaserne++;
	    	} else if (card.equals("Taverne")) {			    		
	    		model.othertaverne++;
	    	} else if (card.equals("Schloss")) {			    		
	    		model.otherschloss++;
	    	} 
		}
	}
	
	/**
	 * Decrease card counter and update labels
	 * 
	 * @author J.Arnold
	 */
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
	
	/**
	 * Activate winner lables and sets the finish score
	 * 
	 * @author J.Arnold
	 */	
	public void settingWinner(String winner) {
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	ellWinner.setVisible(true);
		    	lblWinner.setVisible(true);
		    	lblWinnerScore.setVisible(true);
				lblWinner.setText("Gewinner: " + winner);
				if(lblPlayer1.getText().equals(winner)) {
					lblWinnerScore.setText(lblplayer1score.getText());
					
				} else {
					lblWinnerScore.setText(lblplayer2score.getText());
				}				
		    }
		});
	}
	
	/**
	 * Activate loser lables and sets the finish score
	 * 
	 * @author J.Arnold
	 */
	public void settingLoser(String loser) {
			
			Platform.runLater(new Runnable() {
			    public void run() {
			    	ellWinner.setVisible(true);
			    	lblLoser.setVisible(true);
			    	lblLoserScore.setVisible(true);
					lblLoser.setText("Verlierer: " + loser);
					if(lblPlayer1.getText().equals(loser)) {
						lblLoserScore.setText(lblplayer1score.getText());
					} else {
						lblLoserScore.setText(lblplayer2score.getText());
					}					
			    }
			});
		}
	/**
	 * Activate draw lables and sets the finish score
	 * 
	 * @author J.Arnold
	 */	
	public void settingDraw(String Draw) {
		
		String[] DrawPlayers = Draw.split("|");
		String Player1draw = DrawPlayers[0];
		String Player2draw = DrawPlayers[1];
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	ellWinner.setVisible(true);
		    	lblLoser.setVisible(true);
		    	lblWinner.setVisible(true);
		    	lblWinnerScore.setVisible(true);
		    	lblLoserScore.setVisible(true);
		    	
		    	if(lblPlayer1.getText().equals(Player1draw)) {
					lblLoserScore.setText(lblplayer1score.getText());
					lblLoser.setText("Unentschieden: " + Player1draw);
					lblWinnerScore.setText(lblplayer2score.getText());
					lblWinner.setText("Unentschieden: " + Player2draw);
				} else {
					lblLoserScore.setText(lblplayer2score.getText());
					lblLoser.setText("Unentschieden: " + Player1draw);
					lblWinnerScore.setText(lblplayer1score.getText());
					lblWinner.setText("Unentschieden: " + Player2draw);
				}
		    	
		    }
		});
	}
	
		

}
