package commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Logger;

import commons.Message;
import commons.MessageType;

/**
 * Credit: Prof Dr. Bradley Richards
 */

public abstract class Message {
		private static Logger logger = Logger.getLogger(Message.class.getName());
		
		protected MessageType type;
		
		public Message(MessageType type) {
			this.type = type;
		}
		
		public void send(Socket socket) {
			OutputStreamWriter out;
			try {
				out = new OutputStreamWriter(socket.getOutputStream());
				logger.info("Sending message: " + this.toString());
				out.write(this.toString() + "\n");
				out.flush();
			} catch (IOException e) {
				logger.warning(e.toString());
			}
		}
		
		//Receive Message for Client and Server
		public static Message receive(Socket socket) {
			BufferedReader in;
			Message msg = null;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String msgText = in.readLine(); // Will wait here for complete line
				logger.info("Receiving message: " + msgText);
				
				// Parse message
				
				/**
				 * Convert string back to a message based on their message type
				 * @author P. Mächler
				 */
				
				String[] parts = msgText.split("\\|");
				if (parts[0].equals(MessageType.Join.toString())) {
					msg = new JoinMsg(parts[1]);
				} else if (parts[0].equals(MessageType.Chat.toString())) {
					msg = new ChatMsg(parts[1], parts[2]);
				} else if (parts[0].equals(MessageType.Score.toString())) {
					msg = new ScoreMsg(parts[1], parts[2], parts[3]);
				} else if (parts[0].equals(MessageType.Reward.toString())) {
					msg = new RewardMsg(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
				}  else if (parts[0].equals(MessageType.Visibility.toString())) {
					msg = new VisibilityMsg(parts[1], parts[2]);		
				} else if (parts[0].equals(MessageType.CardStack.toString())) {
					msg = new CardStackMsg(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
				} else if (parts[0].equals(MessageType.CardTakenMsg.toString())) {
					msg = new CardTakenMsg(Integer.parseInt(parts[1]));
				} else if (parts[0].equals(MessageType.Winner.toString())) {
					msg = new WinnerMsg(parts[1], parts[2]);
				} else if (parts[0].equals(MessageType.Draw.toString())) {
					msg = new DrawMsg(parts[1], parts[2]);
					
									
				}
				
			} catch (IOException e) {
				logger.warning(e.toString());
			}
			return msg;
		}
		
		public MessageType getType() {
			return type;
		}
	}
