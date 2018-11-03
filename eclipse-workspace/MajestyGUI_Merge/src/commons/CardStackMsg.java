package commons;

public class CardStackMsg extends Message {
	public CardStack stack;
	private String card1;
	private String card2;
	private String card3;
	private String card4;
	private String card5;
	private String card6;
	
	
	
		
	public CardStackMsg(String card1, String card2, String card3, String card4, String card5, String card6) {
		super(MessageType.CardStack);

		this.card1 = card1;
		this.card2 = card2;
		this.card3 = card3;
		this.card4 = card4;
		this.card5 = card5;
		this.card6 = card6;
		
	}
	
	public CardStack getCardStack() {
		return stack;
	}
	
//	public int getPositions(){
//		return this.position1;		
//	}
//	
	public String getCard1(){
		return this.card1;
	}
	public String getCard2(){
		return this.card2;
	}
	public String getCard3(){
		return this.card3;
	}
	public String getCard4(){
		return this.card4;
	}
	public String getCard5(){
		return this.card5;
	}
	public String getCard6(){
		return this.card6;
	}
	
	
	public String toString() {
		
		return type.toString() + '|' + this.card1 + '|' + this.card2 + '|' + this.card3 + '|' + this.card4 + '|' + this.card5 + '|' + this.card6;
	}

}
