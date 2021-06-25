import java.util.HashMap;

public class Memory {

    // memory is represented as a 2D-array of Strings where the first column
    // represents the key and the second column represents the value, the number
    // of
    // rows in memory depends on the total number of processes we need to save
    // example of a memory
    // row # | key | value
    // 0 | "a_p1" | "value1"
    // 1 | "b_p1" | "value2"
    // ...
    String memory[][];

    int numberOfProcesses;
    int maximumProcessSize;

    // holds the start position of the next process to be initialized
    // goes 0 then maximumProcessSize then 2 * maximumProcessSize ...
    int currentEmptyProcessLocation = 0;

    /**
     * Constructor of the memory, initializes an empty memory with the maximum
     * number of rows needed for the processes
     * 
     * @param numberOfProcesses  number of process that needs access to memory
     * @param maximumProcessSize maximum number of memory words a process needs
     */
    public Memory(int numberOfProcesses, int maximumProcessSize) {
        // initialize the memory array with 2 cols and (numberOfProcesses *
        // maximumProcessSize) rows
        memory = new String[numberOfProcesses * maximumProcessSize][2];
        this.numberOfProcesses = numberOfProcesses;
        this.maximumProcessSize = maximumProcessSize;
    }

    /**
     * sets the memory words in the PCB segment for this process segment in memory
     * 
     * @param programNumber process id
     * @param pcb           hashmap containing the pid and pstate of the process
     */
    public void storePCB(int programNumber, HashMap<String, String> pcb) {
        // starting at the currentEmptyProcessLocation initialize the PCB of the
        // program
        // row # | key | value
        // currentEmptyProcessLocation | pid_<program-#> | pcb.pid
        // currentEmptyProcessLocation+1 | pstate_<program-#> | pcb.pstate
        // currentEmptyProcessLocation+2 | pc_<program-#> | currentEmptyProcessLocation
        // + 5
        // currentEmptyProcessLocation+3 | start-address_<program-#> |
        // currentEmptyProcessLocation
        // currentEmptyProcessLocation+4 | end-address_<program-#> |
        // currentEmptyProcessLocation + maximumProcessSize - 1

        // then updates the currentEmptyProcessLocation

        int i = currentEmptyProcessLocation;
        memory[i][0] = "pid_" + programNumber + "";
        memory[i][1] = pcb.get("pid") + "";
        memory[i + 1][0] = "pstate_" + programNumber + "";
        memory[i + 1][1] = pcb.get("pstate");
        memory[i + 2][0] = "pc_" + programNumber + "";
        memory[i + 2][1] = i + 5 + "";
        memory[i + 3][0] = "start-address_" + programNumber + "";
        memory[i + 3][1] = currentEmptyProcessLocation + "";
        memory[i + 4][0] = "end-address_" + programNumber + "";
        memory[i + 4][1] = ((currentEmptyProcessLocation + maximumProcessSize) - 1) + "";

        System.out.println("MEMORY BEING ACCESSED TO STORE PCB FOR PROCESS `" + programNumber + "`");
        for (int j = currentEmptyProcessLocation; j < currentEmptyProcessLocation + 5; ++j) {
            System.out.printf("`%s` is set to `%s` @ %d\n", memory[j][0], memory[j][1], j);
        }
        currentEmptyProcessLocation = (currentEmptyProcessLocation + maximumProcessSize);

    }

    /**
     * stores the instructions of the process in the instructions segment of the
     * process segment in memory
     * 
     * @param instructions  string array containing the instructions of the process
     * @param programNumber process id
     */
    public void storeInstructions(String[] instructions, String programNumber) {
        // finds the memory block for this program using the programNumber, then
        // find
        // the first position of the instructions block, then store all the
        // instruction,
        // one instruction per row starting from this position
        System.out.println("MEMORY IS BEING ACCESSED TO STORE INSTRUCTIONS FOR PROCESS `" + programNumber + "`");
        int programNum = Integer.parseInt(programNumber);
        int processPosition = ((programNum - 1) * maximumProcessSize);
        int instructionPosition = processPosition + 5;
        int instructionCount = 0;
        for (int i = 0; i < memory.length; i++) {
            if (i == instructionPosition) {
                memory[i][0] = "ins-" + instructionCount + "_" + programNumber;

                if (instructionCount < instructions.length) {
                    memory[i][1] = instructions[instructionCount];
                    System.out.printf("`%s` is set to `%s` @ %d\n", memory[i][0], memory[i][1], i);
                    instructionCount += 1;
                    instructionPosition += 1;
                    if (instructionCount == instructions.length)
                        break;
                }
            }
        }
    }

    /**
     * returs the instruction at the PC of this process
     * 
     * @param programNumber process id
     * @return instrucion to be executed by interpreter
     */
    public String getNextInstruction(String programNumber) {
        // finds the memory block for this program using the programNumber, then
        // finds
        // the PC value of the within the PCB block, then returns the
        // instruction at the
        // PC value and increments the pc value by one
        System.out.println("MEMORY IS BEING ACCESSED TO READ NEXT INSTRUCTION FOR PROCESS `" + programNumber + "`");
        int programNum = Integer.parseInt(programNumber);
        int processPosition = ((programNum - 1) * maximumProcessSize);
        String stringPC = memory[processPosition + 2][1];
        int pc = Integer.parseInt(stringPC);
        String result = memory[pc][1];
        System.out.printf("`%s` is read: `%s` from @ %d\n", memory[pc][0], memory[pc][1], pc);
        pc += 1;
        memory[processPosition + 2][1] = pc + "";
        System.out.printf("`%s` is set to `%s` @ %d\n", memory[processPosition + 2][0], memory[processPosition + 2][1],
                processPosition + 2);
        return result;
    }

    /**
     * Stores the variable key-value pair in the variables segment in the process
     * segment in memory
     * 
     * @param variableName  name of the variable to be stored
     * @param variableValue value of the variable to be stored
     * @param programNumber process id
     */
    public void storeVariable(String variableName, String variableValue, String programNumber) {
        // finds the memory block for this program using the programNumber, then
        // finds
        // the variables block within this block, then finds the first empty
        // location
        // within the variables block and then stores the variable key-value
        // pair
        System.out.println("MEMORY IS BEING ACCESSED TO STORE A VARIABLE FOR PROCESS `" + programNumber + "`");
        int programNum = Integer.parseInt(programNumber);
        int processPosition = ((programNum - 1) * maximumProcessSize);
        int processEnd = processPosition + maximumProcessSize;
        for (int i = processPosition; i < processEnd; i++) {
            if (memory[i][0] == null || memory[i][0].equals(variableName + "_" + programNumber)) {
                memory[i][0] = variableName + "_" + programNumber;
                memory[i][1] = variableValue;
                System.out.printf("`%s` is set to `%s` @ %d\n", memory[i][0], memory[i][1], i);
                break;
            }

        }
    }

    /**
     * gets the variable value associated with the variable name passed
     * 
     * @param variableName  variable name which the method return the value of
     * @param programNumber process id
     * @return the value of the variable with the variable name passed
     */
    public String getVariable(String variableName, String programNumber) {
        // finds the memory block for this program using the programNumber, then
        // finds
        // the variables block within this block and returns the value of the
        // variable
        // name in this process
        System.out.println("MEMORY IS BEING ACCESSED TO READ A VARIABLE FOR PROCESS `" + programNumber + "`");
        int programNum = Integer.parseInt(programNumber);
        int processPosition = ((programNum - 1) * maximumProcessSize);
        int processEnd = processPosition + maximumProcessSize;
        for (int i = processPosition; i < processEnd; i++) {
            if (memory[i][0].equals(variableName + "_" + programNumber)) {
                System.out.printf("`%s` is read: `%s` from @ %d\n", memory[i][0], memory[i][1], i);
                return memory[i][1];
            }

        }

        return "variable not found";
    }

    /**
     * checks whether a variable with the passed variable name exists in the
     * variables segment in the process segment in memory or not
     * 
     * @param variableName  variable name which the method checks its existence
     * @param programNumber process id
     * @return true if the variable exists and false if it does not
     */
    public boolean containsVariable(String variableName, String programNumber) {
        int programNum = Integer.parseInt(programNumber);
        int processPosition = ((programNum - 1) * maximumProcessSize);
        int processEnd = processPosition + maximumProcessSize;
        for (int i = processPosition; i < processEnd; i++) {
            if (memory[i][0] != null && memory[i][0].equals(variableName + "_" + programNumber)) {
                return true;
            }
        }
        return false;
    }

    /**
     * sets the process state of the proccess with the passed id in the PCB segment
     * of the process segment in memory
     * 
     * @param programNumber process id
     * @param state         "running" || "not running"
     */
    public void setProcessState(String programNumber, String state) {
        // finds the memory block for this program using the programNumber, then
        // updates
        // the state of the process to the new state passed as a parameter

        System.out.println("MEMORY IS BEING ACCESSED SET PROCESS STATE FOR PROCESS `" + programNumber + "`");
        int programNum = Integer.parseInt(programNumber);
        int processPosition = ((programNum - 1) * maximumProcessSize);
        int statePosition = processPosition + 1;

        memory[statePosition][1] = state;
        System.out.printf("`%s` is set to `%s` @ %d\n", memory[statePosition][0], memory[statePosition][1],
                statePosition);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("row#\tkey\t\tvalue\n");
        for (int i = 0; i < memory.length; ++i) {
            sb.append(String.format("%d\t%s\t\t%s\n", i, memory[i][0], memory[i][1]));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Memory m = new Memory(3, 13);
        HashMap<String, String> pcb1 = new HashMap<String, String>();
        pcb1.put("pid", "1");
        pcb1.put("pstate", "not running");
        m.storePCB(1, pcb1);
        m.storeInstructions(new String[] { "test-1a", "test-1b", "test-1c" }, "1");
        m.storeVariable("a", "34", "1");
        System.out.println(m.getNextInstruction("1"));
        System.out.println(m.getNextInstruction("1"));
        System.out.println(m.getVariable("a", "1"));
        HashMap<String, String> pcb2 = new HashMap<String, String>();
        pcb2.put("pid", "2");
        pcb2.put("pstate", "not running");
        m.storePCB(2, pcb2);
        m.storeInstructions(new String[] { "test-2", "test-2" }, "2");
        m.storeVariable("b", "89", "2");
        System.out.println(m);

    }
}