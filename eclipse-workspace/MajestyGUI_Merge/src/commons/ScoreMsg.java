package commons;

/**
 * Message to handle attacks and taken cards
 * @author P. M�chler
 */	

public class ScoreMsg extends Message {
	private String card;
	private String name;
	private String Scoretype;
	
	public ScoreMsg(String name, String card, String type) {
		super(MessageType.Score);
		this.card = card;
		this.name = name; 
		this.Scoretype = type;
		
	}

	public String getCard() {
		return card;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return type.toString() + '|' + this.name + '|'  + this.card + '|' + this.Scoretype ;
	}
	
	public String getScoreType() {
		return this.Scoretype;
	}
}