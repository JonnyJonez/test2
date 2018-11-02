package commons;

public class JoinMsg extends Message {
	private String name;
	
	public JoinMsg(String name) {
		super(MessageType.Join);
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return type.toString() + '|' + name;
	}
}
