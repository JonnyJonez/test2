package client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import commons.CardStackMsg;
import commons.CardTakenMsg;
import commons.ChatMsg;
import commons.DrawMsg;
import commons.JoinMsg;
import commons.Message;
import commons.RewardMsg;
import commons.ScoreMsg;
import commons.VisibilityMsg;
import commons.WinnerMsg;
import javafx.beans.property.SimpleStringProperty;

public class client_model {
	
	// Add SimpleStringProperties used for listener in controller
	
	protected SimpleStringProperty newestMessage = new SimpleStringProperty();
	protected SimpleStringProperty newJoin = new SimpleStringProperty();
	protected SimpleStringProperty buttonsVis = new SimpleStringProperty();
	protected SimpleStringProperty buttonsText = new SimpleStringProperty();
	protected SimpleStringProperty buttonImage = new SimpleStringProperty();
	protected SimpleStringProperty myCoins = new SimpleStringProperty();
	protected SimpleStringProperty otherCoins = new SimpleStringProperty();
	protected SimpleStringProperty myCardTaken = new SimpleStringProperty();
	protected SimpleStringProperty otherCardTaken = new SimpleStringProperty();
	protected SimpleStringProperty myCardAction = new SimpleStringProperty();
	protected SimpleStringProperty otherCardAction = new SimpleStringProperty();
	protected SimpleStringProperty winnerVis = new SimpleStringProperty();
	protected SimpleStringProperty loserVis = new SimpleStringProperty();
	protected SimpleStringProperty drawVis = new SimpleStringProperty();
	
	private Logger logger = Logger.getLogger(client_model.class.getName());
	private Socket socket;
	public String name;
	private int joincounter;
	
	// Add counter for my and others card
	
	public int mywachturm = 0;
	public int mytaverne = 0;
	public int mykaserne = 0;
	public int mymuehle = 0;
	public int mybrauerei = 0;
	public int myhexenhaus = 0;
	public int myschloss = 0;
	public int mylazarett = 0;
	
	public int otherwachturm = 0;
	public int othertaverne = 0;
	public int otherkaserne = 0;
	public int othermuehle = 0;
	public int otherbrauerei = 0;
	public int otherhexenhaus = 0;
	public int otherschloss = 0;
	public int otherlazarett = 0;

	public void connect(String ipAddress, int Port, String name) {
		logger.info("Connect");
		this.name = name;
		try {
			socket = new Socket(ipAddress, Port);

			Runnable r = new Runnable() {
				public void run() {
					while (true) {
							
						Message msg = Message.receive(socket);
						
						// Handling of incomming messages based on their type
						
						if (msg instanceof ChatMsg) {	
							ChatMsg chatmsg = (ChatMsg) msg;
							
							// Add chat message to the log area
							
							newestMessage.set(""); 
							newestMessage.set("Chat " + chatmsg.getName() + ": " + chatmsg.getContent());
						
							
							 
						} else if (msg instanceof JoinMsg) {
							JoinMsg joinmsg = (JoinMsg) msg;
							newJoin.set(joinmsg.getName());	
							
							// Add join information to the log area
							
							joincounter++;
							
							// show each join only once
							
							if(joincounter <= 2) {
							newestMessage.set(""); 
							newestMessage.set(joinmsg.getName() + " ist dem Spiel beigetreten.");
							}
																					
						} else if (msg instanceof ScoreMsg) {
							
							/**
							 * distinguish between "action" and "take" score messages
							 * @author R.Thiel
							 */
							
							ScoreMsg scoremsg = (ScoreMsg) msg;
							
							// Add text to listener based on their type
							// - attack and heal to action listener
							// - take to takeCard listener
							
							if(scoremsg.getScoreType().equals("attack")){
								
								if(scoremsg.getName().equals(name)){
									myCardAction.set(((ScoreMsg) msg).getScoreType() + "|" + ((ScoreMsg) msg).getCard());
									} else {
									otherCardAction.set(((ScoreMsg) msg).getScoreType() + "|" + ((ScoreMsg) msg).getCard());
									}
								
								newestMessage.set(""); 
								newestMessage.set(scoremsg.getName() + ": Meine " + scoremsg.getCard() + " ist verwundet !");
																							
							} else if (scoremsg.getScoreType().equals("heal")) {
								
								if(scoremsg.getName().equals(name)){
									myCardAction.set(((ScoreMsg) msg).getScoreType() + "|" + ((ScoreMsg) msg).getCard());
									} else {
									otherCardAction.set(((ScoreMsg) msg).getScoreType() + "|" + ((ScoreMsg) msg).getCard());
									}
								
								newestMessage.set(""); 
								newestMessage.set(scoremsg.getName() + ": " + scoremsg.getCard() + " geheilt.");
								
							} else if (scoremsg.getScoreType().equals("take")) {
								
								if(scoremsg.getName().equals(name)){
									myCardTaken.set(((ScoreMsg) msg).getCard());
									} else {
									otherCardTaken.set(((ScoreMsg) msg).getCard());
									}
								
								newestMessage.set(""); 
								newestMessage.set(scoremsg.getName() + ": " + scoremsg.getCard() + " genommen");
							}
							
							// Add action event to the log area
							
//							newestMessage.set(""); 
//							newestMessage.set("LOG: " + scoremsg.getName() + " " + scoremsg.getScoreType() + " " + scoremsg.getCard());
							
						}	else if (msg instanceof RewardMsg) {
							
							/**
							 * Handle rewards
							 * @author R. Thiel
							 */
							
							RewardMsg rewardmsg = (RewardMsg) msg;
							
							// Add reward info to the log area if i'm the receiver
							
							if(rewardmsg.getName().equals(name)){
								newestMessage.set(""); 
								newestMessage.set("$$$$$ " + rewardmsg.getReward() + " $$$$$");
								myCoins.set(((RewardMsg) msg).getSaldo().toString());
								
							}	else{
								otherCoins.set(((RewardMsg) msg).getSaldo().toString());
							}
							
							// timeout to avoid overlapping messages
							
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							e.printStackTrace();
							}
							
							
						}	else if (msg instanceof VisibilityMsg) {
							
							VisibilityMsg vismsg = (VisibilityMsg) msg;
							
							// Set buttons to visible if i'm the receiver of the visibility message
							
							if(vismsg.getName().equals(name)){
							buttonsVis.set(vismsg.getVisibility());
							}

							
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							e.printStackTrace();
							}
							
							
						}	else if (msg instanceof CardStackMsg) {
							
							/**
							 * Read the CardStack Message
							 * Set the Cards to the Buttons with the listener buttonText in the Client_controller
							 * Set Buttonimg with Buttonimage listener
							 * @author E.Thammavongsa
							 */
							
							CardStackMsg stackmsg = (CardStackMsg) msg;							
							String Card1 = stackmsg.getCard1();
							String Card2 = stackmsg.getCard2();
							String Card3 = stackmsg.getCard3();
							String Card4 = stackmsg.getCard4();
							String Card5 = stackmsg.getCard5();
							String Card6 = stackmsg.getCard6();
							
							// Build string including all 6 cards and set it to a listener
							
							String Cards = Card1 + " " + Card2 + " " + Card3 + " " + Card4 + " " + Card5 + " " + Card6;							
							buttonsText.set(Cards);
							
							// timeout to avoid overlapping messages
							
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							e.printStackTrace();
							}
							
							// Add card information to a separate listener for setting images
							
							buttonImage.set("1"+ Card1);							
							buttonImage.set("2"+ Card2);							
							buttonImage.set("3"+ Card3);							
							buttonImage.set("4"+ Card4);							
							buttonImage.set("5"+ Card5);							
							buttonImage.set("6"+ Card6);
							
							// timeout to avoid overlapping messages
							
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
							e.printStackTrace();
							}
							
							//Read the Winner Message 
						}	else if (msg instanceof WinnerMsg) {
							
							/**
							 * Define the Winner and loser with the Winnermsg at the end of the game.
							 * seperated Listener for Loser and Winner labels
							 * @author E.Thammavongsa
							 */
							
							WinnerMsg winmsg = (WinnerMsg) msg;
							
								//******HERE WINNER ACTION ********
								winnerVis.set(winmsg.getWinner());
								
								try {
									Thread.sleep(300);
								} catch (InterruptedException e) {
								e.printStackTrace();
								}
								
								//******HERE LOSER ACTION ********
								loserVis.set(winmsg.getLoser());
														
						} else if (msg instanceof DrawMsg) {
							DrawMsg drawmsg = (DrawMsg) msg;
							
							drawVis.set(drawmsg.getDraw1() + "|" + drawmsg.getDraw2());
							
							
						}
					}	
				}
				
			};
			
			Thread t = new Thread(r);
			t.start();

			//  Send join message to the server
			 Message msg = new JoinMsg(name);
			 msg.send(socket);
			 
		} catch (Exception e) {
			logger.warning(e.toString());
		}
	}

	public void disconnect() {
		logger.info("Disconnect");
		if (socket != null)
			try {
				socket.close();
			} catch (IOException e) {
			}
	}

	public void takeCard(String card) {
		logger.info("Take card");
		Message msg = new ScoreMsg(name, card, "take");
		msg.send(socket);
		
		// timeout to avoid overlapping messages
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		e.printStackTrace();
		}
	}
	
	
	/**
	 * Send taken Card position to server for removing from Stack
	 * @author E.Thammavongsa
	 */
	
	public void takenCard(int position) {
		logger.info("Send position to Server");
		Message msg = new CardTakenMsg(position);
		msg.send(socket);
		
		// timeout to avoid overlapping messages
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		e.printStackTrace();
		}
	}
	
	// Send a message to the server via socket
	
	public void sendMessage(String message) {
		logger.info("Send message");
		Message msg = new ChatMsg(name, message);
		msg.send(socket);
	}

	// Receive a message via socket

	public String receiveMessage() {
		logger.info("Receive message");
		return newestMessage.get();
	}
	
	// Receive a visibility message
	
	public String receiveVisibility() {
		logger.info("Receive visibility");
		return buttonsVis.get();
	}
	

}
