package commons;

public class MeepleMsg extends Message {
		
		private int MeeplesCount;
		private String playername;
			
			public MeepleMsg(String name, int meeple) {
				super(MessageType.Meeples);
				this.playername = name;
				this.MeeplesCount = meeple;
			}

			public int getMeeples() {
				return MeeplesCount;
			}
			
			public String getname() {
				return playername;
			}
			
			@Override
			public String toString() {
				return type.toString() + '|' + playername + '|' + MeeplesCount;
			}
}
