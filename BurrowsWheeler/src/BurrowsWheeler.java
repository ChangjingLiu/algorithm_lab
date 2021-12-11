import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
//1:compile javac-algs4 BurrowsWheeler.java
//2:run
//java-algs4 BurrowsWheeler - < src/abra.txt | java-algs4 edu.princeton.cs.algs4.HexDump 16

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        StringBuilder input = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            input.append(BinaryStdIn.readString());
        }
//        String input = BinaryStdIn.readString();
        CircularSuffixArray arr = new CircularSuffixArray(input.toString());

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
            BinaryStdOut.write(input.charAt(lastIndex));
        }
        BinaryStdOut.close();

    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        //key-indexed counting algorithm
        int first = BinaryStdIn.readInt();
        String a = BinaryStdIn.readString();
        int N = a.length();
        int[] count = new int[257];
        int[] next = new int[N];
        char[] origin = new char[N];
        for (int i = 0; i < N; i++) {
            count[a.charAt(i) + 1]++;
        }
        for (int r = 0; r < 256; r++) {
            count[r + 1] += count[r];
        }
        for (int i = 0; i < N; i++) {
            int posi = count[a.charAt(i)]++;
            next[posi] = i;
            origin[posi] = a.charAt(i);

        }
        for (int i = 0; i < N; i++) {
            //StdOut.print(next[i]);

            BinaryStdOut.write(origin[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            transform();
        } else if ("+".equals(args[0])) {
            inverseTransform();
        }
    }
}
