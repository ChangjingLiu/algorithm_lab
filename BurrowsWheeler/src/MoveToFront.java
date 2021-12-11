import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> sequence = new LinkedList<>();

        //char[] aux = new char[R];
        for (int i = 0; i < R; i++)
            sequence.add((char) i);

        StringBuilder input = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            input.append(BinaryStdIn.readChar());
        }
//        StdOut.println(input);
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            //StdOut.printf("%c", c);
            char index = (char) sequence.indexOf(c);
            BinaryStdOut.write(index);
            sequence.remove((Object) c);
            sequence.addFirst(c);
        }
        BinaryStdOut.close(); // out.close();
    }

    public static void decode() {
        LinkedList<Character> sequence = new LinkedList<>();

        //char[] aux = new char[R];
        for (int i = 0; i < R; i++)
            sequence.add((char) i);
        StringBuilder input = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            input.append(BinaryStdIn.readChar());
        }
//        StdOut.println(input);
        for (int i = 0; i < input.length(); i++) {
            int c = (int) input.charAt(i);
            //StdOut.printf("%c", c);
            char index = sequence.get(c);

            BinaryStdOut.write(index);
            sequence.remove((Object) index);
            sequence.addFirst(index);
        }
        BinaryStdOut.close(); // out.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else {
            decode();
        }

    }
}
