import java.util.HashMap;
import java.util.concurrent.*;


public class MainMemory {
	
	//W oeuyoudewuhou

	public MemoryAgent mAgent;
	
	private HashMap<String, Integer> storedData = new HashMap<>();
	
	public int load(String x) { return storedData.get(x); }
	
	public void store(String x, int v) { storedData.put(x, v); }
	
}
