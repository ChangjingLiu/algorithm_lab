import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        StringBuilder s = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            s.append(BinaryStdIn.readString());
        }
        CircularSuffixArray arr = new CircularSuffixArray(s.toString());

        //find first
        int length = arr.length();
        //int first;
        for (int i = 0; i < length; i++) {
            if (arr.index(i) == 0) {
                //first = i;
                BinaryStdOut.write(i);
                break;
            }
        }

        //find t
        for (int i = 0; i < length; i++) {
            int tmp = arr.index(i);
            int lastIndex = (length - 1 + tmp) % length;
            BinaryStdOut.write(s.charAt(lastIndex));
        }
        BinaryStdOut.close();

    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            transform();
        } else {
            inverseTransform();
        }
    }
}
