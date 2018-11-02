package commons;

import java.util.Stack;

public class Graveyard {

	public static void main(String[] args) {
		
		// THE WHOLE CLASS IS ABUSED AS AN EDITOR; THE CLASS ITSELF IS NOT WORKING
		
		
//			already existing in player-class
//		Stack<String> lazarett = new Stack<String>();

//			add new Card to the lazarett-Stack after an attack
//		this is done via attack-method

//		remove topmost Card from Stack and put it back to the original location
//		the following code can be added into the player class on around line 159 (getcard().equals("hexenhaus")
		
		
//		comparing the topmost card on the lazarett Stack for its String-value
//		adding the corresponding card back to its origin
		
		try {
			if (lazarett.peek().equals("Muehle")) {
				player.this.Muehle++;
				lazarett.pop();
			} 
			else if (lazarett.peek().equals("Brauerei")) {
				player.this.Brauerei++;
				lazarett.pop();
			}
			else if (lazarett.peek().equals("Hexenhaus")) {
				player.this.Hexenhaus++;
				lazarett.pop();
			}
			else if (lazarett.peek().equals("Wachturm")) {
				player.this.Wachturm++;
				lazarett.pop();
			}		
			else if (lazarett.peek().equals("Kaserne")) {
				player.this.Kaserne++;
				lazarett.pop
			} 
			else if (lazarett.peek().equals("Taverne")) {
				player.this.Taverne++;
				lazarett.pop();
			}
			else if (lazarett.peek().equals("Schloss")){
				player.this.Schloss++;
				lazarett.pop();
			}

			// was muss do förne message hin ond här gschobe wärde demit beidi clients chegge was abghot?
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}