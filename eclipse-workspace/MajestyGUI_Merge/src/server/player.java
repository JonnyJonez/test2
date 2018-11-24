package server;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import commons.JoinMsg;
import commons.Card;
import commons.CardStack;
import commons.CardStackMsg;
import commons.CardTakenMsg;
import commons.ChatMsg;
import commons.Message;
import commons.MessageType;
import commons.RewardMsg;
import commons.ScoreMsg;
import commons.ScoreType;
import commons.VisibilityMsg;
import server.server_model;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class player {

	private Socket socket;
	private String name = "<new>";
	private server_model model;
	
	// Create counter for cards
	
	private int AnzahlMessages = 0;
	private int Wachturm = 0;
	private int Taverne = 0;
	private int Kaserne = 0;
	private int Muehle = 0;
	private int Brauerei = 0;
	private int Hexenhaus = 0;
	private int Schloss = 0;
	private int overallcount = 0;
	
	// Initiate saldo
	
	private int saldo = 0;
	
	private String turn = "false";
	private String erster = "false";
	private int position;
	// @ali String -> boolean
	private boolean complete = false;
	// @ali: mit -1 sieht man, ob der Spieler am Ende 0 Punkte hatte
	private int points = -1;
	
	// Initiate lazarett
	
	private Stack<String> lazarett = new Stack<String>();
	

	protected player(server_model model, Socket socket) {
		this.model = model;
		this.socket = socket;

		// Create thread to read incoming messages
		
		Runnable r = new Runnable() {
			public void run() {
				while(true) {		
					
					Message msg = Message.receive(socket);
					
					// Receive messages and check type
					
					if (msg instanceof ChatMsg) {	
														
						player.this.AnzahlMessages++;					
						model.broadcast((ChatMsg) msg);
									
						ChatMsg countmsg = new ChatMsg(player.this.name, "hat " + player.this.AnzahlMessages + " Nachrichten gesendet");
						model.broadcast(countmsg);
						
											
					} else if (msg instanceof JoinMsg) {
										
						// player.this.name = model.checknames(((JoinMsg) msg).getName());
						player.this.name = ((JoinMsg) msg).getName();
						model.broadcast((JoinMsg) msg);

						
					// Msg to remove Card from Stack and send new Stack
						
					} else if (msg instanceof CardTakenMsg){
						player.this.position = ((CardTakenMsg)msg).getposition();
						model.s1.removeCard(position);	
						
						//waiting for removing card
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
						e.printStackTrace();
						}
						
						CardStackMsg cardSmsg = new CardStackMsg(model.s1.getCard(1), model.s1.getCard(2), model.s1.getCard(3), model.s1.getCard(4), model.s1.getCard(5), model.s1.getCard(6));
						
						//Wait for GUI 
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
						e.printStackTrace();
						}
						
						model.broadcast(cardSmsg);
						
					} else if (msg instanceof ScoreMsg) {
						player.this.name = ((ScoreMsg) msg).getName();	
						
						int reward = 0;
						int rate = 0;
						int rewardall = 0;
						
							overallcount ++;
						if (overallcount < 13) {	
							
							// disable buttons 
							VisibilityMsg vismsg = new VisibilityMsg(player.this.name, "false");
							model.broadcast(vismsg);	
							
							// wait before calculate for GUI
							
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							e.printStackTrace();
							}
							
							// Check taken cards for type and send rewards
							
							if (((ScoreMsg) msg).getCard().equals("Wachturm")) {
								
								try {
									String brauereiMusicFile = "src/sounds/defense.wav";
									Media brauereiSound = new Media (new File(brauereiMusicFile).toURI().toString());
									MediaPlayer brauereiPlayer = new MediaPlayer(brauereiSound);
									brauereiPlayer.setVolume(0.2);
									brauereiPlayer.play();
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								reward = 0;
								rate = 2;
								
								player.this.Wachturm++;							
								model.broadcast((ScoreMsg) msg);
								
								// Get 2 coins for each Kaserne, Wachtrum and Taverne card
	
								reward += (player.this.getKaserne() * rate);
								reward += (player.this.getWachturm() * rate);
								reward += (player.this.getTaverne() * rate);
								player.this.saldo += reward;
							
								RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
								model.broadcast(rewardmsg);
							}
											
						
							if (((ScoreMsg) msg).getCard().equals("Brauerei")) {
								
								try {
									String brauereiMusicFile = "src/sounds/brewery.wav";
									Media brauereiSound = new Media (new File(brauereiMusicFile).toURI().toString());
									MediaPlayer brauereiPlayer = new MediaPlayer(brauereiSound);
									brauereiPlayer.setVolume(0.2);
									brauereiPlayer.play();
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								reward = 0;
								rate = 2;
								rewardall = 2;
														
								player.this.Brauerei++;
								model.broadcast((ScoreMsg) msg);
								
								// Get 2 coins for each Brauerei card
								
								reward += (player.this.getBrauerei() * rate);
								player.this.saldo += reward;
							
								RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
								model.broadcast(rewardmsg);
								
								// Get 2 coins for each player with at least one Muehle card
								
								List<String> players = model.getPlayersWithCard("Muehle");
														
									for(int i = 0; i < players.size(); i++){
										
										String[] parts = players.get(i).split("\\|");
										String temp1;
										rewardmsg.changeMsg(parts[0], rewardall, Integer.parseInt(parts[1])+rewardall);	
										parts[1] = "" + (Integer.parseInt(parts[1])+rewardall);
										temp1 = parts[0] + "|" + parts[1];
										
										model.setSaldi(temp1);	
										model.broadcast(rewardmsg);
										
									}
								}
								
							if (((ScoreMsg) msg).getCard().equals("Hexenhaus")) {
									
							try {
									String brauereiMusicFile = "src/sounds/witch.wav";
									Media brauereiSound = new Media (new File(brauereiMusicFile).toURI().toString());
									MediaPlayer brauereiPlayer = new MediaPlayer(brauereiSound);
									brauereiPlayer.setVolume(0.2);
									brauereiPlayer.play();
								} catch (Exception e) {
									e.printStackTrace();
								}
								
								reward = 0;
								rate = 2;
									
								player.this.Hexenhaus++;
								model.broadcast((ScoreMsg) msg);	
									
								// Get 2 coins for each Muehle, Brauerei and Hexenhaus card
	
								reward += (player.this.getMuehle() * rate);
								reward += (player.this.getBrauerei() * rate);
								reward += (player.this.getHexenhaus() * rate);
								player.this.saldo += reward;
									
								// Heal cards from lazarett
									
									try {
										if (lazarett.peek().equals("Muehle")) {
											ScoreMsg miller = new ScoreMsg((player.this.name = ((ScoreMsg) msg).getName()), "Muehle", "heal");
											player.this.Muehle++;
											lazarett.pop();
											model.broadcast((ScoreMsg) miller); // does this work?
										} 
										
										else if (lazarett.peek().equals("Brauerei")) {
											ScoreMsg beer = new ScoreMsg((player.this.name = ((ScoreMsg) msg).getName()), "Brauerei", "heal");
											player.this.Brauerei++;
											lazarett.pop();
											model.broadcast((ScoreMsg) beer);
										}
										
										else if (lazarett.peek().equals("Hexenhaus")) {
											ScoreMsg witch = new ScoreMsg((player.this.name = ((ScoreMsg) msg).getName()), "Hexenhaus", "heal");
											player.this.Hexenhaus++;
											lazarett.pop();
											model.broadcast((ScoreMsg) witch);
										}
										
										else if (lazarett.peek().equals("Wachturm")) {
											ScoreMsg defense = new ScoreMsg((player.this.name = ((ScoreMsg) msg).getName()), "Wachturm", "heal");
											player.this.Wachturm++;
											lazarett.pop();
											model.broadcast((ScoreMsg) defense);
										}		
	
										else if (lazarett.peek().equals("Kaserne")) {
											ScoreMsg attack = new ScoreMsg((player.this.name = ((ScoreMsg) msg).getName()), "Kaserne", "heal");
											player.this.Kaserne++;
											lazarett.pop();
											model.broadcast((ScoreMsg) attack);
										} 
										
										else if (lazarett.peek().equals("Taverne")) {
											ScoreMsg tavern = new ScoreMsg((player.this.name = ((ScoreMsg) msg).getName()), "Taverne", "heal");
											player.this.Taverne++;
											lazarett.pop();
											model.broadcast((ScoreMsg) tavern);
										}
										
										else if (lazarett.peek().equals("Schloss")){
											ScoreMsg castle = new ScoreMsg((player.this.name = ((ScoreMsg) msg).getName()), "Schloss", "heal");
											player.this.Schloss++;
											lazarett.pop();
											model.broadcast((ScoreMsg) castle);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}			
									
									RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
									model.broadcast(rewardmsg);		
								
								}
								
								if (((ScoreMsg) msg).getCard().equals("Muehle")) {
									
									try {
										String muehleMusicFile = "src/sounds/miller.wav";
										Media muehleSound = new Media (new File(muehleMusicFile).toURI().toString());
										MediaPlayer muehlePlayer = new MediaPlayer(muehleSound);
										muehlePlayer.play();
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									reward = 0;
									rate = 2;
									
									player.this.Muehle++;
									model.broadcast((ScoreMsg) msg);
									
									// get 2 coins for each Muehle
															
									reward += (player.this.getMuehle() * rate);
									player.this.saldo += reward;
									
									RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
									model.broadcast(rewardmsg);	
									
									}
								
								if (((ScoreMsg) msg).getCard().equals("Kaserne")) {
									
									try {
										String muehleMusicFile = "src/sounds/attack.wav";
										Media muehleSound = new Media (new File(muehleMusicFile).toURI().toString());
										MediaPlayer muehlePlayer = new MediaPlayer(muehleSound);
										muehlePlayer.play();
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									reward = 0;
									rate = 3;
									
									player.this.Kaserne++;
									model.broadcast((ScoreMsg) msg);
									
									// Attack players
									model.attackAll(player.this.Kaserne, player.this.name);	
									
									// get 3 coins for each Kaserne
									
									reward += (player.this.getKaserne() * rate);
									player.this.saldo += reward;
									
									RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
									model.broadcast(rewardmsg);	
								}
								
								if (((ScoreMsg) msg).getCard().equals("Schloss")) {
									
									try {
										String muehleMusicFile = "src/sounds/castle.wav";
										Media muehleSound = new Media (new File(muehleMusicFile).toURI().toString());
										MediaPlayer muehlePlayer = new MediaPlayer(muehleSound);
										muehlePlayer.play();
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									reward = 0;
									rate = 5;
									
									player.this.Schloss++;
									model.broadcast((ScoreMsg) msg);
									
									// get 5 coins for each Schloss
									
									reward += (player.this.getSchloss() * rate);
									player.this.saldo += reward;
									
									RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
									model.broadcast(rewardmsg);
																	
								}
								
									
								if (((ScoreMsg) msg).getCard().equals("Taverne")) {
									
									try {
										String muehleMusicFile = "src/sounds/tavern.wav";
										Media muehleSound = new Media (new File(muehleMusicFile).toURI().toString());
										MediaPlayer muehlePlayer = new MediaPlayer(muehleSound);
										muehlePlayer.play();
									} catch (Exception e) {
										e.printStackTrace();
									}
									
									reward = 0;
									rate = 4;
									rewardall = 3;
									
									player.this.Taverne++;
									model.broadcast((ScoreMsg) msg);
									
									// get 4 coins for each Taverne
									
									reward += (player.this.getTaverne() * rate);
									player.this.saldo += reward;
									
									RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
									model.broadcast(rewardmsg);	
									
									// get 3 coins for all players with at least one Brauerei
									
									List<String> players = model.getPlayersWithCard("Brauerei");
									
									for(int i = 0; i < players.size(); i++){
										
										String[] parts = players.get(i).split("\\|");
										String temp1;
										
										rewardmsg.changeMsg(parts[0], rewardall, Integer.parseInt(parts[1])+rewardall);	
										parts[1] = "" + (Integer.parseInt(parts[1])+rewardall);
										temp1 = parts[0] + "|" + parts[1];
										
										model.setSaldi(temp1);
										model.broadcast(rewardmsg);
									
									}
								}
							
								
								// Avoid overlapping messages 
								
								try {
									Thread.sleep(800);
								} catch (InterruptedException e) {
								e.printStackTrace();
								}
								
								// Enable change buttons
								
								model.changeTurn();
								
							} else {
								// TODO: disable buttons in all player windows
								player.this.complete = true;
								model.setComplete();
								model.changeTurn();
								
							}
						}
					
					}
				}			
		
		};
		Thread t = new Thread(r);
		t.start();
	}

	public void send(Message msg) {
		msg.send(socket);
	}
	
	// Getter
	
	public String getName() {
		return this.name;
	}
	
	public Integer getTaverne() {
		return this.Taverne;
	}
	
	public Integer getWachturm() {
		return this.Wachturm;
	}
	
	public Integer getSchloss() {
		return this.Schloss;
	}
	
	public Integer getHexenhaus() {
		return this.Hexenhaus;
	}
	
	public Integer getBrauerei() {
		return this.Brauerei;
	}
	
	public Integer getKaserne() {
		return this.Kaserne;
	}
	
	public Integer getMuehle() {
		return this.Muehle;
	}
	
	public Integer getSaldo() {
		return this.saldo;
	}
	
	public String getTurn() {
		return this.turn;
	}
	
	public String getErster() {
		return this.erster;
	}
	
	public boolean getComplete(){
		return this.complete;
	}

	
	// Setter
	
	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}
	
	public void setErster() {
		this.erster = "true";
	}
	
	public void setTurn() {
		this.turn = "true";
		
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
		
	// @Ali: grösse vom Stack entspricht der Anzahl Personen im Lazarett (Minuspunkte in der Auswertung)
	public int getLazarett() {
		return lazarett.size();
	}

	public String toString() {
		return name + ": " + socket.toString();
	}
	

	
	public void changeTurn() {
		if(this.turn.equals("true")){
			this.turn = "false";
		} else if (this.turn.equals("false")){
			this.turn = "true";
		}
		
		VisibilityMsg vismsg = new VisibilityMsg(player.this.name, this.turn);
		model.broadcast(vismsg);

	}
	
	public void attack(){
		
		// Go from left to right until a card is found
		
		if(player.this.Muehle > 0){
			player.this.Muehle--;
			lazarett.push("Muehle");
			
			ScoreMsg msg = new ScoreMsg(name, "Muehle", "attack");
			model.broadcast(msg);
						
		} else {
			if(player.this.Brauerei > 0){
				player.this.Brauerei--;
				lazarett.push("Brauerei");
				
				ScoreMsg msg = new ScoreMsg(name, "Brauerei", "attack");
				model.broadcast(msg);
				
			} else {
				if(player.this.Hexenhaus > 0){
					player.this.Hexenhaus--;
					lazarett.push("Hexenhaus");
					
					ScoreMsg msg = new ScoreMsg(name, "Hexenhaus", "attack");
					model.broadcast(msg);
					
				} else {
					if(player.this.Wachturm > 0){
						player.this.Wachturm--;
						lazarett.push("Wachturm");
						
						ScoreMsg msg = new ScoreMsg(name, "Wachturm", "attack");
						model.broadcast(msg);
	
					} else {
						if(player.this.Kaserne > 0){
							player.this.Kaserne--;
							lazarett.push("Kaserne");
							
							ScoreMsg msg = new ScoreMsg(name, "Kaserne", "attack");
							model.broadcast(msg);
							
						} else {
							if(player.this.Taverne > 0){
								player.this.Taverne--;
								lazarett.push("Taverne");
								
								ScoreMsg msg = new ScoreMsg(name, "Taverne", "attack");
								model.broadcast(msg);
								
							} else {
								if(player.this.Schloss > 0){
									player.this.Schloss--;
									lazarett.push("Schloss");
									
									ScoreMsg msg = new ScoreMsg(name, "Schloss", "attack");
									model.broadcast(msg);
								} 
							}
						}
					}
				}
			}
		}
	}

	public void stop() {
		try {
			socket.close();
		} catch (IOException e) {
		}
	}
	
}




