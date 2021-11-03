import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private Digraph G;
    private int anc = -1;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();

        } else {
            this.G = new Digraph(G);
        }

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v > G.V() - 1 || w < 0 || w > G.V() - 1)
            throw new IllegalArgumentException();
        anc = -1;

        BreadthFirstDirectedPaths bv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bw = new BreadthFirstDirectedPaths(G, w);

        int minlength = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            if (bv.hasPathTo(i) && bw.hasPathTo(i)) {
                int l = bv.distTo(i) + bw.distTo(i);
                if (l < minlength) {
                    minlength = l;
                    anc = i;
                }
            }
        }
        if (minlength == Integer.MAX_VALUE) return -1;
        else return minlength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        length(v, w);
        return anc;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int length = Integer.MAX_VALUE;
        int temp;
        for (Integer i : v) {
            for (Integer j : w) {
                temp = length(i, j);
                if (temp < length)
                    length = temp;
            }
        }
        if (length == Integer.MAX_VALUE) return -1;
        else return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int length = Integer.MAX_VALUE;
        int local_anc = -1;
        int temp;
        for (Integer i : v) {
            for (Integer j : w) {
                temp = length(i, j);
                if (temp < length) {
                    length = temp;
                    local_anc = this.anc;
                }
            }
        }
        if (length == Integer.MAX_VALUE) return -1;
        else return local_anc;
    }

    public static void main(String[] args) {
        In in = new In(".\\test\\digraph2.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
