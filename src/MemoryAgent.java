import java.util.concurrent.ConcurrentLinkedDeque;

public class MemoryAgent extends Thread {

	public WriteBuffer wBuffer;
	public MainMemory mMemory;
	private boolean finished = false;
	static boolean ONE = true;
	
	/**
	 * The constructor for MemoryAgent.
	 * @param inputWriteBuffer The WriteBuffer for the Processor creating the MemoryAgent
	 * @param inputMainMemory The MainMemory
	 */
	public MemoryAgent(WriteBuffer inputWriteBuffer, MainMemory inputMainMemory){
		wBuffer = inputWriteBuffer;
		mMemory = inputMainMemory;
	}
	
	public void run() {
		//Run loop until endThread is called, then end the thread.
		while(ONE){
			
			memoryVariable nextVariable = wBuffer.nextVariableToStore();
			if(nextVariable == null){
				//if endThread has been called - Setting finished to true - End the thread.
				if(finished){
					break;
				}
				else{ //Otherwise, restart the loop
					continue;
				}
				
			}
			
			//Store the variable to the MainMemory
			mMemory.store(nextVariable.vName, nextVariable.value);
			
			
		}
		
	}
	
	/**
	 * Tells the thread to end when there are no more elements to store. 
	 */
	public void endThread(){
		finished = true;
	}
	
}
