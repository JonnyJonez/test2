package server;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Stack;

import commons.JoinMsg;
import commons.CardStackMsg;
import commons.CardTakenMsg;
import commons.ChatMsg;
import commons.Message;
import commons.RewardMsg;
import commons.ScoreMsg;
import commons.VisibilityMsg;
import server.server_model;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author P.Mächler
 * @author E.Thammavongsa
 * @author R.Thiel
 *
 */
public class player {

	private Socket socket;
	private String name = "<new>";
	private server_model model;
	
	// Create counter for cards
	
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
	
	private boolean complete = false;
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
						
						/**
						 * Identify and broadcast chat message, increase counter						 
						 * @author P.Mächler
						 */				
						model.broadcast((ChatMsg) msg);
											
					} else if (msg instanceof JoinMsg) {
						
						/**
						 * Identify and broadcast join message
						 * @author P.Mächler
						 */

						player.this.name = ((JoinMsg) msg).getName();
						model.broadcast((JoinMsg) msg);
						
					} else if (msg instanceof CardTakenMsg){
						
						/**
						 * Identify the Taken Card and remove it from the Stack. Send Broadcast with the new Cards.
						 * @author E.Thammavongsa
						 */
						player.this.position = ((CardTakenMsg)msg).getposition();
						model.s1.removeCard(position);	
						
						// waiting for removing card
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
						
					} 
					/**
					 * handling of different messages 
					 * @author: P.Mächler
					 */	
					else if (msg instanceof ScoreMsg) {
						
						player.this.name = ((ScoreMsg) msg).getName();	
						
						int reward = 0;
						int rate = 0;
						int rewardall = 0;
									
						if (overallcount < 24) {	
							
							VisibilityMsg vismsgOne = new VisibilityMsg(model.erster, "false");
							VisibilityMsg vismsgTwo = new VisibilityMsg(model.zweiter, "false");
							model.broadcast(vismsgOne);
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							e.printStackTrace();
							}
							model.broadcast(vismsgTwo);
							
							// wait before calculate for GUI
							
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							e.printStackTrace();
							}
							
							// Check taken cards for type and send rewards
																			
							if (((ScoreMsg) msg).getCard().equals("Wachturm")) {
								
					
								try {
									defenseSound();
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
								

//								 Calculate reward for a taken "Brauerei" card. Send Reward message to the player
//								 Identify all player with at least one "Mühle" card and send a reward message for those
								
								try {
									brewerySound();
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
								witchSound();
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
									
								/**
								 * whenever a card with the name "Hexenhaus" is picked, it will be checked,
								 * if the lazarett is empty. If there is at least one card inside of the lazarett,
								 * the following code will check which one it is based on a if peek = x = true -> pop  
								 * @author R.Thiel
								 */
								if (lazarett.empty() == true) {
								
								}
								
								else try {
									
										if (lazarett.peek().equals("Muehle")) {
											ScoreMsg miller = new ScoreMsg((player.this.name = ((ScoreMsg) msg).getName()), "Muehle", "heal");
											player.this.Muehle++;
											lazarett.pop();
											model.broadcast((ScoreMsg) miller);
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
									
									/**
									 * Calculate reward for a taken "Mühle" card. Send Reward message to the player
									 * @author P.Mächler
									 */
									
									try {
										millerSound();
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
									
									/**
									 * Calculate reward for a taken "Kaserne" card. Send Reward message to the player
									 * Attack other players
									 * @author P.Mächler
									 */
									
									try {
										attackSound();
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
									
									/**
									 * Calculate reward for a taken "Schloss" card. Send Reward message to the player
									 * @author P.Mächler
									 */
									
									try {
										castleSound();
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
									
									/**
									 * Calculate reward for a taken "Taverne" card. Send Reward message to the player
									 * Identify all player with at least one "Brauerei" card and send a reward message for those
									 * @author P.Mächler
									 */
									
									try {
										tavernSound();
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
	
	public int getOverallcount() {
		return overallcount;
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

	public void setOverallcount(int overallcount) {
		this.overallcount = overallcount;
	}

	public int getLazarett() {
		return lazarett.size();
	}

	public String toString() {
		return name + ": " + socket.toString();
	}
	
	/**
	 * this method is called after a turn was done. It sends a visibility message to disable or enable the buttons
	 * @author P.Mächler
	 */
	public void changeTurn() {
		
		overallcount++; 
		
		if(this.turn.equals("true")){
			this.turn = "false";
		} else if (this.turn.equals("false")){
			this.turn = "true";
		}
		
		VisibilityMsg vismsg = new VisibilityMsg(player.this.name, this.turn);
		model.broadcast(vismsg);

	}
	
	/**
	 * this method is called when an attack starts (less defense that attackers)
	 * @author R.Thiel
	 */
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
	
	/**
	 * this method is called when a card is picked with the "Wachturm"-name. 
	 * it will play a predetermined sound with 20% volume.  
	 * The original sound file is from the MMORPG "World of Warcraft"
	 * @author R.Thiel
	 */
	public void defenseSound(){ 
	String wachturmMusicFile = "src/sounds/defense.wav";
	Media wachturmSound = new Media (new File(wachturmMusicFile).toURI().toString());
	MediaPlayer wachturmPlayer = new MediaPlayer(wachturmSound);
	wachturmPlayer.setVolume(0.2);
	wachturmPlayer.play();
	}
	
	/**
	 * this method is called when a card is picked with the "Brauerei"-name. 
	 * it will play a predetermined sound with 20% volume.  
	 * The original sound file was taken from a website who distributes free sample sounds. 
	 * @author R.Thiel
	 */
	public void brewerySound() {
	String brauereiMusicFile = "src/sounds/brewery.wav";
	Media brauereiSound = new Media (new File(brauereiMusicFile).toURI().toString());
	MediaPlayer brauereiPlayer = new MediaPlayer(brauereiSound);
	brauereiPlayer.setVolume(0.2);
	brauereiPlayer.play();
	}

	/**
	 * this method is called when a card is picked with the "Hexenhaus"-name. 
	 * it will play a predetermined sound with 20% volume. 
	 * The original sound file was taken from a website who distributes free sample sounds. 
	 * @author R.Thiel
	 */
	public void witchSound() {
	String witchMusicFile = "src/sounds/witch.wav";
	Media witchSound = new Media (new File(witchMusicFile).toURI().toString());
	MediaPlayer witchPlayer = new MediaPlayer(witchSound);
	witchPlayer.setVolume(0.2);
	witchPlayer.play();
	}
	
	/**
	 * this method is called when a card is picked with the "Mühle"-name. 
	 * it will play a predetermined sound with 20% volume.  
	 * The original sound file is from the RTS "Age of Empires II"
	 * @author R.Thiel
	 */
	public void millerSound() {
	String muehleMusicFile = "src/sounds/miller.wav";
	Media muehleSound = new Media (new File(muehleMusicFile).toURI().toString());
	MediaPlayer muehlePlayer = new MediaPlayer(muehleSound);
	muehlePlayer.setVolume(0.2);
	muehlePlayer.play();
	}
	
	/**
	 * this method is called when a card is picked with the "Kaserne"-name. 
	 * it will play a predetermined sound with 20% volume. 
	 * The original sound file is from the MMORPG "World of Warcraft"
	 * @author R.Thiel
	 */
	public void attackSound() {
	String attackMusicFile = "src/sounds/attack.wav";
	Media attackSound = new Media (new File(attackMusicFile).toURI().toString());
	MediaPlayer attackPlayer = new MediaPlayer(attackSound);
	attackPlayer.setVolume(0.2);
	attackPlayer.play();
	}
	
	/**
	 * this method is called when a card is picked with the "Schloss"-name. 
	 * it will play a predetermined sound with 20% volume. 
	 * The original sound file is from the RTS "Age of Empires II"
	 * @author R.Thiel
	 */
	public void castleSound() {
	String castleMusicFile = "src/sounds/castle.wav";
	Media castleSound = new Media (new File(castleMusicFile).toURI().toString());
	MediaPlayer castlePlayer = new MediaPlayer(castleSound);
	castlePlayer.setVolume(0.2);
	castlePlayer.play();
	}
	
	/**
	 * this method is called when a card is picked with the "Taverne"-name. 
	 * it will play a predetermined sound with 20% volume. 
	 * The original sound file was taken from a website who distributes free sample sounds. 
	 * @author R.Thiel
	 */
	public void tavernSound() {
	String tavernMusicFile = "src/sounds/tavern.wav";
	Media tavernSound = new Media (new File(tavernMusicFile).toURI().toString());
	MediaPlayer tavernPlayer = new MediaPlayer(tavernSound);
	tavernPlayer.setVolume(0.2);
	tavernPlayer.play();
	}
	
	
	public void stop() {
		try {
			socket.close();
		} catch (IOException e) {
		}
	}
	
}




