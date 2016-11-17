
public class Processor extends Thread {
	
	/**
	 * The WriteBuffer of the Processor
	 */
	public WriteBuffer wBuffer;
	/**
	 * The MemoryAgent of the Processor
	 */
	public MemoryAgent mAgent;
	/**
	 * The MainMemory that the processor uses
	 */
	public MainMemory mMemory;
	
	public static final int N = 10;
	
	public static int turn;
	public boolean[] flag = new boolean[N];
	
	private int i = 0;
	
	/**
	 * Processor constructor.
	 * @param TSO Whether the WriteBuffer is TSO or not.
	 * @param inputMainMemory The MainMemory that the processor will use.
	 */
	public Processor(boolean TSO, MainMemory inputMainMemory){
		mMemory = inputMainMemory;
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
