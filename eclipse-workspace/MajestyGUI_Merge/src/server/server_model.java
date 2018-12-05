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
import commons.WinnerMsg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Credit: Prof Dr. Bradley Richards
 */	

public class server_model {
	protected final ObservableList<player> players = FXCollections.observableArrayList();

	private final Logger logger = Logger.getLogger("");
	private ServerSocket listener;
	private volatile boolean stop = false;
	private int maxPlayer = 2;
	public String erster;
	public String zweiter;
	public CardStack s1;
	// @Ali: Weil am Anfang noch kein Winner feststeht -> null
	private player winner = null;
	private player loser = null;
	
	// @Ali: Punkte für die Auswertung
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
	
	public void setComplete(){
		
		boolean allComplete = true;  // gehe davon aus, dass alle player genau 12 Karten gezogen haben und fertig sind
		for (player p : players) {   // durch alle player durch
			if (!p.getComplete()) {  // wenn der aktuelle Spieler nicht fertig ist, dann setze ich allcomplete auf falsch
				allComplete = false;
			}
		}
		
//		VisibilityMsg vismsg1 = new VisibilityMsg(erster, "false");
//		broadcast(vismsg1);	
//		logger.info("set zweiter visible false " + zweiter);
//		
//		VisibilityMsg vismsg2 = new VisibilityMsg(zweiter, "false");
//		broadcast(vismsg2);	
//		logger.info("set zweiter visible false " + zweiter);
//		
		
		System.out.println("all complete: " + allComplete);
		
		// wenn alle Spieler fertig sind, Auswertung starten
		if (allComplete) {
			endResult();
		}
	}
	
	
	public void endResult() {
		// Saldo als Punkte auf Spieler setzen (zum unterscheiden von Münzen (Saldo) zu Punkten (Points))
		for (player p : players) {
			p.setPoints(p.getSaldo());
		}
		
		// für das Vergleichen die Spieler 1 und 2 separat speichern
		player playerOne = players.get(0);
		player playerTwo = players.get(1);
		
		// 1. Wertung: Lazarett
		playerOne.setPoints(playerOne.getPoints() - playerOne.getLazarett());
		playerTwo.setPoints(playerTwo.getPoints() - playerTwo.getLazarett());
		
		// 2. Wertung: unterschiedliche Personen
		// zum Speichern der Anzahl verschiedenen Karten
		int oneDifferentCards = 0;
		int twoDifferentCards = 0;
		
		// durch alle Kartentypen durchgehen
		// Mühlen
		if (playerOne.getMuehle() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getMuehle() > 0) {
			twoDifferentCards++;
		}
		// Brauer
		if (playerOne.getBrauerei() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getBrauerei() > 0) {
			twoDifferentCards++;
		}
		// Hexe
		if (playerOne.getHexenhaus() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getHexenhaus() > 0) {
			twoDifferentCards++;
		}
		// Wache
		if (playerOne.getWachturm() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getWachturm() > 0) {
			twoDifferentCards++;
		}
		// Soldat
		if (playerOne.getKaserne() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getKaserne() > 0) {
			twoDifferentCards++;
		}
		// Wirt
		if (playerOne.getTaverne() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getTaverne() > 0) {
			twoDifferentCards++;
		}
		// Adelige
		if (playerOne.getSchloss() > 0) {
			oneDifferentCards++;
		}
		if (playerTwo.getSchloss() > 0) {
			twoDifferentCards++;
		}
		
		// Anzahl verschiedene Karten mit sich selber mutliplizieren und zu den Punkten zählen
		playerOne.setPoints(playerOne.getPoints() + (oneDifferentCards * oneDifferentCards));
		playerTwo.setPoints(playerTwo.getPoints() + (twoDifferentCards * twoDifferentCards));
		
		// 3. Wertung: Mehrheitswertung
		// durch alle Kartentypen durchgehen und Rewards setzen
		
		// Mühle
		// von beiden Spielern die Anzahl der Müllerinnen auslesen
		int oneMuehle = playerOne.getMuehle();
		int twoMuehle = playerTwo.getMuehle();
		
		// Anzahl Müllerinnen vergleichen
		if (oneMuehle > twoMuehle) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_MUEHLE);
		} else if (oneMuehle < twoMuehle) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_MUEHLE);
		} else if (oneMuehle == twoMuehle) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_MUEHLE);
			playerOne.setPoints(playerOne.getPoints() + VALUE_MUEHLE);
		}
		
		// Brauerei
		// von beiden Spielern die Anzahl der Brauer auslesen
		int oneBrauerei = playerOne.getBrauerei();
		int twoBrauerei = playerTwo.getBrauerei();
		
		// Anzahl Brauer vergleichen
		if (oneBrauerei > twoBrauerei) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_BRAUEREI);
		} else if (oneBrauerei < twoBrauerei) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_BRAUEREI);
		} else if (oneBrauerei == twoBrauerei) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_BRAUEREI);
			playerOne.setPoints(playerOne.getPoints() + VALUE_BRAUEREI);
		}
		
		// Hexenhaus
		// von beiden Spielern die Anzahl der Hexen auslesen
		int oneHexenhaus = playerOne.getHexenhaus();
		int twoHexenhaus = playerTwo.getHexenhaus();
		
		// Anzahl Hexen vergleichen
		if (oneHexenhaus > twoHexenhaus) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_HEXENHAUS);
		} else if (oneHexenhaus < twoHexenhaus) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_HEXENHAUS);
		} else if (oneHexenhaus == twoHexenhaus) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_HEXENHAUS);
			playerOne.setPoints(playerOne.getPoints() + VALUE_HEXENHAUS);
		}
		
		// Wachturm
		// von beiden Spielern die Anzahl der Wachen auslesen
		int oneWachturm = playerOne.getWachturm();
		int twoWachturm = playerTwo.getWachturm();
		
		// Anzahl Wachen vergleichen
		if (oneWachturm > twoWachturm) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_WACHTURM);
		} else if (oneWachturm < twoWachturm) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_WACHTURM);
		} else if (oneWachturm == twoWachturm) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_WACHTURM);
			playerOne.setPoints(playerOne.getPoints() + VALUE_WACHTURM);
		}
		
		// Kaserne
		// von beiden Spielern die Anzahl der Soldaten auslesen
		int oneKaserne = playerOne.getKaserne();
		int twoKaserne = playerTwo.getKaserne();
		
		// Anzahl Soldaten vergleichen
		if (oneKaserne > twoKaserne) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_KASERNE);
		} else if (oneKaserne < twoKaserne) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_KASERNE);
		} else if (oneKaserne == twoKaserne) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_KASERNE);
			playerOne.setPoints(playerOne.getPoints() + VALUE_KASERNE);
		}
		
		// Taverne
		// von beiden Spielern die Anzahl der Wirten auslesen
		int oneTaverne = playerOne.getTaverne();
		int twoTaverne = playerTwo.getTaverne();
		
		// Anzahl Wirten vergleichen
		if (oneTaverne > twoTaverne) {
			playerOne.setPoints(playerOne.getPoints() + VALUE_TAVERNE);
		} else if (oneTaverne < twoTaverne) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_TAVERNE);
		} else if (oneTaverne == twoTaverne) {
			playerTwo.setPoints(playerTwo.getPoints() + VALUE_TAVERNE);
			playerOne.setPoints(playerOne.getPoints() + VALUE_TAVERNE);
		}
		
		// Schloss
		// von beiden Spielern die Anzahl der Adlige auslesen
		int oneSchloss = playerOne.getSchloss();
		int twoSchloss = playerTwo.getSchloss();
		
		// Anzahl Adlige vergleichen
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
		
		
		System.out.println("Auswertung:");
		System.out.println(playerOne.getName() + ": " + playerOne.getPoints());
		System.out.println(playerTwo.getName() + ": " + playerTwo.getPoints());
		winner();
	}
	
	/**@Author Ali */
	public void winner() {
		for (player p : players) {
			// falls noch kein winner gesetzt ist (null), wird der erste Spieler in der Schlauf als Winner gesetzt egal wie viel Punkte er hat
			// sobald ein Winner gesetzt ist, wird überprüft ob die Punkte des Spielers höher sind als die Punkte des momentan gesetzten Winners
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
		System.out.println("the winner is: " + winner.getName());
	}
}
