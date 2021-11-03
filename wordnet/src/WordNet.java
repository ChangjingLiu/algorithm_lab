import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class WordNet {

    private ST<String, Bag<Integer>> st;
    private Digraph G;
    private ArrayList<String> idList;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        st = new ST<String, Bag<Integer>>();
        idList = new ArrayList<String>();
        int maxvertex = 0;
        In inSynsets = new In(synsets);
        while (inSynsets.hasNextLine()) {
            maxvertex++;
            String[] line_split = inSynsets.readLine().split(",");  // ,用于分割 id、synset、gloss
            Integer id = Integer.parseInt(line_split[0]);
            String[] nouns = line_split[1].split(" "); // 空格用于分割同义词单词，a2存放单词
            idList.add(line_split[1]);
            for (String noun : nouns) {
                //StdOut.printf(noun);
                if (st.contains(noun)) {
                    st.get(noun).add(id);
                } else {
                    Bag<Integer> b = new Bag<Integer>();
                    b.add(id);
                    st.put(noun, b);
                }
            }
        }

        In inHypernyms = new In(hypernyms);
        boolean[] isNotRoot = new boolean[maxvertex];
        int rootnum = 0;
        G = new Digraph(maxvertex);
        while (inHypernyms.hasNextLine()) {
            String[] line_split = inHypernyms.readLine().split(",");
            int id = Integer.parseInt(line_split[0]);
            isNotRoot[id] = true;
            //String[] nums = line_split[1].split(" ");
            int len = line_split.length;
            for (int i = 1; i < len; i++) {
                int num_id = Integer.parseInt(line_split[i]);
                G.addEdge(id, num_id);
                //StdOut.printf(num);
            }
        }
        for (int i = 0; i < maxvertex; i++) {
            if (!isNotRoot[i]) rootnum++;
        }

        //判断是否有环//判断是否有两个以上的节点
        DirectedCycle cylinder = new DirectedCycle(G);
        if (cylinder.hasCycle() || rootnum >= 2) {
            throw new IllegalArgumentException("root number is larger than 2");
        }

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return st.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("no word");
        } else {
            return st.contains(word);
        }
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new java.lang.IllegalArgumentException("the word is null");
        }

        if (!isNoun(nounA))
            throw new java.lang.IllegalArgumentException("the String nounA is no in WordNet");

        if (!isNoun(nounB))
            throw new java.lang.IllegalArgumentException("the String nounB is no in WordNet");

        Bag<Integer> valueA = st.get(nounA);
        Bag<Integer> valueB = st.get(nounB);
        SAP s = new SAP(G);
        return s.length(valueA, valueB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new java.lang.IllegalArgumentException("the word is null");
        }

        if (!isNoun(nounA))
            throw new java.lang.IllegalArgumentException("the String nounA is no in WordNet");

        if (!isNoun(nounB))
            throw new java.lang.IllegalArgumentException("the String nounB is no in WordNet");

        Bag<Integer> valueA = st.get(nounA);
        Bag<Integer> valueB = st.get(nounB);
        SAP s = new SAP(G);
        int id = s.ancestor(valueA, valueB);
        return idList.get(id);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String in1 = ".\\test\\synsets.txt";
        String in2 = ".\\test\\hypernyms.txt";
        WordNet net1 = new WordNet(in1, in2);
        StdOut.print(net1.sap("Adam", "Acre"));
    }
}
