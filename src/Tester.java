
public class Tester {
	
	public static void main(String[] args) {
		
		// create a single main memory object
		MainMemory mainMemory = new MainMemory();
		
		int numCPU = 10;
		
		boolean TSO = true;
		
		if (args.length == 1)
			numCPU = Integer.parseInt(args[0]);
		
		Processor[] procs = new Processor[numCPU];
		MemoryAgent[] agents = new MemoryAgent[numCPU];
		WriteBuffer[] buffers = new WriteBuffer[numCPU];
		
		for (int i = 0; i < numCPU; i++) {
			
			buffers[i] = new WriteBuffer(TSO, mainMemory);
			procs[i] = new Processor(TSO, mainMemory, numCPU);
			agents[i] = new MemoryAgent(buffers[i], mainMemory);
			procs[i].start();
			
		}
		
	}

}
