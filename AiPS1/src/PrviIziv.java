public class PrviIziv {

    private static int [] generateTable(int n){
        int [] table = new int[n];
        for (int i = 0; i < n; i++) {
            table[i] = i+1;
        }
        return table;
    }

    private static int findLinear(int[] table, int iskaniElement){
        for (int i = 0; i < table.length; i++) {
            if (table[i] == iskaniElement)
                return i;
        }

        return -1;
    }

    private static int findBinary(int[] tabela, int leva, int desna, int iskaniEkement){
        if (leva > desna)
            return -1;
        int mesto = (desna - leva) / 2 + leva;
        int element = tabela[mesto];
        if (iskaniEkement == element)
            return mesto;
        else if (iskaniEkement < element)
            return findBinary(tabela, leva, mesto-1, iskaniEkement);
        else if (iskaniEkement > element)
            return findBinary(tabela, mesto+1, desna, iskaniEkement);

        return -1;
    }

    private static long timeLinear(int n){
        int [] tabela = generateTable(n);

        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            int iskani = (int) (Math.random() * n + 1);
            findLinear(tabela, iskani);
        }
        long executionTime = System.nanoTime() - startTime;

        return executionTime / 1000;
    }

    private static long timeBinary(int n){
        int [] tabela = generateTable(n);

        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            int iskani = (int) (Math.random() * n + 1);
            findBinary(tabela,0, tabela.length, iskani);
        }
        long executionTime = System.nanoTime() - startTime;

        return executionTime / 1000;
    }

    public static void main(String[] args) {

        System.out.printf("   n     |    linearno  |   dvojisko   |\n---------+--------------+---------------\n");

        for (int n = 1000; n <= 100000; n+=1000) {
            System.out.printf("%8d |%13d | %13d\n", n, timeLinear(n), timeBinary(n));
        }
    }
}
