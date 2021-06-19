import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Scanner {
    private BufferedReader br;
    private StringTokenizer st;
    private boolean isFileReader = false;

    public Scanner(String fileName) {
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        isFileReader = true;
    }

    public Scanner(InputStream is) {
        br = new BufferedReader(new InputStreamReader(is));
    }

    public int nextInt() {
        checkEmpty();
        return Integer.parseInt(st.nextToken());
    }

    public String next() {
        checkEmpty();
        return st.nextToken();
    }

    public String nextLine() {
        try {
            return br.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    public String readFile() throws IOException {
        if (!isFileReader) {
            // not allowed to use with console reader
            return null;
        }

        String result = "";
        String line;
        while ((line = nextLine()) != null) {
            result = result + line + "\n";
        }

        return result; // should return the result string that contains all lines of file

    }

    public boolean ready() {
        try {
            return br.ready();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

    private void checkEmpty() {
        if (st == null || !st.hasMoreTokens()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
