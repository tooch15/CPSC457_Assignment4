
public class Processor extends Thread {
	
	public WriteBuffer wBuffer;
	public MemoryAgent mAgent;
	
	public static final int N = 10;
	
	public static int turn;
	public boolean[] flag = new boolean[N];
	
	private int i = 0;
	
	
	
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
