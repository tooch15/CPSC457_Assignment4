
public class ProcessorWithoutBuffer extends Thread{
	/**
	 * The MainMemory that the processor uses
	 */
	public MainMemory mMemory;
	
	/**
	 * an object to test if 2 threads are in the CS at once
	 */
	public CSInteger csInteger;
	
	/**
	 * The number of the processor, for Peterson's algorithm
	 */
	private int processorNumber;
	
	/**
	 * The number of processors, for Peterson's algorithm.
	 */
	private int numberOfProcessors;
	
	/**
	 * The name of the flag variable for this processor
	 */
	String flagVarName;
	
	/**
	 * Processor constructor.
	 * @param TSO Whether the WriteBuffer is TSO or not.
	 * @param inputMainMemory The MainMemory that the processor will use.
	 */
	public ProcessorWithoutBuffer(boolean TSO, MainMemory inputMainMemory, int inputProcessorNumber, int inputNumberOfProcessors, 
			CSInteger inputCSInteger){
		mMemory = inputMainMemory;
		processorNumber = inputProcessorNumber;
		flagVarName = "flag" + processorNumber;
		numberOfProcessors = inputNumberOfProcessors;
		csInteger = inputCSInteger;
	}
	
	public void run() {
		
		for(int i = 0; i < 100; i++) {	
			
			petersonAlgorithmEntrySection();
			
			criticalSection();
			
			PetersonsAlgorithmExitSection();
		}
		
	}
	
	
	/**
	 * Call before running <Critical Section> code. Must call PetersonsAlgorithmExitSection after the 
	 * <Critical Section> is executed.
	 */
	public void petersonAlgorithmEntrySection() {
		//<Entry Section>
		for (int processLevel = 0; processLevel < numberOfProcessors - 1; processLevel++ ){
			//Indicate that this process is competing at level processLevel
			
			mMemory.store(flagVarName, processLevel); //flag[processorNumber] = processLevel;
			String turnVarName = "turn" + processLevel;
			
			//Indicate that it is this process's turn (to wait) at level K 
			mMemory.store(turnVarName, processorNumber); //turn[processLevel] = processorNumber;
			
			//Check to see if there are processors competing at a higher level 
			//and that it is this processor's turn to wait.
			//If there are no processes at a higher level, or it is no longer this processor's turn, move on
			boolean turn_at_process_level = true; //Assume true to start
			while(checkForHigherProcessors(processLevel) && turn_at_process_level){
				//turn_at_process_level: Represents turn[processLevel] == processorNumber
				//load(turnVarName) is the same as getting the value of turn[processLevel]

				
				//Get the currentTurnValue
				int currentTurnValue = mMemory.load(turnVarName);
				
				turn_at_process_level = (currentTurnValue == processorNumber);
			}
			
		}
		//Process can now enter <Critical Section>
		
	}
	
	/**
	 * Call after process exits <Critical Section> code
	 */
	public void PetersonsAlgorithmExitSection(){
		//flag[processorNumber] = -1;
		mMemory.store(flagVarName, -1);
	}
	
	/**
	 * Used to test our petersonAlgorithmEntrySection and PetersonsAlgorithmExitSection.
	 * Gets a random value, writes it to the csInteger, and sleeps. If another thread enters the Critical Section,
	 * it will change the value of csInteger, which will be detected when the first thread resumes. 
	 */
	public void criticalSection() {
		
		int originalValue = (int) (1000 * Math.random());
		
		csInteger.value = originalValue;
		
		try {
			sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (csInteger.value != originalValue) {
			System.err.println("More than one thread in the critical section detected.");
			csInteger.failCount++;
		}

	}
	
	
	/**
	 * THIS IS A HELPER FUNCTION
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
			
			//Get the flag of the other processor
			//flag[otherProcessor] == wBuffer.load("flag" + processLevel)
			int otherProcessorsFlag  = mMemory.load("flag" + otherProcessor);
			
			//If the other processor is at the same or a higher level than the current one, wait.
			if( otherProcessorsFlag >= processLevel) {
				return true;
			}
		}
		
		//If no other processor is competing at the same or a higher level return false.
		return false;
	}
	
}
