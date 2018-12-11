package commons;

import java.net.Socket;
import java.util.ArrayList;

import java.util.Collections;
import server.server_model;

/**
 * Class to generate the entire Stack for the game and shuffle it at the beginning. It will not be shuffled during the game.
 * The Cards will be saved in a Arraylist to avoid any "empty" Index.
 * @author E. Thammavongsa
 */
public class CardStack {
	
	private ArrayList<Card> stack = new ArrayList<>();
	private Socket socket;
	private server_model model;
	private int[] Frequency = {9,6,5,5,3,4,5};
	
	//Consturctor to create the Card stack with the default Frequency.
	public CardStack() {
		int counter = 0;
		for (CardType cards: CardType.values()) {			
			for(int i = 0; i < Frequency[counter]; i++){
				stack.add(new Card(cards));					
			}
			counter++;
		}
		
		this.shuffleStack();
	}	
	
	
	public ArrayList<Card> getCardStack() {
		return stack;
	}
	
	//Shuffle the generated Stack
	public void shuffleStack() {
		
		ArrayList<Card> tempStack = new ArrayList<>();
		
		for (Card card: stack) {
			tempStack.add(card);
		}
		
		Collections.shuffle(tempStack);
		
		this.stack = tempStack;
		
	}	

	
	public int size() {
		return stack.size();
	}
	
	
	public String toString() {
		String results = "";
	
		for(int i = 0; i < stack.size(); i++){
			results += stack.get(i);			
		}
		return results;
	}
	
	//Get Card Index for the Card which will be removed. The game send the index of the card 0-5 and the specific Card will be removed from the stack
	public Card getCardIndex(int position) {
		int temp = position;
		Card A = this.stack.get(temp - 1);
		
		return A;
	}
	
	public String getCard(int position) {
		Card CardTemp = this.stack.get(position);
		String Cardname = CardTemp.toString();
		return Cardname;
	}
	
	//Remove Card from Stack
	public void removeCard(int position) {
		ArrayList<Card> tempStack = this.stack;		
		tempStack.remove(position);		
		this.stack = tempStack;
		
	}
	

	public Card get(int i) {
		return this.stack.get(i);
	}
	
}
