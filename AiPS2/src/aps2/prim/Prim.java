package aps2.prim;

import java.util.ArrayList;

/**
 * Prim's algorithm to compute the minimum spanning tree on the given graph.
 */
public class Prim {
    int[][] data; // prices of edges in the graph of size n*n
    int n;
    private int cost = 0;

    public Prim(int n) {
        data = new int[n][n];
        this.n = n;
    }

    public Prim(int[][] d) {
        data = d;
        n = data.length;
    }

    /**
     * Adds a bi-directional edge from node i to node j of price d into the cost
     * matrix.
     *
     * @param i Source node
     * @param j Target node
     * @param d Price
     */
    public void addEdge(int i, int j, int d) {
        data[i][j] = d;
        data[j][i] = d;
    }

    /**
     * Returns the total cost of the minimum spanning tree. ie. The sum of all
     * edge weights included in the minimum spanning tree.
     *
     * @return The total cost of the minimum spanning tree.
     */
    public int MSTcost() {
        if (cost == 0) {
            computeMST(2);
        }

        return cost;
    }

    /**
     * Computes the minimum spanning tree on the graph using Prim's algorithm.
     * If two edges share the same price, follow the edge with the smaller target node index.
     *
     * @param source Initial vertex
     * @return An array of visited edges in the correct order of size n-1. The edge in the array from node u to node v is encoded as u*n+v.
     */
    public int[] computeMST(int source) {
        int[] visited = new int[n - 1];
        int[] price = new int[n];
        int[] parent = new int[n];

        ArrayList<Integer> zaporedje = new ArrayList<>();

        boolean[] mst = new boolean[n];

        for (int i = 0; i < n; i++) {
            price[i] = Integer.MAX_VALUE;
            mst[i] = false;
        }

        price[source] = 0;
        parent[source] = -1;

        for (int i = 0; i < n; i++) {
            int u = pickMin(price, mst);
            mst[u] = true;
            zaporedje.add(u);
            for (int v = 0; v < n; v++) {
                if (data[u][v] != 0 && !mst[v] && data[u][v] < price[v]) {
                    price[v] = data[u][v];
                    parent[v] = u;
                }
            }
        }

        int index = 0;
        for (int i : zaporedje) {
            if (parent[i] != -1){
                visited[index] = parent[i] * n + i;
                index++;
            }
        }

        cost = 0;
        for (int aPrice : price) {
            cost += aPrice;
        }

        return visited;
    }

    private int pickMin(int[] price, boolean[] mst) {
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int i = 0; i < price.length; i++) {
            if (!mst[i] && price[i] < min) {
                min = price[i];
                min_index = i;
            }
        }

        return min_index;
    }
}
