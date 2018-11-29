package commons;

public class WinnerMsg extends Message {
	private String winner;
	private String loser;
	
	public WinnerMsg(String winner) {
		super(MessageType.Winner);
		this.winner = winner;		
		
	}
		
	public String getWinner() {
		return this.winner;
	}
	
	public String getLoser() {
		return this.loser;
	}
	
	public String toString() {
		return type.toString() + '|' + winner + '|' + loser;
	}
			
}