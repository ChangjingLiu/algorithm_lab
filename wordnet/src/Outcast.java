import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        int maxdistance = Integer.MIN_VALUE;
        String max_word = nouns[0];
        for (String noun1 : nouns) {
            int tmpdistance = 0;
            for (String noun2 : nouns) {
                if (!noun1.equals(noun2)) {
                    tmpdistance += this.wordnet.distance(noun1, noun2);
                }
            }
            if (tmpdistance > maxdistance) {
                maxdistance = tmpdistance;
                max_word = noun1;
            }
        }
        return max_word;
    }


    public static void main(String[] args) {
        String[] a = new String[5];
        a[0] = ".\\test\\synsets.txt";
        a[1] = ".\\test\\hypernyms.txt";
        a[2] = ".\\test\\outcast5.txt";
        a[3] = ".\\test\\outcast8.txt";
        a[4] = ".\\test\\outcast11.txt";
        WordNet wordnet = new WordNet(a[0], a[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < a.length; t++) {
            In in = new In(a[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(a[t] + ": " + outcast.outcast(nouns));
        }
    }
}
