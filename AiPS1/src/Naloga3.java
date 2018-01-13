import java.util.Scanner;

public class Naloga3 {

    static int[][] povezave;

    public static void main(String[] args) {
        args = new String[]{"directed", "walks", "3"};


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
        povezave = new int[velikost][velikost];

        for (int k = 0; k < velikost; k++) {
            int i = sc.nextInt();
            int j = sc.nextInt();
            povezave[i][j] = 1;
            if (usmerjenost.equals("undirected")) {
                povezave[j][i] = 1;
            }
        }



        System.out.println();
        for (int i = 0; i < velikost; i++) {
            for (int j = 0; j < velikost; j++) {
                System.out.printf("%d ", povezave[i][j]);
            }
            System.out.println();
        }
        System.out.println();


        /*switch (algoritem){
            case "walks":
                walks(arg);
        }*/

    }

    static void walks(int k) {
        int [][] copy = multiplyMatrix(k);
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[i].length; j++) {
                System.out.printf("%d ", copy[i][j]);
            }
            System.out.println();
        }
    }

    static int[][] multiplyMatrix(int n) {
        int[][] copy = new int[povezave.length][povezave.length];
        System.arraycopy(povezave,0,copy,0,povezave.length);

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < copy.length; i++) {
                for (int j = 0; j < povezave[i].length; j++) {
                    int rez = 0;
                    for (int t = 0; t < copy[i].length; t++) {
                        rez += copy[i][t] * povezave[t][j];
                    }
                    copy[i][j] = rez;
                }
            }
        }
        return copy;
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