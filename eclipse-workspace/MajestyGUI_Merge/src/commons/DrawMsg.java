package commons;


/**
 * Last Message after defining the Winner and the Loser
 * @author E. Thammavongsa
 */
public class DrawMsg extends Message {
	private String player1;
	private String player2;
	
	public DrawMsg(String draw1, String draw2) {
		super(MessageType.Winner);
		this.player1 = draw1;
		this.player2 = draw2;
		
	}
	
	public String getDraw1(){
		return this.player1;
	}
	
	public String getDraw2(){
		return this.player2;
	}
		

	public String toString() {
		return type.toString() + '|' + player1 + '|' + player2;
	}
			
}
