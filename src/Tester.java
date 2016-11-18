
public class Tester {
	
	public static void main(String[] args) {
		
		// create a single main memory object
		MainMemory mainMemory = new MainMemory();
		
		int numCPU = 10;
		
		for (int i = 0; i < numCPU; i++) {
			mainMemory.store("flag" + i, -1);
		}
		
		for (int i = 0; i < numCPU - 1; i++) {
			mainMemory.store("turn" + i, 0);
		}
		
		boolean TSO = false;
		
		if (args.length == 1)
			numCPU = Integer.parseInt(args[0]);
		
		CSInteger csi = new CSInteger();
		csi.failCount = 0;
		
		Processor[] procs = new Processor[numCPU];
		MemoryAgent[] agents = new MemoryAgent[numCPU];
		WriteBuffer[] buffers = new WriteBuffer[numCPU];
		
		for (int i = 0; i < numCPU; i++) {
			
			buffers[i] = new WriteBuffer(TSO, mainMemory);
			procs[i] = new Processor(TSO, mainMemory, i, numCPU, buffers[i], csi);
			agents[i] = new MemoryAgent(buffers[i], mainMemory);
			
			agents[i].start();
			
		}
		
		for (int k = 0; k < numCPU; k++) {
			procs[k].start();
		}
		
		for (int k = 0; k < numCPU; k++) {
			try {
				procs[k].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (int k = 0; k < numCPU; k++) {
			agents[k].endThread();
		}
		
		System.out.println("1000 test threads were ran and there were " + csi.failCount + " failures.");
		System.out.println("Fail percentage: " + ((csi.failCount / 1000.00) * 100.0) + "%");
		
		
	}

}
