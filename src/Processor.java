
public class Processor extends Thread {
	
	public WriteBuffer wBuffer;
	public MemoryAgent mAgent;
	public MainMemory mMemory;
	
	public static final int N = 10;
	
	public static int turn;
	public boolean[] flag = new boolean[N];
	
	private int i = 0;
	
	public Processor(boolean TSO, MainMemory mMemory){
		this.mMemory = mMemory;
		wBuffer = new WriteBuffer(TSO);
		mAgent = new MemoryAgent(wBuffer, mMemory);
	}
	
	
	
	public void run() {
		
	}
	
	public void PetersonAlgorithm() {
		
		int index = i;
		
		flag[i] = true;
		
		i = (i + 1) % N;
		
		turn = i;
		
		while (flag[index] && turn != index); // busy wait
		
		// critical section
		
		flag[index] = false;
		
		
	}
	

}
