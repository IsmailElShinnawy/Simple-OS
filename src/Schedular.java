import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Schedular {

    private Memory memory;
    private Queue<Integer> ready, terminated;

    public Schedular(String files[]) {
        memory = new Memory(files.length, 13);
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

            memory.storeInstructions(instructions.toArray(new String[instructions.size()]), programNumber + "");

            ready.add(programNumber++);
        }

        run();
    }

    public void run() {

        while (!ready.isEmpty()) {
            int currentProcess = ready.poll();
        }
    }
}
