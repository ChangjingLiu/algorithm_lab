import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

// 参照stackflow
public class CircularSuffixArray {
    private String string;
    private Integer[] sortSuffixes;

    private class SuffixesOrder implements Comparator<Integer> {
        //主要关注字符串相等情况
        public int compare(Integer a, Integer b) {
            //if ((length() - 1) < i) return 1;//
            //else if ((length() - 1) < j) return -1;//
            for (int i = 0; i < length(); i++) {
                char c1 = string.charAt((i + a) % length());
                char c2 = string.charAt((i + b) % length());
                if (c1 > c2) return 1;
                if (c1 < c2) return -1;
            }
            return 0;
        }
    }

    private Comparator<Integer> suffixesOrder() {
        return new SuffixesOrder();
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new NullPointerException("null argument");
        string = s;
        sortSuffixes = new Integer[length()];//
        for (int i = 0; i < length(); i++)
            sortSuffixes[i] = i;
        Arrays.sort(sortSuffixes, suffixesOrder());//
    }

    public int length() {
        return string.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) {
            throw new IllegalArgumentException("index ou of range!");
        }
        return sortSuffixes[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray arr = new CircularSuffixArray(s);
        //StdOut.print(CircularSuffixArray.sortSuffixes[0]);
        for (int i = 0; i < s.length(); i++) {
            int ii = arr.index(i);
            StdOut.println(ii);
        }
    }

}
