import java.util.concurrent.*;


public class WriteBuffer {

	private boolean tso; // if true, use FIFO; else use FIFO per variable

	private ConcurrentHashMap<String, ConcurrentLinkedDeque<memoryVariable>> buffer = new ConcurrentHashMap<>();
	
	public WriteBuffer(boolean tsoIn) {
		tso = tsoIn;
		
		if (tso)
			buffer.put("TSOBuffer", new ConcurrentLinkedDeque<memoryVariable>());
		
	}
	
	public void load (String x) throws NotInBufferException {
		
		boolean throwException = false;
		
		if (tso)
			throwException = loadTSO(x);
		else
			throwException = loadPSO(x);
		
		if (throwException)
			throw new NotInBufferException();
		else {
			
		}
		
	}
	
	public void store (String x, int v) {
		
		if(tso)
			storeTSO(x, v);
		else
			storePSO(x, v);
		
	}
	
	private int loadTSO(String x) {
		
		//if (buffer.get("TSOBuffer").)
		return true;
		
	}
	
	private int loadPSO(String x) {
		
		//return (buffer.containsKey(x))

	}
	
	private void storeTSO(String x, int v) {
		
		buffer.get("TSOBuffer").add(new memoryVariable(x, v));
		
	}
	
	private void storePSO(String x, int v) {
		
		if (!buffer.containsKey(x))
			buffer.put(x, new ConcurrentLinkedDeque<memoryVariable>());
		
		buffer.get(x).add(new memoryVariable(x, v));

	}

}
