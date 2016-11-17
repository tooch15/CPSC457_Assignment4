
import java.util.concurrent.*;


public class MainMemory {
	
	private ConcurrentHashMap<String, Integer> storedData = new ConcurrentHashMap<>();
	
	public int load(String x) { return storedData.get(x); }
	
	public void store(String x, int v) { storedData.put(x, v); }
	
}
