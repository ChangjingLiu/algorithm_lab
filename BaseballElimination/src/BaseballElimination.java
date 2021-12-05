import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Hashtable;

public class BaseballElimination {
    private final String[] teamName; // 球队队名表
    private final int teamNum; // 球队数量
    private final int[][] g;  // 最近待比对战表
    private HashMap<String, Team> teams; // 球队哈希表
    private int V;
    private Hashtable<Integer, Integer> v2id;
    private int flowFromS;
    private int meachesTeamNum;

    private class Team // 球队类
    {
        private final int id;
        private final int wins;
        private final int losses;
        private final int left;

        public Team(int id, int wins, int losses, int left) {
            this.id = id;
            this.wins = wins;
            this.losses = losses;
            this.left = left;
        }
    }

    public BaseballElimination(String filename) {
        if (filename == null)
            throw new java.lang.IllegalArgumentException("the file name is null");

        In in = new In(filename);
        teamNum = Integer.parseInt(in.readLine()); // 球队数量
        teams = new HashMap<String, Team>(); // 球队哈希表
        teamName = new String[teamNum]; // 队名表
        g = new int[teamNum][teamNum]; // 最近待比对战表
        v2id = new Hashtable<Integer, Integer>();
        int id = 0;
        while (in.hasNextLine()) {
            String readLine = in.readLine().trim();   // 读取当前行
            String[] arr = readLine.split("\\s+");
            String key = arr[0];
            int wins = Integer.parseInt(arr[1]);
            int losses = Integer.parseInt(arr[2]);
            int left = Integer.parseInt(arr[3]);
            //StdOut.print("\n");

            Team team = new Team(id, wins, losses, left);

            teamName[id] = key;
            teams.put(key, new Team(id, wins, losses, left));

            for (int i = 0; i < teamNum; i++)   // 最近对战
                g[id][i] = Integer.parseInt(arr[4 + i]);

            id++;
        }
    }

    public int numberOfTeams() {
        return this.teamNum;
    }

    public Iterable<String> teams() {
        Queue<String> list = new Queue<String>();
        for (String s : teamName) {
            list.enqueue(s);
        }
        return list;
    }

    public int wins(String team) {
        return teams.get(team).wins;
    }

    public int losses(String team) {
        return teams.get(team).losses;
    }

    public int remaining(String team) {
        return teams.get(team).left;
    }

    public int against(String team1, String team2) {
        return g[teams.get(team1).id][teams.get(team2).id];
    }

    public boolean isEliminated(String team) {
        FlowNetwork G = constructFlowNetwork(team);
        if (G == null)  // 情形1
            return true;
        else {
            FordFulkerson fordFulkerson = new FordFulkerson(G, 0, V - 1);
            return flowFromS > fordFulkerson.value();
        }

    }

    private FlowNetwork constructFlowNetwork(String team) {
        Team qteam = this.teams.get(team);
        //计算节点数量，2+n-1+C^2_(n-1)
        meachesTeamNum = (this.numberOfTeams() - 1) * (this.numberOfTeams() - 2) / 2;
        this.V = 2 + this.numberOfTeams() - 1 +
                (this.numberOfTeams() - 1) * (this.numberOfTeams() - 2) / 2;
        FlowNetwork G = new FlowNetwork(V);

        int mostWins = qteam.wins + qteam.left;
        this.flowFromS = 0;

        int thisid = qteam.id;
        int s = 0;//源节点下标
        int t = V - 1;//目标节点下标
        int indexI = (this.numberOfTeams() - 1) * (this.numberOfTeams() - 2) / 2;
        int indexJ = indexI;
        int indexMeaches = 1;//比赛节点下标

        for (int i = 0; i < this.numberOfTeams(); i++) {
            if (i == thisid) {
                continue;
            }
            indexI++;
            indexJ = indexI;
            if (mostWins < wins(teamName[i])) {
                return null;
            }

            for (int j = i + 1; j < this.numberOfTeams(); j++) {
                if (j == thisid) {
                    continue;
                }
                indexJ++;
                flowFromS = flowFromS + g[i][j]; // 流
                // 新增关于比赛节点到源节点s的边
                G.addEdge(new FlowEdge(s, indexMeaches, g[i][j]));
                G.addEdge(new FlowEdge(indexMeaches, indexI, Double.MAX_VALUE));
                G.addEdge(new FlowEdge(indexMeaches, indexJ, Double.MAX_VALUE));
                indexMeaches++;
            }
            v2id.put(indexI, i); // 网络下标与球队ID的对应关系
            G.addEdge(new FlowEdge(indexI, t, mostWins - wins(teamName[i])));
        }
        //StdOut.printf(G.toString());
        return G;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!isEliminated(team))   // team未被淘汰
            return null;
        else  // team被淘汰
        {
            Queue<String> certificates = new Queue<>();
            int thisId = teams.get(team).id;
            FlowNetwork G = constructFlowNetwork(team);
            if (G == null) {
                int thisTeamMostWins = wins(team) + remaining(team); // 当前球队最大可获胜数
                for (int i = 0; i < teamNum; i++) {
                    if (i == thisId)
                        continue;

                    if (thisTeamMostWins < wins(teamName[i]))
                        certificates.enqueue(teamName[i]); // 入栈
                }

            } else {
                FordFulkerson fordFulkerson = new FordFulkerson(G, 0, V - 1);
                for (int i = 1 + meachesTeamNum; i < V; i++) // 球队节点
                {
                    if (fordFulkerson.inCut(i)) {
                        int id = this.v2id.get(i); // 网络下标为i的球队的id
                        certificates.enqueue(teamName[id]);
                    }
                }
            }
            return certificates;
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
