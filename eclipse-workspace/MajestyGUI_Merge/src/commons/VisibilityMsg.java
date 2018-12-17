package commons;

/**
 * Message to change the visibility of buttons
 * @author P.Mächler
 */	
public class VisibilityMsg extends Message {
	private String name;
	private String turn;
	
	public VisibilityMsg(String name, String turn) {
		super(MessageType.Visibility);
		this.name = name;
		this.turn = turn;
	}

	public String toString() {
		return type.toString() + '|' + name + '|' + turn;
	}

	public String getName() {
		return this.name;
	}
	
	public String getVisibility(){
		return this.turn;
	}
		
}