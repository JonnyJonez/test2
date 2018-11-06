package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import commons.CardStack;
import commons.CardStackMsg;
import commons.ChatMsg;
import commons.JoinMsg;
import commons.Message;
import commons.RewardMsg;
import commons.ScoreMsg;
import commons.VisibilityMsg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class server_model {
	protected final ObservableList<player> players = FXCollections.observableArrayList();

	private final Logger logger = Logger.getLogger("");
	private ServerSocket listener;
	private volatile boolean stop = false;
	private int maxPlayer = 2;
	public String erster;
	public String zweiter;
	public CardStack s1;

	public void startServer(int port) {
		logger.info("Start server");
		
		try {
			listener = new ServerSocket(port, 10, null);
			Runnable r = new Runnable() {
				@Override
				public void run() {
										
					while (!stop) {
						try {
							
							Socket socket = listener.accept();	
							
							if(players.size() < maxPlayer){
								
								player player = new player(server_model.this, socket);
								players.add(player); 
								
								if(players.size() == 1){
									player.setErster();
									player.setTurn();
									VisibilityMsg firstmsg = new VisibilityMsg(erster, "false");
									broadcast(firstmsg);	
									
								}
								
								if(players.size() == maxPlayer){
									
									
									
									// Stack erstellen
									
									s1 = new CardStack();
									logger.info("ready to send cards");
									
									CardStackMsg cardSmsg = new CardStackMsg(s1.getCard(1), s1.getCard(2), s1.getCard(3), s1.getCard(4), s1.getCard(5), s1.getCard(6));
									broadcast(cardSmsg);
									logger.info("send cards");
									
									
									
									// Erster herausfinden
									
									for (player p : players) {
										p.getErster();
										if(p.getErster() == "true"){
											erster = p.getName();
											
										} else if(p.getErster() == "false") {
											zweiter = p.getName();
										}
									}
										
									// JoinMsg senden
										
									for (player p : players) {
										
									try {
										Thread.sleep(200);
									} catch (InterruptedException e) {
									e.printStackTrace();
									}	
									
									JoinMsg joinmsg = new JoinMsg(p.getName());
									broadcast(joinmsg);
											
											
									}
										
									// Visibility msg senden an erster
									
																										
									VisibilityMsg vismsg = new VisibilityMsg(erster, "true");
									broadcast(vismsg);
									logger.info("set erster visible " + erster);
									
									
									try {
										Thread.sleep(300);
									} catch (InterruptedException e) {
									e.printStackTrace();
									}
									
									VisibilityMsg vismsg2 = new VisibilityMsg(zweiter, "false");
									broadcast(vismsg2);	
									logger.info("set zweiter visible false " + zweiter);
								
									
								}
							
							} else {
								// Pech, schon alle da
							}
							
							
							
							
						} catch (Exception e) {
							logger.info(e.toString());
						}
						
				}
				}
			};
			
			Thread t = new Thread(r, "ServerSocket");
			t.start();
			
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	public void stopServer() {
		logger.info("Stop all clients");
		for (player p : players) 
			p.stop();
		logger.info("Stop server");
		stop = true;
		if (listener != null) {
			try {
				listener.close();
			} catch (IOException e) {
				// Uninteresting
			}
		}
	}
	
	// Broadcast of messages
	
	public void broadcast(ChatMsg chatMsg) {
		logger.info("Broadcasting Chatmessage to clients");
		for (player p : players) {
			p.send(chatMsg);
		}
	}
	
	public void broadcast(RewardMsg rewardmsg) {
		logger.info("Broadcasting Rewardmessage to clients");
		for (player p : players) {
			p.send(rewardmsg);
		}
	}// Jonez was here
	
	public void broadcast(JoinMsg joinMsg) {
		logger.info("Broadcasting Joinmessage to clients");
		for (player p : players) {
			p.send(joinMsg);
		}
	}
	
	public void broadcast(ScoreMsg scoremsg) {
		logger.info("Broadcasting cards");
		for (player p : players) {
			p.send(scoremsg);
		}
	}
	
	public void broadcast(VisibilityMsg vismsg) {
		logger.info("Broadcasting Visibility msg");
		for (player p : players) {
			p.send(vismsg);
		}
	}
	
	public void broadcast(CardStackMsg stackmsg) {
		logger.info("Broadcasting CardStack msg");
		for (player p : players) {
			p.send(stackmsg);
		}
	}
	
	public String getNames(){
		String msg = "Spieler: ";
		for (player p : players) {
		msg += (p.getName()+ " ");
		}
		
		return msg;
	
	}
	
	// Get all players with certain card
	
	public List<String> getPlayersWithCard(String s){
		
		List<String> array = new ArrayList<String>();
		String test;
		
		if(s.equals("Muehle")) {
			for (player p : players) {
				if(p.getMuehle() >= 1){
					test = p.getName() + "|" + p.getSaldo();
					array.add(test);
				}
			}
			
		} else if (s.equals("Brauerei")) {
			for (player p : players) {
				if(p.getBrauerei() >= 1){
					test = p.getName() + "|" + p.getSaldo();
					array.add(test);
				}
			}
		}		
		
		return array;

	}
	
	// Get Players mit weniger Verteitigung 
	
	public void attackAll(int attackers, String name){
		
		// Search Playeers
		for (player p : players) {
			if(!p.getName().equals(name)){
			if(p.getWachturm() < attackers){
				p.attack();
				logger.info("attack players");
				}
			}
		}
		
		//
	
		
	}
	
	
	// Get saldo of a player

	public void setSaldi(String string) {
		String[] parts = string.split("\\|");
			for (player p : players) {
				if(p.getName().equals(parts[0])) {
					p.setSaldo(Integer.parseInt(parts[1]));
				}
			}
	
	}
	
	// Change turn of players
	
	public void changeTurn(){
		for (player p : players) {
			p.changeTurn();
		}
	}
	
	// Get number of players
	
	public int getPlayerCount(){
		return players.size();
	}
	
	// Check if name is already used

	public String checknames(String name) {
		String returnName = name;
		boolean given = false;
		for (player p : players) {
			if(p.getName().equals(name)){
				given = true;
			}
		}
		
		if(given == true){
			returnName = name + "2";			
		}
		return returnName;
	}
	
	

	
}
