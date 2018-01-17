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

        int velikost = sc.nextInt();
        int[][] povezave = new int[velikost][velikost];

        while (sc.hasNextLine()){
            String[] s = sc.nextLine().split(" ");
            int i = Integer.parseInt(s[0]);
            int j = Integer.parseInt(s[1]);
            povezave[i][j] = 1;
            if (usmerjenost.equals("undirected")) {
                povezave[j][i] = 1;
            }
        }

        Graph graph = new Graph(povezave);

        switch (algoritem){
            case "walks":
                printMatrix(graph.walks(arg));
                break;
        }




//        System.out.println();
//        printMatrix(povezave);
//        System.out.println();
//
//        printMatrix(graph.walks(arg));


    }

    static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%d ", matrix[i][j]);
            }
            System.out.println();
        }
    }

}

class Graph {
    int[][] povezave;

    public Graph(int[][] povezave) {
        this.povezave = povezave;
    }

    int[][] walks(int k) {
        int[][] copy = makeCopy(this.povezave);

        for (int i = 1; i < k; i++) {
            copy = multiplyMatrix(copy, this.povezave);
        }

        return copy;
    }

    int[][] multiplyMatrix(int[][] mat1, int[][] mat2) {
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

    int[][] makeCopy(int[][] vnos){
        int[][] nova = new int[vnos.length][vnos.length];

        for (int i = 0; i < vnos.length; i++) {
            for (int j = 0; j < vnos[i].length; j++) {
                nova[i][j] = vnos[i][j];
            }
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
}