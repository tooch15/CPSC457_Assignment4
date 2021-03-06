
public class Tester {
	
	public static void main(String[] args) {
		
		// create a single main memory object
		MainMemory mainMemory = new MainMemory();
		
		int numCPU = 10;
		//Default numCPU = 10. If input, change numCPU
		if (args.length == 1)
			numCPU = Integer.parseInt(args[0]);
		
		//Initialize flag values
		for (int i = 0; i < numCPU; i++) {
			mainMemory.store("flag" + i, -1);
		}
		
		//Initialize flag values
		for (int i = 0; i < numCPU - 1; i++) {
			mainMemory.store("turn" + i, 0);
		}
		
		boolean TSO = true;
		
		
		
		CSInteger csi = new CSInteger();
		csi.failCount = 0;
		
		//Create Processors, MemoryAgents and WriteBuffers
		Processor[] procs = new Processor[numCPU];
		MemoryAgent[] agents = new MemoryAgent[numCPU];
		WriteBuffer[] buffers = new WriteBuffer[numCPU];
		
		for (int i = 0; i < numCPU; i++) {
			
			buffers[i] = new WriteBuffer(TSO, mainMemory);
			procs[i] = new Processor(TSO, mainMemory, i, numCPU, buffers[i], csi);
			agents[i] = new MemoryAgent(buffers[i], mainMemory);
			
			agents[i].start();
			
		}
		
		//Start processors
		for (int k = 0; k < numCPU; k++) {
			procs[k].start();
		}
		
		//Wait for all processors to finish
		for (int k = 0; k < numCPU; k++) {
			try {
				procs[k].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Tell MemoryAgents to finish
		for (int k = 0; k < numCPU; k++) {
			agents[k].endThread();
		}
		
		//Calculate number of failures
		System.out.println("1000 test threads were ran and there were " + csi.failCount + " failures.");
		System.out.println("Fail percentage: " + ((csi.failCount / 1000.00) * 100.0) + "%");
		
	}

}
