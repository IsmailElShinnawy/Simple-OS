public class Memory {

    // memory is represented as a 2D-array of Strings where the first column
    // represents the key and the second column represents the value, the number
    // of
    // rows in memory depends on the total number of processes we need to save
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
        memory = new String[numberOfProcesses * maximumProcessSize][2];
        this.numberOfProcesses = numberOfProcesses;
        this.maximumProcessSize = maximumProcessSize;
    }

    public void initializePCB(int programNumber) {
        // starting at the currentEmptyProcessLocation initialize the PCB of the
        // program
        // row # | key | value
        // currentEmptyProcessLocation | pid_<program-#> | <program-#>
        // currentEmptyProcessLocation+1 | pstate_<program-#> | "not running"
        // currentEmptyProcessLocation+2 | pc_<program-#> | "0"
        // currentEmptyProcessLocation+3 | start-address_<program-#> |
        // currentEmptyProcessLocation
        // currentEmptyProcessLocation+4 | end-address_<program-#> |
        // currentEmptyProcessLocation + maximumProcessSize - 1

        // update the currentEmptyProcessLocation

        int i = currentEmptyProcessLocation;
        memory[i][0] = "pid_" + programNumber + "";
        memory[i][1] = programNumber + "";
        memory[i + 1][0] = "pstate_" + programNumber + "";
        memory[i + 1][1] = "not running";
        memory[i + 2][0] = "pc_" + programNumber + "";
        memory[i + 2][1] = i + 5 + "";
        memory[i + 3][0] = "start-address_" + programNumber + "";
        memory[i + 3][1] = currentEmptyProcessLocation + "";
        memory[i + 4][0] = "end-address_" + programNumber + "";
        memory[i + 4][1] = ((currentEmptyProcessLocation + maximumProcessSize) - 1) + "";
        currentEmptyProcessLocation = (currentEmptyProcessLocation + maximumProcessSize);

    }

    public void storeInstructions(String[] instructions, String programNumber) {
        // finds the memory block for this program using the programNumber, then
        // find
        // the first position of the instructions block, then store all the
        // instruction,
        // one instruction per row starting from this position
        int programNum = Integer.parseInt(programNumber);
        int processPosition = ((programNum - 1) * maximumProcessSize);
        int instructionPosition = processPosition + 5;
        int instructionCount = 0;
        for (int i = 0; i < memory.length; i++) {
            if (i == instructionPosition) {
                memory[i][0] = "ins-" + instructionCount + "_" + programNumber;

                if (instructionCount < instructions.length) {
                    memory[i][1] = instructions[instructionCount];
                    instructionCount += 1;
                    instructionPosition += 1;
                    if (instructionCount == instructions.length)
                        break;
                }
            }
        }
    }

    public String getNextInstruction(String programNumber) {
        // finds the memory block for this program using the programNumber, then
        // finds
        // the PC value of the within the PCB block, then returns the
        // instruction at the
        // PC value and increments the pc value by one
        int programNum = Integer.parseInt(programNumber);
        int processPosition = ((programNum - 1) * maximumProcessSize);
        String stringPC = memory[processPosition + 2][1];
        int pc = Integer.parseInt(stringPC);
        String result = memory[pc][1];
        pc += 1;
        memory[processPosition + 2][1] = pc + "";
        return result;
    }

    public void storeVariable(String variableName, String variableValue, String programNumber) {
        // finds the memory block for this program using the programNumber, then
        // finds
        // the variables block within this block, then finds the first empty
        // location
        // within the variables block and then stores the variable key-value
        // pair
        int programNum = Integer.parseInt(programNumber);
        int processPosition = ((programNum - 1) * maximumProcessSize);
        int processEnd = processPosition + maximumProcessSize;
        for (int i = processPosition; i < processEnd; i++) {
            if (memory[i][0] == null) {
                memory[i][0] = variableName + "_" + programNumber;
                memory[i][1] = variableValue;
                break;
            }

        }
    }

    public String getVariable(String variableName, String programNumber) {
        // finds the memory block for this program using the programNumber, then
        // finds
        // the variables block within this block and returns the value of the
        // variable
        // name in this process

        int programNum = Integer.parseInt(programNumber);
        int processPosition = ((programNum - 1) * maximumProcessSize);
        int processEnd = processPosition + maximumProcessSize;
        for (int i = processPosition; i < processEnd; i++) {
            if (memory[i][0].equals(variableName + "_" + programNumber)) {
                return memory[i][1];

            }

        }

        return "variable not found";
    }

    public void setProcessState(String programNumber, String state) {
        // finds the memory block for this program using the programNumber, then
        // updates
        // the state of the process to the new state passed as a parameter

        int programNum = Integer.parseInt(programNumber);
        int processPosition = ((programNum - 1) * maximumProcessSize);
        int statePosition = processPosition + 1;

        memory[statePosition][1] = state;

    }

    public String toString() {
        StringBuilder sb = new StringBuilder("row#\tkey\t\tvalue\n");
        for (int i = 0; i < memory.length; ++i) {
            sb.append(String.format("%d\t%s\t\t%s\n", i, memory[i][0], memory[i][1]));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Memory m = new Memory(3, 13);
        m.initializePCB(1);
        m.storeInstructions(new String[] { "test-1a", "test-1b", "test-1c" }, "1");
        m.storeVariable("a", "34", "1");
        System.out.println(m.getNextInstruction("1"));
        System.out.println(m.getNextInstruction("1"));
        System.out.println(m.getVariable("a", "1"));
        m.initializePCB(2);
        m.storeInstructions(new String[] { "test-2", "test-2" }, "2");
        m.storeVariable("b", "89", "2");
        System.out.println(m);

    }
}