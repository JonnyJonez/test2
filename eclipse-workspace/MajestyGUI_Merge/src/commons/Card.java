package commons;


public class Card {
	
	private CardType karte;

	public Card() {
		
	}
	
	public Card(CardType cardtyp) {
		this.karte = cardtyp;
	}
	
	
	
	public CardType getCard() {
		return karte;
	}
	
	public String toString() {
		return "" + karte;
	}
	
	public Card StringtoCard(String StrCard) {
		for(CardType types: CardType.values()) {
			if(StrCard.equals(types.toString())) {
				Card A = new Card(types);
				return A;
			} 
				
			
		}
		return null;
		
	}

}
