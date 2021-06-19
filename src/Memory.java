public class Memory {

    // memory is represented as a 2D-array of Strings where the first column
    // represnts the key and the second column represents the value, the number of
    // rows in memory is depends on the total number of processes we need to save
    // example of a memory
    // row # | key | value
    // 1 | "a_p1" | "Hamada"
    // 2 | "b_p1" | "Sasa"
    // ...
    String memory[][];

    int numberOfProcesses;
    int maximumProcessSize;

    // holds the start position of the next process to be initialized
    // goes 0 then maximumProcessSize then 2 * maximumProcessSize ...
    int currentEmptyProcessLocation = 0;

    public Memory(int numberOfProcesses, int maximumProcessSize) {
        // initialize the memory array with 2 cols and (numberOfProcesses *
        // maximumProcessSize) rows
    }

    public void initializePCB(int programNumber) {
        // starting at the currentEmptyProcessLocation initialize the PCB of the program
        // row # | key | value
        // currentEmptyProcessLocation | pid_<program-#> | <program-#>
        // currentEmptyProcessLocation+1 | pstate_<program-#> | "not running"
        // currentEmptyProcessLocation+2 | pc_<program-#> | "0"
        // currentEmptyProcessLocation+3 | start-address_<program-#> | currentEmptyProcessLocation
        // currentEmptyProcessLocation+4 | end-address_<program-#> | currentEmptyProcessLocation + maximumProcessSize - 1
        
        // update the currentEmptyProcessLocation
    }
    
    public void storeInstructions(String[] instructions) {
        // finds the memory block for this program using the programNumber, then find
        // the first position of the instructions block, then store all the instruction,
        // one instruction per row starting from this postition
    }

    public String getNextInstruction(String programNumber) {
        // finds the memory block for this program using the programNumber, then finds
        // the PC value of the within the PCB block, then returns the instruction at the
        // PC value and increments the pc value by one
        return null;
    }
    public void storeVariable(String variableName, String variableValue, String programNumber) {
        // finds the memory block for this program using the programNumber, then finds
        // the variables block within this block, then finds the first empty location
        // within the variables block and then stores the variable key-value pair
    }

    public String getVariable(String variableName, String programNumber) {
        // finds the memory block for this program using the programNumber, then finds
        // the variables block within this block and returns the value of the variable
        // name in this process
        return null;
    }


    public void setProcessState(String programNumber, String state) {
        // finds the memory block for this program using the programNumber, then updates
        // the state of the process to the new state passed as a parameter
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("row#\tkey\tvalue\n");
        for(int i = 0; i < memory.length; ++i) {
            sb.append(String.format("%d\t%s\t%s\n", i, memory[i][0], memory[i][1]));
        }
        return sb.toString();
    }
}
