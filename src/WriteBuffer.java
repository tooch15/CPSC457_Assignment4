import java.util.concurrent.*;


public class WriteBuffer {

	private boolean tso; // if true, use FIFO; else use FIFO per variable

	
	public WriteBuffer(boolean tsoIn) {
		tso = tsoIn;
	}
	
	public void load (String x) throws NotInBufferException {
		
	}
	
	public void store (String x, int v) {
		
	}

}
