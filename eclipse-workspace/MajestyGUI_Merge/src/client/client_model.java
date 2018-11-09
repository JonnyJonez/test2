package client;


import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import javax.sound.midi.ControllerEventListener;
import javax.swing.text.View;

import commons.CardStackMsg;
import commons.CardTakenMsg;
import commons.ChatMsg;
import commons.JoinMsg;
import commons.Message;
import commons.MessageType;
import commons.RewardMsg;
import commons.ScoreMsg;
import commons.ScoreType;
import commons.VisibilityMsg;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import server.player;


/**
 * Note: One error in this implementation: The *display* of received messages is triggered
 * by a ChangeListener attached to the SimpleStringProperty. If the newly received message
 * is *identical* to the current contents of the SimpleStringProperty, then there is no
 * change, and the new (duplicate) message is not displayed.
 */
public class client_model {
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
	
	

	private Logger logger = Logger.getLogger("");
	private Socket socket;
	public String name;
	
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
				@Override
				public void run() {
					while (true) {
							
						Message msg = Message.receive(socket);
						
						// Handling of incomming messages
						
						if (msg instanceof ChatMsg) {	
							ChatMsg chatmsg = (ChatMsg) msg;
							newestMessage.set(""); // erase previous message
							newestMessage.set(chatmsg.getName() + ": " + chatmsg.getContent());
							 
						} else if (msg instanceof JoinMsg) {
							JoinMsg joinmsg = (JoinMsg) msg;
							newJoin.set(joinmsg.getName());														
							newestMessage.set(""); // erase previous message
							newestMessage.set("-------" + joinmsg.getName() + " joined the chat ------");
							
														
						} else if (msg instanceof ScoreMsg) {
							
							ScoreMsg scoremsg = (ScoreMsg) msg;
							
							if(scoremsg.getScoreType().equals("attack")){
								
								if(scoremsg.getName().equals(name)){
									myCardAction.set(((ScoreMsg) msg).getScoreType() + "|" + ((ScoreMsg) msg).getCard());
									} else {
									otherCardAction.set(((ScoreMsg) msg).getScoreType() + "|" + ((ScoreMsg) msg).getCard());
									}
																							
							} else if (scoremsg.getScoreType().equals("heal")) {
								
								if(scoremsg.getName().equals(name)){
									myCardAction.set(((ScoreMsg) msg).getScoreType() + "|" + ((ScoreMsg) msg).getCard());
									} else {
									otherCardAction.set(((ScoreMsg) msg).getScoreType() + "|" + ((ScoreMsg) msg).getCard());
									}
								
							} else if (scoremsg.getScoreType().equals("take")) {
								
								if(scoremsg.getName().equals(name)){
									myCardTaken.set(((ScoreMsg) msg).getCard());
									} else {
									otherCardTaken.set(((ScoreMsg) msg).getCard());
									}
								
							}
							
							newestMessage.set(""); // erase previous message
							newestMessage.set("***" + scoremsg.getName() + scoremsg.getScoreType() + scoremsg.getCard() + " ***");
							
						}	else if (msg instanceof RewardMsg) {
							RewardMsg rewardmsg = (RewardMsg) msg;
							
							
							if(rewardmsg.getName().equals(name)){								
								newestMessage.set(""); // erase previous message
								newestMessage.set("$$$ Erhaltene Coins: " + rewardmsg.getReward() + " || Neuer Kontostand: " + rewardmsg.getSaldo());
								myCoins.set(((RewardMsg) msg).getSaldo().toString());
							}	else{
								otherCoins.set(((RewardMsg) msg).getSaldo().toString());
							}
							
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							e.printStackTrace();
							}
							
						}	else if (msg instanceof VisibilityMsg) {
							VisibilityMsg vismsg = (VisibilityMsg) msg;
							
							if(vismsg.getName().equals(name)){
							buttonsVis.set(vismsg.getVisibility());
							}
							newestMessage.set("");
							newestMessage.set("----- Turn buttons " + vismsg.getVisibility() + " for " + vismsg.getName());
							
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							e.printStackTrace();
							}
							
						}	else if (msg instanceof CardStackMsg) {
							CardStackMsg stackmsg = (CardStackMsg) msg;							
							String Card1 = stackmsg.getCard1();
							String Card2 = stackmsg.getCard2();
							String Card3 = stackmsg.getCard3();
							String Card4 = stackmsg.getCard4();
							String Card5 = stackmsg.getCard5();
							String Card6 = stackmsg.getCard6();
//							String[] cards = {Card1, Card2, Card3, Card4, Card5, Card6};
														
							String Cards = Card1 + " " + Card2 + " " + Card3 + " " + Card4 + " " + Card5 + " " + Card6;
														
							buttonsText.set(Cards);
							
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							e.printStackTrace();
							}
							
						
							
//							logger.info("$$$$$$$$$$$$$ReadButton Text $$$$$$$$$$$$$$$");
							
							buttonImage.set("1"+ Card1);
//							buttonImage.set("2"+ Card2);
//							buttonImage.set("3"+ Card3);
//							buttonImage.set("4"+ Card4);
//							buttonImage.set("5"+ Card5);
//							buttonImage.set("6"+ Card6);
							
							
							newestMessage.set("");
							newestMessage.set("**** Card Stack Updated **** \n " +  Cards);
							
							
							
						}
										
						// If the client is sending the _same_ message as before, we cannot simply
						// set the property, because this would not be a change, and the change
						// listener will not trigger. Therefore, we first erase the previous message.
						// This is a change, but empty messages are ignored by the change-listener
						
						
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
				// Uninteresting
			}
	}

	public void takeCard(String card) {
		logger.info("Take card");
		Message msg = new ScoreMsg(name, card, "take");
		msg.send(socket);
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		e.printStackTrace();
		}
	
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//		e.printStackTrace();
//		}
//		
//		//Send Location of the taken Card
//		logger.info("Taken Person Card");
//		Message msg2 = new CardTakenMsg(position);
//		msg.send(socket);
	}
	
	//Send position to server to remove
	public void takenCard(int position) {
		logger.info("Send position to Server");
		Message msg = new CardTakenMsg(position);
		msg.send(socket);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		e.printStackTrace();
		}
		
		}
	
	
	public void sendMessage(String message) {
		logger.info("Send message");
		Message msg = new ChatMsg(name, message);
		msg.send(socket);
	}


	public String receiveMessage() {
		logger.info("Receive message");
		return newestMessage.get();
	}
	
	public String receiveVisibility() {
		logger.info("Receive visibility");
		return buttonsVis.get();
	}
	

}
