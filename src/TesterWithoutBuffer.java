
public class TesterWithoutBuffer {
	
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
		
		//Create ProcessorWithoutBuffers
		ProcessorWithoutBuffer[] procs = new ProcessorWithoutBuffer[numCPU];
		for (int i = 0; i < numCPU; i++) {
			procs[i] = new ProcessorWithoutBuffer(TSO, mainMemory, i, numCPU, csi);			
		}
		
		//Start ProcessorWithoutBuffers
		for (int k = 0; k < numCPU; k++) {
			procs[k].start();
		}
		
		//Wait for all ProcessorWithoutBuffers to finish
		for (int k = 0; k < numCPU; k++) {
			try {
				procs[k].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Calculate number of failures
		System.out.println("1000 test threads were ran and there were " + csi.failCount + " failures.");
		System.out.println("Fail percentage: " + ((csi.failCount / 1000.00) * 100.0) + "%");
		
		
	}

}
