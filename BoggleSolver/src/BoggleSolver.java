import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

// dictionary-algs4.txt board4x4.txt
// dictionary-yawl.txt board-antidisestablishmentarianisms.txt
// dictionary-yawl.txt board-dichlorodiphenyltrichloroethanes.txt
public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    //private final TrieST<Integer> dic = new TrieST<Integer>();
    private Bag<Integer>[] adj;
    private int cols, rows;
    private boolean[] marked;
    private SET<String> validwords;
    private BoggleBoard board;
    private Node root;

    public BoggleSolver(String[] dictionary) {
        root = new Node();
        int cnt = 0;
        for (String s : dictionary) {
            put(s, cnt);
            //dic.put(s,cnt);
            cnt++;
        }
//        StdOut.print(dic.contains("AI"));
    }

    private static class Node {
        private int val;
        private final Node[] next = new Node[26];
    }

    private int get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return 0;
        return x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d) - 'A';
        return get(x.next[c], key, d + 1);
    }

    private void put(String key, int val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, int val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = 1;
            return x;
        }
        int c = key.charAt(d) - 'A';
        x.next[c] = put(x.next[c], key, val, d + 1);
        return x;
    }

    private boolean isExist(int i, int j) {
        return i >= 0 && i < rows && j >= 0 && j < cols;
    }

    private void CreatAdj(BoggleBoard board) {
        this.board = board;
        cols = this.board.cols();
        rows = this.board.rows();
        adj = new Bag[cols * rows];//建立bag，长度为dice个数

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int v = i * cols + j;//当前dice的索引
                adj[v] = new Bag<Integer>();//为每个dice建立一个bag
                //八个方向
                if (isExist(i - 1, j - 1)) adj[v].add((i - 1) * cols + (j - 1));//left_top
                if (isExist(i - 1, j)) adj[v].add((i - 1) * cols + (j));//top
                if (isExist(i - 1, j + 1)) adj[v].add((i - 1) * cols + (j + 1));//right_top
                if (isExist(i, j + 1)) adj[v].add((i) * cols + (j + 1));//right
                if (isExist(i + 1, j + 1)) adj[v].add((i + 1) * cols + (j + 1));//right_bottom
                if (isExist(i + 1, j)) adj[v].add((i + 1) * cols + (j));//bottom
                if (isExist(i + 1, j - 1)) adj[v].add((i + 1) * cols + (j - 1));//left_bottom
                if (isExist(i, j - 1)) adj[v].add((i) * cols + (j - 1));//left
            }
        }

//        for(int i=0;i< adj.length;i++){
//            int r=i/cols;
//            int c=i%cols;
//            StdOut.printf("(%d,%d): ",r,c);
//            for(Integer s:adj[i]){
//                StdOut.printf(" (%d,%d)",s/cols,s%cols);
//            }
//            StdOut.println();
//        }

    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        validwords = new SET<String>();

        CreatAdj(board);

        for (int i = 0; i < adj.length; i++) {
            marked = new boolean[adj.length];
            Stack<Integer> visitingDices = new Stack<Integer>();

            visitingDices.push(i);
            marked[i] = true;
            char c = getLetterOnBoard(i);
            if (c == 'Q') {
                dfs(i, root.next['Q' - 'A'].next['U' - 'A'], "QU", visitingDices);
            } else {
                dfs(i, root.next[c - 'A'], c + "", visitingDices);
            }
        }

        return validwords;
    }

    private void dfs(Integer v, Node x, String str, Stack<Integer> visitingDices) {

        if (str.length() >= 3 && x != null) {
            if (x.val == 1) {
                validwords.add(str);
            }
        }


        for (Integer s : adj[v]) {
            char c = getLetterOnBoard(s);
            //Queue<String> tmp= (Queue<String>) dic.keysWithPrefix(str+c);
            if (!marked[s] && x != null) {
                if (x.next[c - 'A'] != null) {
                    visitingDices.push(s);
                    marked[s] = true;

                    if (c == 'Q') {
                        dfs(s, x.next['Q' - 'A'].next['U' - 'A'], str + "QU", visitingDices);
                    } else {
                        dfs(s, x.next[c - 'A'], str + c, visitingDices);
                    }


                    int index = visitingDices.pop();
                    marked[index] = false;
                }
            }

        }

    }

    private char getLetterOnBoard(int v) {
        int i = v / cols;
        int j = v % cols;
        return board.getLetter(i, j);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (get(word) == 0) {
            return 0;
        } else {
            int len = word.length();
            if (len < 3) {
                return 0;
            } else if (len <= 4) {
                return 1;
            } else if (len == 5) {
                return 2;
            } else if (len == 6) {
                return 3;
            } else if (len == 7) {
                return 5;
            } else {
                return 11;
            }
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
