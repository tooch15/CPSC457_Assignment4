import java.util.concurrent.*;


public class WriteBuffer {

	private boolean tso; // if true, use FIFO; else use FIFO per variable

	ConcurrentHashMap<String, ConcurrentLinkedDeque<Variable>> buffer = new ConcurrentHashMap<>();
	
	public WriteBuffer(boolean tsoIn) {
		tso = tsoIn;
		
		if (tso)
			buffer.put("TSOBuffer", new ConcurrentLinkedDeque<Variable>());
		
	}
	
	public void load (String x) throws NotInBufferException {
		
		boolean throwException = false;
		
		if (tso)
			throwException = loadTSO(x);
		else
			throwException = loadPSO(x);
		
		if (throwException)
			throw new NotInBufferException();
		
	}
	
	public void store (String x, int v) {
		if(tso)
			storeTSO(x, v);
		else
			storePSO(x, v);
		
	}
	
	private boolean loadTSO(String x) {
		
		//if (buffer.get("TSOBuffer").)
		
		
	}
	
	private boolean loadPSO(String x) {
		
		return (buffer.containsKey(x)) ? false : true; 

	}
	
	private void storeTSO(String x, int v) {
		
		buffer.get("TSOBuffer").add(new Variable(x, v));
		
	}
	
	private void storePSO(String x, int v) {
		
		if (!buffer.containsKey(x))
			buffer.put(x, new ConcurrentLinkedDeque<Variable>());
		
		buffer.get(x).add(new Variable(x, v));

	}

}
