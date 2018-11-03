package commons;

import java.net.Socket;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import server.server_model;

public class CardStack {
	
	private ArrayList<Card> stack = new ArrayList<>();
	private Socket socket;
	private server_model model;
	
	public CardStack() {
		for (CardType cards: CardType.values()) {
			for(int i = 0; i<4; i++){
				stack.add(new Card(cards));					
			}			
		}
		
		this.shuffleStack();
	}
	

	
	
	
	public ArrayList<Card> getCardStack() {
		return stack;
	}
	
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
		
//		for(Card card : stack) {
//			results = results + card.toString() + "|";
	
		for(int i = 0; i < stack.size(); i++){
			results += stack.get(i);			
		}
		return results;
	}
	
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
	
	public void removeCard(int position) {
		ArrayList<Card> tempStack = this.stack;		
		tempStack.remove(position);		
		this.stack = tempStack;
		
	}
	

	public Card get(int i) {
		return this.stack.get(i);
	}
	
}
