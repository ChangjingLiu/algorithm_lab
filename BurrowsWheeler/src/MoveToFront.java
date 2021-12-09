import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> sequence = new LinkedList<>();

        char[] aux = new char[R];
        for (int i = 0; i < R; i++)
            aux[i] = (char) i;
        String input = BinaryStdIn.readString();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int index = sequence.indexOf(c);
            BinaryStdOut.write(index);
            sequence.remove((Object) c);
            sequence.addFirst(c);
        }
        BinaryStdOut.flush(); // out.close();
    }

    public static void main(String[] args) {

    }
}
