package commons;

public class CardStackMsg extends Message {
	public CardStack stack;
	private int position;
	private String card;
	
	
	//public CardStackMsg(CardStack Stack) {
		
	public CardStackMsg(int position, String card) {
		super(MessageType.CardStack);

		this.position = position;
		this.card = card;
		
	}
	
	public CardStack getCardStack() {
		return stack;
	}
	
	public int getPosition(){
		return this.position;		
	}
	
	public String getCard(){
		return this.card;
	}
	
	
	public String toString() {
		
		return type.toString() + '|' + this.position + '|' + this.card;
	}

}
