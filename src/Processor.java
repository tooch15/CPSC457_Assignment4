
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
	
	/**
	 * The number of the processor, for Peterson's algorithm
	 */
	private int processorNumber;
	
	/**
	 * The number of processors, for Peterson's algorithm.
	 */
	private static int numberOfProcessors;
	
	/**
	 * The turn variable at each level, for Peterson's algorithm..
	 */
	public static int[] turn;

	/**
	 * The flag array, for Peterson's algorithm
	 */
	private static int[] flag;
	
	
	/**
	 * Processor constructor.
	 * @param TSO Whether the WriteBuffer is TSO or not.
	 * @param inputMainMemory The MainMemory that the processor will use.
	 */
	public Processor(boolean TSO, MainMemory inputMainMemory, int inputProcessorNumber){
		mMemory = inputMainMemory;
		wBuffer = new WriteBuffer(TSO);
		mAgent = new MemoryAgent(wBuffer, mMemory);
		processorNumber = inputProcessorNumber;
	}
	
	/**
	 * Sets the number of processors (equivalent to processes) for Peterson's algorithm
	 * @param numProcessors The number of processors in use. 
	 */
	public void setUpPetersonsAlgorithm(int numProcessors){
		numberOfProcessors = numProcessors;
		flag = new int[numberOfProcessors]; //Initialized to 0
		turn = new int[numberOfProcessors - 1];
	}
	
	
	public void run() {
		
	}
	
	
	/**
	 * Call before running <Critical Section> code. Must call PetersonsAlgorithmExitSection after the 
	 * <Critical Section> is executed.
	 */
	public void petersonAlgorithmEntrySection() {
		//<Entry Section>
		for (int processLevel = 0; processLevel < numberOfProcessors - 1; processLevel++ ){
			//Indicate that this process is competing at level processLevel
			flag[processorNumber] = processLevel;
			
			//Indicate that it is this process's turn (to wait) at level K 
			turn[processLevel] = processorNumber;
			
			//Check to see if there are processors competing at a higher level 
			//and that it is this processor's turn to wait.
			//If there are no processes at a higher level, or it is no longer this processor's turn, move on
			while(!checkForHigherProcessors(processLevel) && turn[processLevel] == processorNumber);
			
		}
		//Process can now enter <Critical Section>
		
	}
	
	/**
	 * Call after process exits <Critical Section> code
	 */
	public void PetersonsAlgorithmExitSection(){
		flag[processorNumber] = -1;
	}
	
	
	/**
	 * Returns true if there is a processor competing at a higher (or equal) level as the current process
	 * @param processLevel the level that the processor is currently competing at
	 * @return true or false
	 */
	private boolean checkForHigherProcessors(int processLevel) {
		
		for(int otherProcessor = 0; otherProcessor < numberOfProcessors; otherProcessor++){
			//Skip if the process being checked is this process
			if(otherProcessor == processorNumber){
				continue;
			}
			
			//If the other processor is at the same or a higher level than the current one, wait.
			if(flag[otherProcessor] >= processLevel){
				return true;
			}
		}
		
		//If no other processor is competing at the same or a higher level return false.
		return false;
	}
	

}
