package commons;

/**
 * @author E.Thammavongsa
 */
public class Card {
	
	private CardType card;

	public Card() {
		
	}
	
	public Card(CardType cardtyp) {
		this.card = cardtyp;
	}
	
	
	
	public CardType getCard() {
		return card;
	}
	
	public String toString() {
		return "" + card;
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
