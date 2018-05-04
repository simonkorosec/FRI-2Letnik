package aps2.viterbi;

import java.util.LinkedList;
import java.util.List;

/**
 * @author matevz
 */
public class Viterbi {

    public final double[][] probability;
    final int n; // number of states
    final int m; // number of nodes per state

    private final LinkedList<Node> optPath;

    /**
     * @param probability Matrix of transition probabilities between consecutive
     *                    states of size n columns x m^2 rows, where n is the number of states and
     *                    m the number of nodes per each state.
     *                    <p>
     *                    Example for n=3 and m=2. Let there be nodes A1, A2 on 0th state, B1, B2
     *                    on 1st state, and C1, C2 on 2nd state with probabilities:
     *                    A1 -> B1: 0.2
     *                    A1 -> B2: 0.8
     *                    A2 -> B1: 0.0
     *                    A2 -> B2: 1.0
     *                    B1 -> C1: 0.4
     *                    B1 -> C2: 0.6
     *                    B2 -> C1: 0.3
     *                    B2 -> C2: 0.7
     *                    <p>
     *                    is encoded as:
     *                    probability[0][0] = 0.2;
     *                    probability[0][1] = 0.8;
     *                    probability[0][2] = 0.0;
     *                    probability[0][3] = 1.0;
     *                    probability[1][0] = 0.4;
     *                    probability[1][1] = 0.6;
     *                    probability[1][2] = 0.3;
     *                    probability[1][3] = 0.7;
     *                    <p>
     *                    For details, consult page:
     *                    https://ucilnica.fri.uni-lj.si/mod/assign/view.php?id=13961
     */
    public Viterbi(double probability[][]) {
        this.probability = probability;
        this.n = probability.length;
        this.m = (int) Math.sqrt(probability[0].length);

        // you can add your own initialization here
        optPath = new LinkedList<>();
    }

    /**
     * Calculates path from any initial state to any final state with highest
     * probability using dynamic programming and memoization.
     */
    public final void calculateOptimalPath() {
        int[][] history = this.getMemoizationHistoryMatrix();
        double[][] price = this.getMemoizationPriceMatrix();
        int index = this.indexLast(price);

        for (int level = history.length - 1; level >= 0; level--) {
            optPath.addFirst(new Node(index, price[level][index]));
            index = history[level][index];
        }

    }

    private int indexLast(double[][] price) {
        int index = 0;
        int l =price.length - 1;
        double max = 0.0;

        for (int i = 0; i < price[l].length; i++) {
            double p = price[l][i];
            if (max <p){
                index = i;
                max = p;
            }
        }

        return index;
    }

    /**
     * @return Index of the optimal node in the last state.
     */
    public int getOptimalResultingState() {
        return optPath.peekLast().index;
    }

    /**
     * @return List of nodes in the optimal path.
     */
    public List<Integer> getOptimalPath() {
        LinkedList<Integer> path = new LinkedList<>();
        for (Node n : optPath) {
            path.add(n.index);
        }
        return path;
    }

    /**
     * @return Overall probability of the optimal path.
     */
    public double getOptimalPathProbability() {
        return optPath.peekLast().probability;
    }

    /**
     * @return Intermediate memomization price matrix of size (n+1)*m generated
     * during dynamic programming calls. It contains best
     * probabilities for each state for specific node. Probabilities for the
     * initial state are equal to 1.0/m.
     */
    public double[][] getMemoizationPriceMatrix() {
        double[][] priceMatrix = new double[n + 1][m];
        for (int i = 0; i < priceMatrix[0].length; i++) {
            priceMatrix[0][i] = 1.0 / m;
        }

        for (int i = 1; i < priceMatrix.length; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < m; k++) {
                    int indexPovezave = m * j + k;
                    double prejsnNode = priceMatrix[i - 1][j];
                    double povezava = probability[i - 1][indexPovezave];
                    double p = prejsnNode * povezava;
                    priceMatrix[i][k] = Math.max(priceMatrix[i][k], p);
                }
            }
        }

        return priceMatrix;
    }

    /**
     * @return Intermediate memomization matrix of size (n+1)*m generated
     * during dynamic programming calls. For each state for specific node, it
     * contains the predecessor of the node on the optimal path from the initial
     * state to the final state. Predecessors of nodes in the first state are
     * set to -1.
     */
    public int[][] getMemoizationHistoryMatrix() {
        int[][] historyMatrix = new int[n + 1][m];
        double[][] priceMatrix = new double[n + 1][m];

        for (int i = 0; i < historyMatrix[0].length; i++) {
            historyMatrix[0][i] = -1;
            priceMatrix[0][i] = 1.0 / m;
        }

        for (int i = 1; i < priceMatrix.length; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < m; k++) {
                    int indexPovezave = m * j + k;
                    double prejsnNode = priceMatrix[i - 1][j];
                    double povezava = probability[i - 1][indexPovezave];
                    double p = prejsnNode * povezava;
                    if (p > priceMatrix[i][k]) {
                        priceMatrix[i][k] = p;
                        historyMatrix[i][k] = j;
                    }
                }
            }
        }

        return historyMatrix;
    }

    private class Node {
        final int index;
        final double probability;

        public Node(int index, double probability) {
            this.index = index;
            this.probability = probability;
        }
    }
}
