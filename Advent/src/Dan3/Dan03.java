package Dan3;

public class Dan03 {
    public static void main(String[] args) {
        int n = 361527;
        int i = 1;
        while (i*i < n)
            i += 2;
        
        int [] p = new int[4];

        for (int j = 0; j < 4; j++)
            p[j] = i*i - j*(i-1);

        for (int pv : p) {
            int d = Math.abs(pv - n);
            if (d <= (i-1)/2){
                System.out.printf("%d", i-1-d);
                break;
            }
        }
        
        
    }
}
