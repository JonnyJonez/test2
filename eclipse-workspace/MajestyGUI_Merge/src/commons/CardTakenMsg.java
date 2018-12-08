package commons;

/**
 * Client to Server Message to point to the taken Card, which will be removed from the CardStack
 * @author E. Thammavongsa
 */
public class CardTakenMsg extends Message{
	private int positionofCard;
		
	public CardTakenMsg(int position){
		super(MessageType.CardTakenMsg);
		this.positionofCard = position;
	}

	public int getposition() {
		return positionofCard;
	}
	
	public String toString() {
		
		return type.toString() + '|' + this.positionofCard;
	}
	


}
