package commons;

public class ScoreMsg extends Message {
	private String card;
	private String name;
	
	public ScoreMsg(String name, String card) {
		super(MessageType.Score);
		this.card = card;
		this.name = name; 
	}

	public String getCard() {
		return card;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return type.toString() + '|' + this.name + '|'  + this.card;
	}
}