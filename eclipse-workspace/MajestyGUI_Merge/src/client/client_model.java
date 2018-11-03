package client;


import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import javax.sound.midi.ControllerEventListener;

import commons.CardStackMsg;
import commons.CardTakenMsg;
import commons.ChatMsg;
import commons.JoinMsg;
import commons.Message;
import commons.MessageType;
import commons.RewardMsg;
import commons.ScoreMsg;
import commons.VisibilityMsg;
import javafx.beans.property.SimpleBooleanProperty;
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
	protected SimpleStringProperty buttonsVis = new SimpleStringProperty();
	protected SimpleStringProperty buttonsText = new SimpleStringProperty();

	private Logger logger = Logger.getLogger("");
	private Socket socket;
	private String name;

	public void connect(String ipAddress, int Port, String name) {
		logger.info("Connect");
		this.name = name;
		try {
			socket = new Socket(ipAddress, Port);

			// Create thread to read incoming messages
			Runnable r = new Runnable() {
				@Override
				public void run() {
					while (true) {
						 
						// Logik
						
						Message msg = Message.receive(socket);
						
						// Handling of incomming messages
						
						if (msg instanceof ChatMsg) {	
							ChatMsg chatmsg = (ChatMsg) msg;
							newestMessage.set(""); // erase previous message
							newestMessage.set(chatmsg.getName() + ": " + chatmsg.getContent());
							 
						} else if (msg instanceof JoinMsg) {
							JoinMsg joinmsg = (JoinMsg) msg;
							newestMessage.set(""); // erase previous message
							newestMessage.set("-------" + joinmsg.getName() + " joined the chat ------");
							
						} else if (msg instanceof ScoreMsg) {
							ScoreMsg scoremsg = (ScoreMsg) msg;
							newestMessage.set(""); // erase previous message
							newestMessage.set("***" + scoremsg.getName() + " took " + scoremsg.getCard() + " ***");
							
						}	else if (msg instanceof RewardMsg) {
							RewardMsg rewardmsg = (RewardMsg) msg;
							
							if(rewardmsg.getName().equals(name)){
							newestMessage.set(""); // erase previous message
							newestMessage.set("$$$ Erhaltene Coins: " + rewardmsg.getReward() + " || Neuer Kontostand: " + rewardmsg.getSaldo());
							}							
							
						}	else if (msg instanceof VisibilityMsg) {
							VisibilityMsg vismsg = (VisibilityMsg) msg;
							
							if(vismsg.getName().equals(name)){
							buttonsVis.set(vismsg.getVisibility());
							}
							
						}	else if (msg instanceof CardStackMsg) {
							CardStackMsg stackmsg = (CardStackMsg) msg;
							
							
							String Card1 = stackmsg.getCard1();
							String Card2 = stackmsg.getCard2();
							String Card3 = stackmsg.getCard3();
							String Card4 = stackmsg.getCard4();
							String Card5 = stackmsg.getCard5();
							String Card6 = stackmsg.getCard6();
							String Cards = Card1 + " " + Card2 + " " + Card3 + " " + Card4 + " " + Card5 + " " + Card6;
							buttonsText.set(Cards);
//							logger.info("$$$$$$$$$$$$$ReadButton Text $$$$$$$$$$$$$$$");
							
							
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
		Message msg = new ScoreMsg(name, card);
		msg.send(socket);
	
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
