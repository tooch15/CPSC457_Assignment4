import java.util.Iterator;
import java.util.concurrent.*;


public class WriteBuffer {

	private boolean tso; // if true, use FIFO; else use FIFO per variable

	private ConcurrentHashMap<String, ConcurrentLinkedDeque<memoryVariable>> buffer = new ConcurrentHashMap<>();
	
	private ConcurrentLinkedDeque<String> nextVariableQueue = new ConcurrentLinkedDeque<>();
	
	public WriteBuffer(boolean tsoIn) {
		tso = tsoIn;
		
		if (tso)
			buffer.put("TSOBuffer", new ConcurrentLinkedDeque<memoryVariable>());
		
	}
	
	public int load (String x) throws NotInBufferException {
		
		if (tso)
			return loadTSO(x);
		else
			return loadPSO(x);
		
	}
	
	public void store (String x, int v) {
		
		if(tso)
			storeTSO(x, v);
		else
			storePSO(x, v);
		
	}
	
	public memoryVariable nextVariableToStore() {
		
		if (tso)
			return nextTSO();
		else 
			return nextPSO();
		
	}
	
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
	
	private int loadPSO(String x) throws NotInBufferException {
		
		if (!buffer.containsKey(x))
			throw new NotInBufferException();
		
		if (buffer.get(x).isEmpty())
			throw new NotInBufferException();
		
		return buffer.get(x).getLast().value;

	}
	
	private void storeTSO(String x, int v) {
		
		buffer.get("TSOBuffer").add(new memoryVariable(x, v));
		
	}
	
	private void storePSO(String x, int v) {
		
		if (!buffer.containsKey(x))
			buffer.put(x, new ConcurrentLinkedDeque<memoryVariable>());
		
		buffer.get(x).add(new memoryVariable(x, v));
		
		if (!nextVariableQueue.contains(x))
			nextVariableQueue.add(x);

	}
	
	private memoryVariable nextTSO() {
		
		return buffer.get("TSOBuffer").poll();
		
	}
	
	private memoryVariable nextPSO() {
		
		String nextKey = nextVariableQueue.poll();
		
		if (nextKey == null)
			return null;
		
		if (buffer.get(nextKey).size() > 1)
			nextVariableQueue.add(nextKey);
		
		return buffer.get(nextKey).poll();
		
	}

}
