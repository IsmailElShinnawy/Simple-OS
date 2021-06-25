import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Schedular {

    private Memory memory;
    private Queue<Integer> ready, terminated;
    private Interpreter interpreter;
    private int quantaMap[];

    /**
     * Constuctor of the schedular, initializes the processes' PCBs and instructions
     * 
     * @param files String array containing the paths to the programs
     * @throws IOException when I/O fails
     */
    public Schedular(String files[]) throws IOException {
        memory = new Memory(files.length, 14);
        interpreter = new Interpreter(memory);
        quantaMap = new int[files.length + 1];

        ready = new LinkedList<Integer>();
        terminated = new LinkedList<Integer>();

        int programNumber = 1;
        for (String file : files) {
            HashMap<String, String> pcb = new HashMap<String, String>();

            pcb.put("pid", programNumber + "");
            pcb.put("pstate", "not running");
            // rest of PCB is initialized in memory because it needs memory addresses not
            // known here

            memory.storePCB(programNumber, pcb);

            Scanner sc = new Scanner(file);
            LinkedList<String> instructions = new LinkedList<String>();
            while (sc.ready()) {
                String ins = sc.nextLine();
                instructions.add(ins);
            }
            instructions.add("END");

            memory.storeInstructions(instructions.toArray(new String[instructions.size()]), programNumber + "");

            ready.add(programNumber++);
        }
    }

    /**
     * runs each process for 1 quantum (2 instructions) and terminates a process
     * when it ends
     * 
     * @throws IOException when I/O fails
     */
    public void run() throws IOException {

        while (!ready.isEmpty()) {
            int currentProcess = ready.poll();
            System.out.printf("PROCESS `%s` CHOSEN TO RUN\n", currentProcess + "");
            memory.setProcessState(currentProcess + "", "running");

            String currentInstruction = memory.getNextInstruction(currentProcess + "");
            if (currentInstruction.equals("END")) {
                memory.setProcessState(currentProcess + "", "not running");
                terminated.add(currentProcess);
                System.out.printf("PROCESS `%s` TERMINATED: RAN FOR %f quanta\n", currentProcess + "",
                        quantaMap[currentProcess] / 2.0);
                continue;
            }
            interpreter.interpretInstruction(currentInstruction, currentProcess);
            quantaMap[currentProcess]++;

            currentInstruction = memory.getNextInstruction(currentProcess + "");
            if (currentInstruction.equals("END")) {
                memory.setProcessState(currentProcess + "", "not running");
                terminated.add(currentProcess);
                System.out.printf("PROCESS `%s` TERMINATED: RAN FOR %f quanta\n", currentProcess + "",
                        quantaMap[currentProcess] / 2.0);
                continue;
            }
            interpreter.interpretInstruction(currentInstruction, currentProcess);
            quantaMap[currentProcess]++;

            memory.setProcessState(currentProcess + "", "not running");
            ready.add(currentProcess);
        }
        System.out.println(memory);
    }

    public static void main(String[] args) throws IOException {
        Schedular schedular = new Schedular(
                new String[] { "programs/Program 1.txt", "programs/Program 2.txt", "programs/Program 3.txt" });

        schedular.run();

    }
}
