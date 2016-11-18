
public class MemoryAgent extends Thread {

	/**
	 * The WriteBuffer
	 */
	public WriteBuffer wBuffer;
	/**
	 * The MainMemory used by the MemoryAgent's processor
	 */
	public MainMemory mMemory;
	/*
	 * A boolean to keep track of if the thread should stop
	 */
	private boolean finished = false;
	/**
	 * Java needs this
	 */
	static final boolean ONE = true;
	
	/**
	 * The constructor for MemoryAgent.
	 * @param inputWriteBuffer The WriteBuffer for the Processor creating the MemoryAgent
	 * @param inputMainMemory The MainMemory
	 */
	public MemoryAgent(WriteBuffer inputWriteBuffer, MainMemory inputMainMemory){
		wBuffer = inputWriteBuffer;
		mMemory = inputMainMemory;
	}
	
	/**
	 * Checks the wBuffer for a variable to store. If a variable is found, store it to main memory. Otherwise, repeat.
	 * Will stop when endThread is called and the wBuffer is empty.
	 */
	public void run() {
		//Run loop until endThread is called, then end the thread.
		while(ONE){
			
			memoryVariable nextVariable = wBuffer.nextVariableToStore();
			wBuffer.loadFlag = false;
			
			if(nextVariable == null){
				//if endThread has been called - Setting finished to true - End the thread.
				if(finished){
					break;
				}
				else{ //Otherwise, restart the loop
					wBuffer.loadFlag = true;
					wBuffer.notifyLoad();
					continue;
				}
				
			}

			
			//Store the variable to the MainMemory
			mMemory.store(nextVariable.vName, nextVariable.value);
			wBuffer.loadFlag = true;
			wBuffer.notifyLoad();
			
		}
		
	}
	
	/**
	 * Tells the thread to end when there are no more elements to store. 
	 */
	public void endThread(){
		finished = true;
	}
	
}
