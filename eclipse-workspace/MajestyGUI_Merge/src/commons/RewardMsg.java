package commons;

/**
 * Message to distribute coins 
 * @author P. Mächler
 */
public class RewardMsg extends Message {
	private String name;
	private int reward;
	private int saldo;
	
	public RewardMsg(String name, int reward, int saldo) {
		super(MessageType.Reward);
		this.name = name;
		this.reward = reward;
		this.saldo = saldo;
	}

	public String getName() {
		return name;
	}
	
	public void changeMsg(String name, int reward, int saldo){
		this.name = name;
		this.reward = reward;
		this.saldo = saldo;
	}

	public int getReward() {
		return reward;
	}
	
	public String toString() {
		return type.toString() + '|' + name + '|' + reward + '|' + saldo;
	}
	
	public void setReward(int reward) {
		this.reward = reward;
	}
	
	public Integer getSaldo() {
		return saldo;
	}
	
	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}
	
		
}