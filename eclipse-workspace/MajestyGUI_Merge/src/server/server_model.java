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
import commons.RewardMsg;
import commons.ScoreMsg;
import commons.VisibilityMsg;
import commons.WinnerMsg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Credit: Prof Dr. Bradley Richards
 */	

public class server_model {
	protected final ObservableList<player> players = FXCollections.observableArrayList();

	private final Logger logger = Logger.getLogger(server_model.class.getName());
	private ServerSocket listener;
	private volatile boolean stop = false;
	private int maxPlayer = 2;
	public String erster;
	public String zweiter;
	public CardStack s1;
	
	// @Ali
	private player winner = null;
	private player loser = null;
	
	// @Ali: card points for evaluation
	private static final int VALUE_MUEHLE = 10;
	private static final int VALUE_BRAUEREI = 11;
	private static final int VALUE_HEXENHAUS = 12;
	private static final int VALUE_WACHTURM = 13;
	private static final int VALUE_KASERNE = 14;
	private static final int VALUE_TAVERNE = 15;
	private static final int VALUE_SCHLOSS = 16;

	public void startServer(int port) {
		logger.info("Start server");
		
		try {
			listener = new ServerSocket(port, 10, null);
			Runnable r = new Runnable() {
				public void run() {
										
					while (!stop) {
						try {
							
							Socket socket = listener.accept();	
							
							// Accept new players as long as not all spaces are taken
							
							if(players.size() < maxPlayer){
																
								player player = new player(server_model.this, socket);
								players.add(player); 
								
								// Set "first" flag to the first joiner
								
								if(players.size() == 1){
									player.setErster();
									player.setTurn();
									VisibilityMsg firstmsg = new VisibilityMsg(erster, "false");
									broadcast(firstmsg);	
								}
								

								if(players.size() == maxPlayer){
																		
									// Create stack
									/**
									 * If all Users are Online, the Cardstack will be generated and broadcasted.
									 * @author E. Thammavongsa
									 */
									
									s1 = new CardStack();
									logger.info("ready to send cards");
									
									// Send first 6 cards
									CardStackMsg cardSmsg = new CardStackMsg(s1.getCard(1), s1.getCard(2), s1.getCard(3), s1.getCard(4), s1.getCard(5), s1.getCard(6));
									broadcast(cardSmsg);
									logger.info("send cards");	
									
									// Avoid overlapping messages

									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
									e.printStackTrace();
									}
									
									/**
									 * Find first joiner and broadcast join messages & visibility messages
									 * @author: P. Mächler
									 */
																		
									for (player p : players) {
										p.getErster();
										if(p.getErster() == "true"){
											erster = p.getName();
											
										} else if(p.getErster() == "false") {
											zweiter = p.getName();
										}
									}
										
									// Send Join messages
										
									for (player p : players) {
										
										// Avoid overlapping messages
											
										try {
											Thread.sleep(200);
										} catch (InterruptedException e) {
										e.printStackTrace();
										}	
									
									JoinMsg joinmsg = new JoinMsg(p.getName());
									broadcast(joinmsg);
												
									}
										
									// Send visibility message (true) to the first player to start the game
																																			
									VisibilityMsg vismsg = new VisibilityMsg(erster, "true");
									broadcast(vismsg);
									logger.info("set erster visible " + erster);
									
									// Avoid overlapping messages
									
									try {
										Thread.sleep(300);
									} catch (InterruptedException e) {
									e.printStackTrace();
									}
									
									// Send visibility message (false) to the second player to wait 
																		
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
			}
		}
	}
	
	/**
	 * Broadcast messages to all players
	 * @author: P. Mächler
	 */	
	
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
	}
	
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
	
	public void broadcast(WinnerMsg winmsg) {
		logger.info("Broadcasting Winner msg");
		for (player p : players) {
			p.send(winmsg);
		}
	}
	
	// Get names of all players
	
	public String getNames(){
		String msg = "Spieler: ";
		
		for (player p : players) {
		msg += (p.getName()+ " ");
		}
		
		return msg;
	}
	
	// Get all players with a certain card 
	// Used for  rewards concerning other players (used in Taverne or Muehle)
	
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
	
	// Attack all player with less defense than attackers
	
	public void attackAll(int attackers, String name){
				
		for (player p : players) {
			if(!p.getName().equals(name)){
			if(p.getWachturm() < attackers){
				p.attack();
				logger.info("attack players");
				}
			}
		}	
	}
	
	
	// Get saldo of a certain player

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
	
	/**
	 * end the game after everybody got 12 cards
	 * @author A. Atici
	 */
	
	public void setComplete(){
		
		boolean allComplete = true;  
		for (player p : players) {   
			if (!p.getComplete()) { 
				allComplete = false;
			}
		}
		
		logger.info("all complete: " + allComplete);
		
		if (allComplete) {
			endResult();
		}
	}
	
	/**
	 * this method contains the evaluation of points per gamer
	 * @author A. Atici
	 */
	
	public void endResult() {
		
		for (player p : players) {
			p.setPoints(p.getSaldo());
		}
		
		player playerOne = players.get(0);
		player playerTwo = players.get(1);
		
		// first evaluation: looking at the lazarett aspect
		playerOne.setPoints(playerOne.getPoints() - playerOne.getLazarett());
		playerTwo.setPoints(playerTwo.getPoints() - playerTwo.getLazarett());
		
		//  second evaluation: multiplication different card typs with themselves
		int oneDifferentCards = 0;
		int twoDifferentCards = 0;
		

		if (playerOne.getMuehle() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getMuehle() > 0) {
			twoDifferentCards++;
		}
		
		if (playerOne.getBrauerei() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getBrauerei() > 0) {
			twoDifferentCards++;
		}
		
		if (playerOne.getHexenhaus() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getHexenhaus() > 0) {
			twoDifferentCards++;
		}
	
		if (playerOne.getWachturm() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getWachturm() > 0) {
			twoDifferentCards++;
		}
	
		if (playerOne.getKaserne() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getKaserne() > 0) {
			twoDifferentCards++;
		}
		
		if (playerOne.getTaverne() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getTaverne() > 0) {
			twoDifferentCards++;
		}
		
		if (playerOne.getSchloss() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getSchloss() > 0) {
			twoDifferentCards++;
		}
		
		// adding the actual points to the second evaluation
		playerOne.setPoints(playerOne.getPoints() + (oneDifferentCards * oneDifferentCards));
		playerTwo.setPoints(playerTwo.getPoints() + (twoDifferentCards * twoDifferentCards));
		
		// third evaluation: go through all typ of cards and add reward
		int oneMuehle = playerOne.getMuehle();
		int twoMuehle = playerTwo.getMuehle();
		
		
		if (oneMuehle > twoMuehle) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_MUEHLE);
		} else if (oneMuehle < twoMuehle) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_MUEHLE);
		} else if (oneMuehle == twoMuehle) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_MUEHLE);
			playerOne.setPoints(playerOne.getPoints() + VALUE_MUEHLE);
		}
		
		
		int oneBrauerei = playerOne.getBrauerei();
		int twoBrauerei = playerTwo.getBrauerei();
		
		if (oneBrauerei > twoBrauerei) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_BRAUEREI);
		} else if (oneBrauerei < twoBrauerei) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_BRAUEREI);
		} else if (oneBrauerei == twoBrauerei) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_BRAUEREI);
			playerOne.setPoints(playerOne.getPoints() + VALUE_BRAUEREI);
		}
		
		int oneHexenhaus = playerOne.getHexenhaus();
		int twoHexenhaus = playerTwo.getHexenhaus();
		
		if (oneHexenhaus > twoHexenhaus) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_HEXENHAUS);
		} else if (oneHexenhaus < twoHexenhaus) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_HEXENHAUS);
		} else if (oneHexenhaus == twoHexenhaus) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_HEXENHAUS);
			playerOne.setPoints(playerOne.getPoints() + VALUE_HEXENHAUS);
		}
		
		int oneWachturm = playerOne.getWachturm();
		int twoWachturm = playerTwo.getWachturm();
		
		if (oneWachturm > twoWachturm) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_WACHTURM);
		} else if (oneWachturm < twoWachturm) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_WACHTURM);
		} else if (oneWachturm == twoWachturm) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_WACHTURM);
			playerOne.setPoints(playerOne.getPoints() + VALUE_WACHTURM);
		}
		
		int oneKaserne = playerOne.getKaserne();
		int twoKaserne = playerTwo.getKaserne();
		
		if (oneKaserne > twoKaserne) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_KASERNE);
		} else if (oneKaserne < twoKaserne) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_KASERNE);
		} else if (oneKaserne == twoKaserne) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_KASERNE);
			playerOne.setPoints(playerOne.getPoints() + VALUE_KASERNE);
		}
		
		int oneTaverne = playerOne.getTaverne();
		int twoTaverne = playerTwo.getTaverne();
		
		if (oneTaverne > twoTaverne) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_TAVERNE);
		} else if (oneTaverne < twoTaverne) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_TAVERNE);
		} else if (oneTaverne == twoTaverne) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_TAVERNE);
			playerOne.setPoints(playerOne.getPoints() + VALUE_TAVERNE);
		}
		
		int oneSchloss = playerOne.getSchloss();
		int twoSchloss = playerTwo.getSchloss();
		
		if (oneSchloss > twoSchloss) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_SCHLOSS);
		} else if (oneSchloss < twoSchloss) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_SCHLOSS);
		} else if (oneSchloss == twoSchloss) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_SCHLOSS);
			playerOne.setPoints(playerOne.getPoints() + VALUE_SCHLOSS);
		}
		

		//determine the winner and broadcast it to all players
		playerOne.setSaldo(playerOne.getPoints());
		playerTwo.setSaldo(playerTwo.getPoints());
		RewardMsg rewardmsgOne = new RewardMsg(playerOne.getName(), 0, playerOne.getSaldo());
		RewardMsg rewardmsgTwo = new RewardMsg(playerTwo.getName(), 0, playerTwo.getSaldo());
		broadcast(rewardmsgOne);
		broadcast(rewardmsgTwo);
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
		e.printStackTrace();
		}
		
		// adding the actual points to the third evaluation
		logger.info("Auswertung:");
		logger.info(playerOne.getName() + ": " + playerOne.getPoints());
		logger.info(playerTwo.getName() + ": " + playerTwo.getPoints());
		winner();
	}
	
	/**
	 * evaluation of winner
	 * @author A. Atici
	 */
	public void winner() {
		for (player p : players) {
			if(winner == null && loser == null) {
				loser = p;
				winner = p;				
			}else {
				if (p.getPoints() > winner.getPoints())	{
					winner = p;
				} else
					loser = p;
			} 		
			
		}		
		
		WinnerMsg winmsg = new WinnerMsg(winner.getName(),loser.getName());
		broadcast(winmsg);
		logger.info("the winner is: " + winner.getName());
	}
}
