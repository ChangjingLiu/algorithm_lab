import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    private ST<String, Bag<Integer>> st;
    private Digraph G;
    //private SAP sap;
    //private ArrayList<String> idList;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        st = new ST<String, Bag<Integer>>();
        int maxvertex = 0;
        In inSynsets = new In(synsets);
        In inHypernyms = new In(hypernyms);
        while (inSynsets.hasNextLine()) {
            maxvertex++;
            String[] line_split = inSynsets.readLine().split(",");  // ,用于分割 id、synset、gloss
            Integer id = Integer.parseInt(line_split[0]);
            String[] nouns = line_split[1].split(" "); // 空格用于分割同义词单词，a2存放单词
            for (String noun : nouns) {
                StdOut.printf(noun);
                if (st.contains(noun)) {
                    st.get(noun).add(id);
                } else {
                    Bag<Integer> b = new Bag<Integer>();
                    b.add(id);
                    st.put(noun, b);
                }
            }
        }

        boolean[] isNotRoot = new boolean[maxvertex];
        int rootnum = 0;
        G = new Digraph(maxvertex);
        while (inHypernyms.hasNextLine()) {
            String[] line_split = inHypernyms.readLine().split(",");
            int id = Integer.parseInt(line_split[0]);
            isNotRoot[id] = true;
            String[] nums = line_split[1].split(" ");
            for (String num : nums) {
                int num_id = Integer.parseInt(num);
                G.addEdge(id, num_id);
                StdOut.printf(num);
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

    // do unit testing of this class
    public static void main(String[] args) {
        String in1 = ".\\test\\synsets3.txt";
        String in2 = ".\\test\\hypernyms3InvalidTwoRoots.txt";
        WordNet net1 = new WordNet(in1, in2);
    }
}
