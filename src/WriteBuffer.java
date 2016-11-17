import java.util.Iterator;
import java.util.concurrent.*;


public class WriteBuffer {

	/**
	 * a boolean to determine is the write buffer operates in TSO or PSO
	 */
	private boolean tso; // if true, use FIFO; else use FIFO per variable (PSO)

	/**
	 * a concurrent hashmap so store the variable states in a buffer. Can be used for TSO and PSO
	 * implementations.
	 */
	private ConcurrentHashMap<String, ConcurrentLinkedDeque<memoryVariable>> buffer = new ConcurrentHashMap<>();
	
	/**
	 * a queue used to make PSO stores fair (round robin)  
	 */
	private ConcurrentLinkedDeque<String> nextVariableQueue = new ConcurrentLinkedDeque<>();
	
	/**
	 * a constructor for the WriteBuffer object
	 * @param tsoIn a boolean to determine if the WriteBuffer is tso or pso
	 */
	public WriteBuffer(boolean tsoIn) {
		
		tso = tsoIn;
		
		if (tso)
			buffer.put("TSOBuffer", new ConcurrentLinkedDeque<memoryVariable>());
		
	}
	
	/**
	 * the load function that is called to load a value of a variable
	 * @param x the variable name that needs to be loaded
	 * @return the value of the variable
	 * @throws NotInBufferException if the variable does not exist in the buffer
	 */
	public int load (String x) throws NotInBufferException {
		
		if (tso)
			return loadTSO(x);
		else
			return loadPSO(x);
		
	}
	
	/**
	 * a function to store the values of the variables into the buffer of the WriteBuffer
	 * @param x the name of the variable to be store 
	 * @param v the value of the variable to be store
	 */
	public void store (String x, int v) {
		
		if(tso)
			storeTSO(x, v);
		else
			storePSO(x, v);
		
	}
	
	/** 
	 * a method to be called by the MemoryAgent in order to store the next value into main memory
	 * @return the variable object that describes a variables name and value
	 */
	public memoryVariable nextVariableToStore() {
		
		if (tso)
			return nextTSO();
		else 
			return nextPSO();
		
	}
	
	/**
	 * a helper function to load the variable using TSO (so from a FIFO queue)
	 * @param x teh variable to be loaded
	 * @return the value of the requested variable
	 * @throws NotInBufferException if the variable is not in the buffer
	 */
	private int loadTSO(String x) throws NotInBufferException {
		
		Iterator<memoryVariable> iter = buffer.get("TSOBuffer").iterator();
		
		int rv = 0;
		boolean loaded = false;
		
		while (iter.hasNext()) {	
			memoryVariable mV = iter.next();
			
			if (mV.vName == x) {
				loaded = true;
				rv = mV.value;
			}
		}
		
		if (!loaded)
			throw new NotInBufferException();
		
		return rv;
		
	}
	
	/**
	 * a method to lead the variable using PSO (all variables are stored in seperate FIFO queues)
	 * @param x the variable to be loaded
	 * @return the value of the requested variable
	 * @throws NotInBufferException if the variable is not in the buffer
	 */
	private int loadPSO(String x) throws NotInBufferException {
		
		if (!buffer.containsKey(x))
			throw new NotInBufferException();
		
		if (buffer.get(x).isEmpty())
			throw new NotInBufferException();
		
		return buffer.get(x).getLast().value;

	}
	
	/**
	 * a method to store the variable into the write buffer using TSO
	 * @param x the name of the variable to be stored
	 * @param v the value of the variable that is to be stored
	 */
	private void storeTSO(String x, int v) {
		
		buffer.get("TSOBuffer").add(new memoryVariable(x, v));
		
	}
	
	/**
	 * a method to store the variable in the buffer using PSO
	 * @param x the name of the variable to be stored
	 * @param v the value of the variable to be stored
	 */
	private void storePSO(String x, int v) {
		
		if (!buffer.containsKey(x))
			buffer.put(x, new ConcurrentLinkedDeque<memoryVariable>());
		
		buffer.get(x).add(new memoryVariable(x, v));
		
		if (!nextVariableQueue.contains(x))
			nextVariableQueue.add(x);

	}
	
	
	/**
	 * the next value to be stored to main memory using TSO
	 * @return the head of the FIFO tso queue
	 */
	private memoryVariable nextTSO() {
		
		return buffer.get("TSOBuffer").poll();
		
	}
	
	/**
	 * a helper method to send the next value that needs to be stored in main memory
	 * @return
	 */
	private memoryVariable nextPSO() {
		
		String nextKey = nextVariableQueue.poll();
		
		if (nextKey == null)
			return null;
		
		if (buffer.get(nextKey).size() > 1)
			nextVariableQueue.add(nextKey);
		
		return buffer.get(nextKey).poll();
		
	}

}
