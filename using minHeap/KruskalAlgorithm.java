import java.util.ArrayList;
import java.util.List;

public class KruskalAlgorithm {

    private static class Edge {
        int v1;
        int v2;
        int weight;

        public Edge( int v1, int v2, int weight) {
            this.v1 = v1;
            this.v2 = v2;
            this.weight = weight;
        }
    }

    private static class DisjointSet {
        private int[] parent;
        private int[] rank;

        public DisjointSet(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return;
            }
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                rank[rootY] += rank[rootX];
            } else {
                parent[rootY] = rootX;
                rank[rootX] += rank[rootY];
            }
        }
    }

    public static List<Edge> kruskal(int n, List<Edge> edges) {
        List<Edge> mst = new ArrayList<>();
        MinHeap minHeap = new MinHeap(edges.size());
        for (Edge e : edges) {
            minHeap.insert(e.weight);
        }
        DisjointSet ds = new DisjointSet(n);
        while (!minHeap.isEmpty() && mst.size() < n - 1) {
            int weight = minHeap.deleteMin();
            Edge e = null;
            for (Edge edge : edges) {
                if (edge.weight == weight) {
                    e = edge;
                    break;
                }
            }
            if (ds.find(e.v1) != ds.find(e.v2)) {
                mst.add(e);
                ds.union(e.v1, e.v2);
            }
        }
        return mst;
    }

    public static void printMST(List<Edge> mst){
        System.out.println("Minimum Spanning Tree:");
        int min = 0;
        for (Edge e : mst) {
            System.out.println(e.v1 + " -> " + e.v2 + " = " + e.weight);
            min += e.weight;
        }
        System.out.println("----------");
        System.out.println("Min. cost -> "+min);
    }

    public static void main(String[] args) {
        int n = 5;
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1, 2));
        edges.add(new Edge(0, 2, 6));
        edges.add(new Edge(0, 3, 5));
        edges.add(new Edge(1, 2, 3));
        edges.add(new Edge(1, 3, 8));
        edges.add(new Edge(1, 4, 7));
        edges.add(new Edge(2, 4, 9));
        edges.add(new Edge(3, 4, 1));
        List<Edge> mst = kruskal(n, edges);

        printMST(mst);
        
    }
}