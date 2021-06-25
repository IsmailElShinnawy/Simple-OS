import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;

public class Interpreter {

    private int pointer = 0;
    private String currentInstructionParsed[];
    private Memory memory;
    private int programNumber;

    public Interpreter(Memory memory) {
        this.memory = memory;
    }

    public void interpretInstruction(String ins, int programNumber) throws IOException {
        setPointer(0);
        setProgramNumber(programNumber);
        currentInstructionParsed = ins.split(" ");
        while (pointer < currentInstructionParsed.length) {
            String command = getNext();
            switch (command) {
                case "assign":
                    assignHandler();
                    break;
                case "add":
                    addHandler();
                    break;
                case "readFile":
                    readFileHandler();
                    break;
                case "writeFile":
                    writeFileHandler();
                    break;
                case "print":
                    printHandler();
                    break;
                case "input":
                    inputHandler();
                    break;
            }
        }
    }

    private void assignHandler() throws IOException {
        // String variable = sc.next();
        String variable = getNext();

        // String data = sc.next();
        String data = getNext();
        switch (data) {
            case "readFile":
                data = readFileHandler();
                break;
            case "input":
                data = inputHandler();
                break;
            default:
                data = memory.containsVariable(data, programNumber + "") ? memory.getVariable(data, programNumber + "")
                        : data;
        }

        try {
            int value = Integer.parseInt(data);
            // memory.put(variable, value);
            memory.storeVariable(variable, value + "", programNumber + "");
        } catch (Exception e) {
            // memory.put(variable, data);
            memory.storeVariable(variable, data, programNumber + "");
        }

    }

    /**
     * Handles the add call by getting the two operands and adding them and then
     * returns the result and saves it in the first one if it is a variable
     * 
     * @return the addition of two integer numbers
     */
    private int addHandler() throws IOException {
        // 1. read the next token using sc.next() -> should be 1st operand
        // 2. handle the cases as mentioned in readFile and writeFile
        // 3. same as 1 & 2 but for 2nd token -> should be 2nd operand

        // 4. add the two operands
        // 5. if first operand was a variable in memory then store the result as
        // its
        // value

        // 6. return the result of addition
        // String firstOperand = sc.next();
        String firstOperand = getNext();
        String variableName = null;
        boolean flag = false;
        switch (firstOperand) {
            case "readFile":
                firstOperand = readFileHandler();
                break;
            case "input":
                firstOperand = inputHandler();
                break;
            default: {
                // flag = memory.containsKey(firstOperand);
                flag = memory.containsVariable(firstOperand, programNumber + "");
                if (flag) {
                    variableName = firstOperand;
                }
                // firstOperand = memory.containsKey(firstOperand) ?
                // memory.get(firstOperand).toString() : firstOperand;
                firstOperand = memory.containsVariable(firstOperand, programNumber + "")
                        ? memory.getVariable(firstOperand, programNumber + "")
                        : firstOperand;
            }
        }

        // String secondOperand = sc.next();
        String secondOperand = getNext();
        switch (secondOperand) {
            case "readFile":
                secondOperand = readFileHandler();
                break;
            case "input":
                secondOperand = inputHandler();
                break;
            default:
                // secondOperand = memory.containsKey(secondOperand) ?
                // memory.get(secondOperand).toString()
                // : secondOperand;
                secondOperand = memory.containsVariable(secondOperand, programNumber + "")
                        ? memory.getVariable(secondOperand, programNumber + "")
                        : secondOperand;
        }
        int first = Integer.parseInt(firstOperand);
        int second = Integer.parseInt(secondOperand);
        if (flag) {
            // memory.put(variableName, first + second);
            memory.storeVariable(variableName, (first + second) + "", programNumber + "");
        }
        return first + second;

    }

    /**
     * Handles the readFile call by getting the fileName needed to be read and then
     * reading the file and returning its content
     * 
     * @return String representation of the content of the file
     */
    private String readFileHandler() throws IOException {
        // check printHandler for an idea about how I handled it
        // 1. read the next token by using sc.next() -> should be fileName
        // 2. check if the read token is another system call and if so then
        // handle first
        // the system call and then use the result as the fileName
        // 3. if not system call then check that the token is a variable name in
        // memory
        // if so use the value of the variable as the fileName
        // 4. if not a variable name then use the token as a fileName directly

        // 5. use the Scanner class with the Scanner(String fileName)
        // constructor and
        // the readFile() method (that you should write, I didn't) to read the
        // contents
        // of the file and return it

        // String filename = sc.next();
        String filename = getNext();
        switch (filename) {
            case "readFile":
                filename = readFileHandler();
                break;
            case "input":
                filename = inputHandler();
                break;
            default:
                // filename = memory.containsKey(filename) ? memory.get(filename).toString() :
                // filename;
                filename = memory.containsVariable(filename, programNumber + "")
                        ? memory.getVariable(filename, programNumber + "")
                        : filename;
        }
        Scanner fileScanner = new Scanner(filename);
        return fileScanner.readFile();
    }

    /**
     * Handles the writeFile call by getting the fileName to write to and the data
     * to be written and then writing it
     */
    private void writeFileHandler() throws IOException {
        // check printHandler for an idea about how I handled it
        // 1. read the next token by using sc.next() -> should be fileName
        // 2. check if the read token is another system call and if so then
        // handle first
        // the system call and then use the result as the fileName
        // 3. if not sys call then should check if it is a variable in memory
        // and if so
        // use the value of the variable as a fileName and if not then use the
        // token as
        // a fileName directly

        // 4. read the next token by using sc.next() -> should be data to write
        // 5. if sys call handled it first as above and as in read
        // 6. if variable also handle it as above and as in read

        // 7. use the PrintWriter class (should be imported) to write the data
        // to the
        // file

        // String fileName = sc.next();
        String fileName = getNext();
        switch (fileName) {
            case "readFile":
                fileName = readFileHandler();
                break;
            case "input":
                fileName = inputHandler();
                break;
            default:
                fileName = memory.containsVariable(fileName, programNumber + "")
                        ? memory.getVariable(fileName, programNumber + "")
                        : fileName;
        }

        // String data = sc.next();
        String data = getNext();
        switch (data) {
            case "readFile":
                data = readFileHandler();
                break;
            case "add":
                data = "" + addHandler();
                break;
            case "input":
                data = inputHandler();
                break;
            default:
                // data = memory.containsKey(data) ? memory.get(data).toString() : data;
                data = memory.containsVariable(data, programNumber + "") ? memory.getVariable(data, programNumber + "")
                        : data;
        }

        PrintWriter fileWriter = new PrintWriter(fileName);
        fileWriter.write(data);
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * Handles the print call by printing to the screen what comes after, if system
     * call follows print then the call is handled first and then the result is what
     * is printed
     * 
     * @throws IOException
     */
    private void printHandler() throws IOException {
        // String toPrint = sc.next();
        String toPrint = getNext();
        switch (toPrint) {
            case "readFile":
                toPrint = readFileHandler();
                break;
            case "add":
                toPrint = "" + addHandler();
                break;
            case "input":
                toPrint = inputHandler();
                break;
            default:
                // toPrint = memory.containsKey(toPrint) ? memory.get(toPrint).toString() :
                // toPrint;
                toPrint = memory.containsVariable(toPrint, programNumber + "")
                        ? memory.getVariable(toPrint, programNumber + "")
                        : toPrint;
        }
        System.out.println(toPrint);
    }

    /**
     * Handles the input call by getting the the text input from the user
     * 
     * @return String input by the user
     */
    private static String inputHandler() {
        System.out.println("WAITING FOR INPUT...");
        Scanner inputScanner = new Scanner(System.in);
        return inputScanner.nextLine();
    }

    private void setPointer(int pointer) {
        this.pointer = pointer;
    }

    private String getNext() {
        return currentInstructionParsed[pointer++];
    }

    private void setProgramNumber(int programNumber) {
        this.programNumber = programNumber;
    }
}
