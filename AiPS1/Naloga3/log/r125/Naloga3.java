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
            case "bfs":
                System.out.println(graph.bfs());
                break;
            case "sp":
                graph.sp(arg);
                break;
            case "comp":
                graph.comp(usmerjenost);
                break;
        }


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
    private int[][] povezave;           // tabela povezav v grafu
    private int stVozlisc;              // število vozlišč
    private ResizableArray vhodDFS;     // array vhodnih vozlišč za DFS
    private ResizableArray izhodDFS;    // array izhodnih vozlišč za DFS
    private StringBuilder izhodBFS;     // array izhodnih vozlišč za BFS
    private int[] dolzina;              // array v koliko korakih pride do vozlišča za SP
    private int[] komponente;            // array za skupine komponent


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
        this.vhodDFS = new ResizableArray();
        this.izhodDFS = new ResizableArray();

        for (int i = 0; i < obiskani.length; i++) {
            if (!obiskani[i]) {
                dfs(i, obiskani);
            }
        }

        String[] sled = new String[2];

        StringBuilder sb = new StringBuilder();
        int[] array = vhodDFS.getArray();
        for (int anArray : array) {
            sb.append(anArray).append(" ");
        }
        sled[0] = sb.toString().trim();

        sb = new StringBuilder();
        array = izhodDFS.getArray();
        for (int anArray : array) {
            sb.append(anArray).append(" ");
        }
        sled[1] = sb.toString().trim();

        return sled;
    }

    private void dfs(int v, boolean[] obiskani) {
        this.vhodDFS.enqueue(v); // Dodaj ko pridem
        obiskani[v] = true;
        for (int i = 0; i < this.povezave[v].length; i++) {
            if (this.povezave[v][i] == 1 && !obiskani[i]) {
                dfs(i, obiskani);
            }
        }

        this.izhodDFS.enqueue(v); // Dodaj ko zapustim
    }

    public String bfs() {
        izhodBFS = new StringBuilder();
        boolean obiskani[] = new boolean[this.stVozlisc];

        for (int i = 0; i < this.stVozlisc; i++) {
            if (!obiskani[i]) {
                bfs(i, obiskani);
            }
        }


        return izhodBFS.toString();
    }

    private void bfs(int n, boolean[] obiskani) {
        ResizableArray sledBFS = new ResizableArray();
        sledBFS.enqueue(n);
        obiskani[n] = true;
        this.dolzina[n] = 0;

        while (!sledBFS.isEmpty()) {
            int v = sledBFS.dequeue();
            if (this.izhodBFS != null) {
                this.izhodBFS.append(v).append(" ");
            }

            for (int i = 0; i < this.povezave[v].length; i++) {
                if (this.povezave[v][i] == 1 && !obiskani[i]) {
                    obiskani[i] = true;
                    sledBFS.enqueue(i);

                    this.dolzina[i] = this.dolzina[v] + 1;
                }
            }
        }
    }

    void sp(int v) {
        boolean obiskani[] = new boolean[this.stVozlisc];
        this.dolzina = new int[this.stVozlisc];
        for (int i = 0; i < this.dolzina.length; i++) {
            this.dolzina[i] = -1;
        }


        bfs(v, obiskani);

        for (int i = 0; i < this.stVozlisc; i++) {
            System.out.printf("%d %d\n", i, this.dolzina[i]);
        }

    }

    public void comp(String usmerjenost) {
        if (usmerjenost.equals("undirected")) {
            undirectedComp();
        } else {
            directedComp();
        }
    }

    private void directedComp() {
        boolean[] obiskani = new boolean[this.stVozlisc];
        this.izhodDFS = new ResizableArray();

        for (int i = 0; i < obiskani.length; i++) {
            if (!obiskani[i]) {
                dfsDirComp(i, obiskani);
            }
        }
        obiskani = new boolean[this.stVozlisc];
        int[] vrstniRed = this.izhodDFS.getArray();

        for (int i = 0; i < vrstniRed.length / 2; i++) {
            int tmp = vrstniRed[i];
            vrstniRed[i] = vrstniRed[vrstniRed.length - 1 - i];
            vrstniRed[vrstniRed.length - 1 - i] = tmp;
        }

        obrniGraf();
        this.komponente = new int[this.stVozlisc];
        for (int v : vrstniRed) {
            if (!obiskani[v]) {
                dfDirComp2(v, obiskani, v);
            }
        }

        StringBuilder[] sb = new StringBuilder[this.stVozlisc];

        izpisKomponent(sb);
        sb = uredi(sb);

        for (StringBuilder aSb : sb) {
            if (aSb != null) {
                System.out.println(aSb.toString());
            }
        }


    }

    private StringBuilder[] uredi(StringBuilder[] in) {
        int j = 0;
        for (StringBuilder anIn : in) {
            if (anIn != null) {
                j++;
            }
        }

        StringBuilder[] sb = new StringBuilder[j];
        j = 0;
        for (StringBuilder anIn : in) {
            if (anIn != null) {
                sb[j++] = anIn;
            }
        }

        for (int i = 1; i < sb.length; i++) {
            int k = Integer.parseInt(sb[i-1].toString().split(" ")[0]);
            int h = Integer.parseInt(sb[i].toString().split(" ")[0]);

            if (k > h){
                StringBuilder tmp = sb[i-1];
                sb[i-1] = sb[i];
                sb[i] = tmp;
            }
        }

        return sb;
    }

    private void dfDirComp2(int v, boolean[] obiskani, int comp) {
        obiskani[v] = true;
        this.komponente[v] = comp;
        for (int i = 0; i < this.povezave[v].length; i++) {
            if (this.povezave[v][i] == 1 && !obiskani[i]) {
                dfDirComp2(i, obiskani, comp);
            }
        }
    }

    private void dfsDirComp(int v, boolean[] obiskani) {
        obiskani[v] = true;
        for (int i = 0; i < this.povezave[v].length; i++) {
            if (this.povezave[v][i] == 1 && !obiskani[i]) {
                dfsDirComp(i, obiskani);
            }
        }
        this.izhodDFS.enqueue(v); // Dodaj ko zapustim
    }

    private void obrniGraf() {
        int[][] tmp = new int[this.povezave.length][this.povezave.length];
        for (int i = 0; i < this.povezave.length; i++) {
            for (int j = 0; j < this.povezave[i].length; j++) {
                tmp[j][i] = this.povezave[i][j];
            }
        }
        this.povezave = tmp;
    }

    private void izpisKomponent(StringBuilder[] sb) {
        for (int i = 0; i < this.stVozlisc; i++) {
            int k = this.komponente[i];
            if (sb[k] == null) {
                sb[k] = new StringBuilder();
            }
            sb[k].append(i).append(" ");
        }
    }

    private void undirectedComp() {
        boolean[] obiskani = new boolean[this.stVozlisc];
        this.komponente = new int[this.stVozlisc];
        int comp = 0;
        for (int i = 0; i < obiskani.length; i++) {
            if (!obiskani[i]) {
                dfsUndirComp(i, obiskani, comp);
                comp++;
            }
        }

        StringBuilder[] sb = new StringBuilder[comp];

        izpisKomponent(sb);

        for (StringBuilder aSb : sb) {
            System.out.println(aSb.toString());
        }

    }

    private void dfsUndirComp(int v, boolean[] obiskani, int comp) {
        obiskani[v] = true;
        this.komponente[v] = comp;
        for (int i = 0; i < this.povezave[v].length; i++) {
            if (this.povezave[v][i] == 1 && !obiskani[i]) {
                dfsUndirComp(i, obiskani, comp);
            }
        }
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
    private int back;
    private int front;
    private int size;
    private int count;

    ResizableArray() {
        this.array = new int[10];
        this.size = 10;
        this.count = 0;
        this.front = 0;
        this.back = 0;
    }

    void enqueue(int x) {
        if (this.isFull()) {
            this.resize();
        }

        this.array[this.back] = x;
        this.back = this.back + 1;
        this.count++;
    }

    public int dequeue() {
        int tmp = this.array[this.front];
        this.array[this.front] = 0;
        this.front = this.front + 1;
        this.count--;
        return tmp;
    }

    private boolean isFull() {
        return this.back == this.size;
    }

    private void resize() {
        int[] tmp = new int[this.array.length * 2];
        System.arraycopy(this.array, 0, tmp, 0, this.array.length);
        this.array = tmp;
        this.size = this.array.length;
    }

//    void fixArray() {
//        int[] tmp = new int[this.size];
//        System.arraycopy(this.array, this.front, tmp, 0, this.size - 1);
//        this.array = tmp;
//        this.front = 0;
//        this.back = this.size - 1;
//    }

    public int[] getArray() {
        int[] tmp = new int[this.count];
        System.arraycopy(this.array, this.front, tmp, 0, tmp.length);
        return tmp;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int anArray : this.array) {
            sb.append(anArray).append("  ");
        }
        sb.append("}");

        return sb.toString();
    }

}
