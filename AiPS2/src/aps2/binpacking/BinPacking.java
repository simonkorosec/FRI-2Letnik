package aps2.binpacking;

import java.util.*;

/**
 * Bin packing algorithm to compute the minimum number of bins to store all the
 * * given items.
 */
public class BinPacking {
    int[] items; // sizes of items to store
    int m; // size of each bin

    /* spremenljivke za exact */
    private int[] bins;
    private int[] razporeditev;
    private int[] bestRazporeditev;


    public BinPacking(int[] items, int m) {
        this.items = items;
        this.m = m;

        razporeditev = new int[items.length];
        bestRazporeditev = new int[items.length];
        int velikost = 0;
        for (int item : items) {
            velikost += item;
        }
        velikost = (int) Math.ceil(velikost / (m * 1.0));
        bins = new int[velikost];
    }

    /**
     * Computes the strictly minimal number of bins for the given items.
     *
     * @return Array of size n which stores the index of bin, where each item is stored in.
     */
    public int[] binPackingExact() {
        for (int i = 0; i < razporeditev.length; i++) {
            razporeditev[i] = -1;
            bestRazporeditev[i] = i;
        }

        recursive(0);
        return bestRazporeditev;
    }

    private void recursive(int itemID) {
        if (itemID >= items.length) {
            if (betterSolution()) {
                bestRazporeditev = razporeditev.clone();
            }
            return;
        }

        for (int i = 0; i < bins.length; i++) {
            putIntoBin(itemID, i);
            recursive(itemID + 1);
            removeFromBin(itemID, i);
        }
    }

    private boolean betterSolution() {
        for (int bin : bins) {
            if (bin > m) {
                return false;
            }
        }

        HashSet<Integer> bestHS = new HashSet<>();
        HashSet<Integer> currHS = new HashSet<>();

        for (int i = 0; i < razporeditev.length; i++) {
            currHS.add(razporeditev[i]);
            bestHS.add(bestRazporeditev[i]);
        }
        int best = bestHS.size();
        int curr = currHS.size();

        return curr < best;
    }

    private void removeFromBin(int itemID, int i) {
        razporeditev[itemID] = -1;
        bins[i] -= items[itemID];
    }

    private void putIntoBin(int itemID, int i) {
        razporeditev[itemID] = i;
        bins[i] += items[itemID];
    }

    /**
     * Uses heuristics (e.g. first-fit, best-fit, ordered first-fit etc.) to
     * compute the minimal number of bins for the given items. The smaller the
     * number, the more tests you will pass.
     *
     * @return Array of size n which stores the index of bin, where each item is stored in.
     */
    public int[] binPackingApproximate() {
        int binNum = 0;
        bins = new int[items.length];

        int[] order = order();

        for (int i : order) {
            int velikost = items[i];

            int j;
            for (j = 0; j < binNum; j++) {
                if (bins[j] >= velikost){
                    bins[j] -= velikost;
                    bestRazporeditev[i] = j;
                    break;
                }
            }

            if (j == binNum){
                bins[binNum] = m - velikost;
                bestRazporeditev[i] = binNum;
                binNum++;
            }
        }

        return bestRazporeditev;
    }

    private int[] order() {
        TreeMap<Integer, Integer> order = new TreeMap<>();
        for (int i = 0; i < items.length; i++) {
            order.put(i, items[i]);
        }

        TreeMap<Integer, Integer> neworder = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int c = order.get(o2).compareTo(order.get(o1));
                if (c == 0) return 1;
                return c;
            }
        });
        neworder.putAll(order);

        int [] zap = new int[neworder.size()];
        int j = 0;
        for (int i : neworder.keySet()) {
            zap[j++] = i;
        }
        return zap;
    }

}
