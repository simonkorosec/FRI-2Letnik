import java.util.Scanner;

public class Naloga3 {

    public static void main(String[] args) {
        String usmerjenost = args[0];
        String algoritem = args[1];
        int arg = 0;
        if (algoritem.equals("walks")) {
            arg = Integer.parseInt(args[2]);
        } else if (algoritem.equals("sp")) {
            arg = Integer.parseInt(args[2]);
        }

        Scanner sc = new Scanner(System.in);

        int velikost = Integer.parseInt(sc.nextLine().trim());
        int[][] povezave = new int[velikost][velikost];

        while (sc.hasNextLine()) {
            String[] s = sc.nextLine().trim().split(" ");
            int i = Integer.parseInt(s[0]);
            int j = Integer.parseInt(s[1]);
            povezave[i][j] = 1;
            if (usmerjenost.equals("undirected")) {
                povezave[j][i] = 1;
            }
        }

        Graph graph = new Graph(povezave);

        switch (algoritem) {
            case "walks":
                printMatrix(graph.walks(arg));
                break;
            case "dfs":
                String[] s = graph.dfs();
                System.out.println(s[0]);
                System.out.println(s[1]);
                break;
        }


//        System.out.println();
//        printMatrix(povezave);
//        System.out.println();
//
//        printMatrix(graph.walks(arg));


    }

    private static void printMatrix(int[][] matrix) {
        for (int[] aMatrix : matrix) {
            for (int anAMatrix : aMatrix) {
                System.out.printf("%d ", anAMatrix);
            }
            System.out.println();
        }
    }

}

class Graph {
    private int[][] povezave;
    private int stVozlisc;
    private ResizableArray sledDFS;

    Graph(int[][] povezave) {
        this.povezave = povezave;
        this.stVozlisc = povezave.length;
    }

    int[][] walks(int k) {
        int[][] copy = makeCopy(this.povezave);

        for (int i = 1; i < k; i++) {
            copy = multiplyMatrix(copy, this.povezave);
        }

        return copy;
    }

    String[] dfs() {
        boolean[] obiskani = new boolean[this.stVozlisc];
        this.sledDFS = new ResizableArray();

        for (int i = 0; i < obiskani.length; i++) {
            if (!obiskani[i]) {
                dfs(i, obiskani);
            }
        }

        String[] sled = new String[2];
        StringBuilder sb = new StringBuilder();

        int[] array = sledDFS.getArray();
        for (int anArray : array) {
            if (!sb.toString().contains(Integer.toString(anArray))) {
                sb.append(anArray).append(" ");
            }
        }
        sled[0] = sb.toString().trim();
        sb = new StringBuilder();

        for (int i = array.length - 1; i >= 0; i--) {
            if (!sb.toString().contains(Integer.toString(array[i]))) {
                sb.append(array[i]).append(" ");
            }
        }
        sled[1] = sb.reverse().toString().trim();

        return sled;
    }

    private void dfs(int n, boolean[] obiskani) {
        this.sledDFS.add(n); // Dodaj ko pridem
        obiskani[n] = true;
        for (int i = 0; i < this.povezave[n].length; i++) {
            if (this.povezave[n][i] == 1) {
                if (!obiskani[i]) {
                    dfs(i, obiskani);
                }
            }
        }

        this.sledDFS.add(n); // Dodaj ko zapustim
    }


    private int[][] multiplyMatrix(int[][] mat1, int[][] mat2) {
        int[][] nova = new int[mat1[0].length][mat2.length];

        for (int i = 0; i < mat1.length; i++) {
            for (int j = 0; j < mat2[i].length; j++) {
                int r = 0;
                for (int k = 0; k < mat1[i].length; k++) {
                    r += mat1[i][k] * mat2[k][j];
                }
                nova[i][j] = r;
            }
        }

        return nova;
    }

    private int[][] makeCopy(int[][] vnos) {
        int[][] nova = new int[vnos.length][vnos.length];

        for (int i = 0; i < vnos.length; i++) {
            System.arraycopy(vnos[i], 0, nova[i], 0, vnos[i].length);
        }

        return nova;
    }

}

class ResizableArray {

    int[] array;
    private int size;

    ResizableArray() {
        this.array = new int[10];
        this.size = 0;
    }

    void add(int x) {
        if (this.isFull()) {
            this.resize();
        }
        this.array[this.size] = x;
        this.size++;

    }

    private boolean isFull() {
        return this.size == this.array.length - 1;
    }

    private void resize() {
        int[] tmp = new int[this.array.length * 2];
        System.arraycopy(this.array, 0, tmp, 0, this.array.length);
        this.array = tmp;
    }

    void fixArray() {
        int[] tmp = new int[this.size];
        System.arraycopy(this.array, 0, tmp, 0, this.size);
        this.array = tmp;
    }

    public int[] getArray() {
        this.fixArray();
        return array;
    }
}