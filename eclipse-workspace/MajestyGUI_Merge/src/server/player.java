package server;

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
import commons.VisibilityMsg;
import server.server_model;


public class player {

	private Socket socket;
	private String name = "<new>";
	private server_model model;
	private int AnzahlMessages = 0;
	private int Wachturm = 0;
	private int Taverne = 0;
	private int Kaserne = 0;
	private int Muehle = 0;
	private int Brauerei = 0;
	private int Hexenhaus = 0;
	private int Schloss = 0;
	private int saldo = 0;
	private String turn = "false";
	private String erster = "false";
	private int position;
	private Stack<String> lazarett = new Stack<String>();
	

	protected player(server_model model, Socket socket) {
		this.model = model;
		this.socket = socket;

		// Create thread to read incoming messages
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				while(true) {			
					Message msg = Message.receive(socket);
					
					// Messages empfangen und auf Typ überprüfen
					
					if (msg instanceof ChatMsg) {	
														
						player.this.AnzahlMessages++;					
						model.broadcast((ChatMsg) msg);
						
						if (((ChatMsg) msg).getContent().equals("Hallo")) {
							((ChatMsg) msg).setContent("3 Coins erhalten");	
						model.broadcast((ChatMsg) msg); }
						
						ChatMsg countmsg = new ChatMsg(player.this.name, "hat " + player.this.AnzahlMessages + " Nachrichten gesendet");
						model.broadcast(countmsg);
						
											
					} else if (msg instanceof JoinMsg) {
						player.this.name = model.checknames(((JoinMsg) msg).getName());
						model.broadcast((JoinMsg) msg);
					//Msg to remove Card from Stack and send new Stack
					} else if (msg instanceof CardTakenMsg){
						player.this.position = ((CardTakenMsg)msg).getposition();
						model.s1.removeCard(position);			
							
						CardStackMsg cardSmsg = new CardStackMsg(model.s1.getCard(1), model.s1.getCard(2), model.s1.getCard(3), model.s1.getCard(4), model.s1.getCard(5), model.s1.getCard(6));
						model.broadcast(cardSmsg);
							
						
						
						
					} else if (msg instanceof ScoreMsg) {
						player.this.name = ((ScoreMsg) msg).getName();	
						
						int reward = 0;
						int rate = 0;
						int rewardall = 0;
						
						// disable buttons 
						VisibilityMsg vismsg = new VisibilityMsg(player.this.name, "false");
						model.broadcast(vismsg);	
						
						// wait before calculate for GUI
						
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
						e.printStackTrace();
						}
						
						// Auf Typen überprüfen
						
						if (((ScoreMsg) msg).getCard().equals("Wachturm")) {
							
							player.this.Wachturm++;							
							model.broadcast((ScoreMsg) msg);

							reward += (player.this.getKaserne() * 2);
							reward += (player.this.getWachturm() * 2);
							reward += (player.this.getTaverne() * 2);
							player.this.saldo += reward;
						
							RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
							model.broadcast(rewardmsg);
						}
										
					
						if (((ScoreMsg) msg).getCard().equals("Brauerei")) {
							
							reward = 0;
							rate = 2;
							rewardall = 2;
													
							player.this.Brauerei++;
							model.broadcast((ScoreMsg) msg);
							
							reward += (player.this.getBrauerei() * rate);
							player.this.saldo += reward;
						
							RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
							model.broadcast(rewardmsg);
							
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
								
								rate = 2;
								
								player.this.Hexenhaus++;
								model.broadcast((ScoreMsg) msg);	
								
								reward += (player.this.getMuehle() * rate);
								reward += (player.this.getBrauerei() * rate);
								reward += (player.this.getHexenhaus() * rate);
								player.this.saldo += reward;
								
								RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
								model.broadcast(rewardmsg);		
							
							}
							
							if (((ScoreMsg) msg).getCard().equals("Muehle")) {
								
								reward = 0;
								rate = 2;
								
								player.this.Muehle++;
								model.broadcast((ScoreMsg) msg);
														
								reward += (player.this.getMuehle() * rate);
								player.this.saldo += reward;
								
								RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
								model.broadcast(rewardmsg);	
								
								}
							
							if (((ScoreMsg) msg).getCard().equals("Kaserne")) {
								
								reward = 0;
								rate = 3;
								
								player.this.Kaserne++;
								model.broadcast((ScoreMsg) msg);
								
								model.attackAll(player.this.Kaserne, player.this.name);	
								
								reward += (player.this.getKaserne() * rate);
								player.this.saldo += reward;
								
								RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
								model.broadcast(rewardmsg);	
							}
							
							if (((ScoreMsg) msg).getCard().equals("Schloss")) {
								
								reward = 0;
								rate = 5;
								
								player.this.Schloss++;
								model.broadcast((ScoreMsg) msg);
								
								reward += (player.this.getSchloss() * rate);
								player.this.saldo += reward;
								
								RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
								model.broadcast(rewardmsg);
																
							}
							
								
							if (((ScoreMsg) msg).getCard().equals("Taverne")) {
								
								reward = 0;
								rate = 4;
								rewardall = 3;
								
								player.this.Taverne++;
								model.broadcast((ScoreMsg) msg);
								
								reward += (player.this.getTaverne() * rate);
								player.this.saldo += reward;
								
								RewardMsg rewardmsg = new RewardMsg(player.this.name, reward, player.this.saldo);
								model.broadcast(rewardmsg);	
								
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
							
							
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
							e.printStackTrace();
							}
							
							// Enable change buttons
							
							model.changeTurn();
							
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
	
	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}
	
	public void stop() {
		try {
			socket.close();
		} catch (IOException e) {
			// Uninteresting
		}
	}

	public String toString() {
		return name + ": " + socket.toString();
	}
	
	public String getTurn() {
		return this.turn;
	}
	
	public void changeTurn() {
		if(this.turn.equals("true")){
			this.turn = "false";
		} else if (this.turn.equals("false")){
			this.turn = "true";
		}		
		VisibilityMsg vismsg = new VisibilityMsg(player.this.name, this.turn);
		model.broadcast(vismsg);
		
//		CardStackMsg stackmsg = new CardStackMsg(this.position, this.card.toString());
//		model.broadcast(stackmsg);
	}

	public void setErster() {
		this.erster = "true";
	}
	
	public String getErster() {
		return this.erster;
	}

	public void setTurn() {
		this.turn = "true";
		
	}
	
	public void attack(){
		
	//	boolean found = false;
	//	int i = 0;
	//	Integer[] searchArray = new Integer[7];
	//	searchArray[0] = player.this.Muehle;
	//	searchArray[1] = "Brauerei";
	//	searchArray[2] = "Hexenhaus";
	//	searchArray[3] = "Wachturm";
	//	searchArray[4] = "Taverne";
	//	searchArray[6] = "Schloss";
	//	
	//	while (i < searchArray.length && found == false){
	//			if(player.this. > 0){
	//				
	//			}
	//		}
	//	
		
		if(player.this.Muehle > 0){
			player.this.Muehle--;
			lazarett.push("Muehle");
			
			Message msg = new ChatMsg(name, "Minus Mühle");
			msg.send(socket);
						
		} else {
			if(player.this.Brauerei > 0){
				player.this.Brauerei--;
				lazarett.push("Brauerei");
				
				Message msg = new ChatMsg(name, "Minus Mühle");
				msg.send(socket);
				
			} else {
				if(player.this.Hexenhaus > 0){
					player.this.Hexenhaus--;
					lazarett.push("Hexenhaus");
					
					Message msg = new ChatMsg(name, "Minus Hexenhaus");
					msg.send(socket);
					
				} else {
					if(player.this.Wachturm > 0){
						player.this.Wachturm--;
						lazarett.push("Wachturm");
						
						Message msg = new ChatMsg(name, "Minus Wachturm");
						msg.send(socket);
	
					} else {
						if(player.this.Kaserne > 0){
							player.this.Kaserne--;
							lazarett.push("Kaserne");
							
							Message msg = new ChatMsg(name, "Minus Kaserne");
							msg.send(socket);
							
						} else {
							if(player.this.Taverne > 0){
								player.this.Taverne--;
								lazarett.push("Taverne");
								
								Message msg = new ChatMsg(name, "Minus Taverne");
								msg.send(socket);
								
							} else {
								if(player.this.Schloss > 0){
									player.this.Schloss--;
									lazarett.push("Schloss");
									
									Message msg = new ChatMsg(name, "Minus Schloss");
									msg.send(socket);
								} 
							}
						}
					}
				}
			}
		}
	}
	

}





