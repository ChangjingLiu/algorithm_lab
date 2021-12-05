import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieST;

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    TrieST<Integer> dic = new TrieST<Integer>();
    Bag<Integer>[] adj;
    int cols,rows;
    private boolean[] marked;
    private String path;
    private int[] edgeto;
    private Stack<Integer> visitingDices;
    SET<String> validwords;
    BoggleBoard board;

    public BoggleSolver(String[] dictionary){
        int cnt=0;
        for(String s:dictionary){
            dic.put(s,cnt);
            cnt++;
        }
//        StdOut.print(dic.contains("AI"));
    }








    private boolean isExist(int i,int j){
        if(i>=0&&i<rows&&j>=0&&j<cols){
            return true;
        }else {
            return false;
        }
    }
    private void dfs(Integer v,String str,Stack<Integer> visitingDices){

        if(str.length()>=3&& dic.contains(str)){
            validwords.add(str);
        }


        for(Integer s:adj[v]){
            char c = this.board.getLetter(s/cols,s%rows);
            Queue<String> tmp= (Queue<String>) dic.keysWithPrefix(str+c);
            if(!marked[s]&&tmp.size()>=1){
                visitingDices.push(s);
                marked[s]=true;
                if(c=='Q'){
                    dfs(s,str+"QU",visitingDices);
                }else{
                    dfs(s,str+c,visitingDices);
                }


                int index = visitingDices.pop();
                marked[index]=false;

            }

        }

    }
    private void CreatAdj(BoggleBoard board){
        this.board=board;
        cols= this.board.cols();
        rows= this.board.rows();
        adj= (Bag<Integer>[])new Bag[cols*rows];//建立bag，长度为dice个数

        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                int v=i*rows+j;//当前dice的索引
                adj[v] = new Bag<Integer>();//为每个dice建立一个bag
                //八个方向
                if(isExist(i-1,j-1))  adj[v].add((i-1)*cols+(j-1));//left_top
                if(isExist(i-1,j))  adj[v].add((i-1)*cols+(j));//top
                if(isExist(i-1,j+1))  adj[v].add((i-1)*cols+(j+1));//right_top
                if(isExist(i,j+1))  adj[v].add((i)*cols+(j+1));//right
                if(isExist(i+1,j+1))  adj[v].add((i+1)*cols+(j+1));//right_bottom
                if(isExist(i+1,j))  adj[v].add((i+1)*cols+(j));//bottom
                if(isExist(i+1,j-1))  adj[v].add((i+1)*cols+(j-1));//left_bottom
                if(isExist(i,j-1))  adj[v].add((i)*cols+(j-1));//left
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

        for(int i=0;i< adj.length;i++){
            marked=new boolean[adj.length];
            visitingDices = new Stack<Integer>();
            String str="";
            char c=this.board.getLetter(i/cols,i%rows);
            if(c=='Q') {
                str+="QU";
            }else{
                str+=c;
            }
            marked[i]=true;
            dfs(i,str,visitingDices);
            marked[i]=false;
        }

    }
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        validwords=new SET<String>();
        CreatAdj(board);
        return validwords;
    }
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        int len=word.length();
        if(len>=3&&len<=4){
            return 1;
        }
        else if(len==5){
            return 2;
        }
        else if(len==6){
            return 3;
        }else if(len==7){
            return 5;
        }else if(len>=8){
            return 11;
        }else{
            throw new IllegalArgumentException("");
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
