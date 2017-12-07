package Dan6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dan06 {

    static int[] trenutni;
    static ArrayList<int[]> prejsni;

    public static void main(String[] args) {
        try {
            //Scanner sc = new Scanner(new File("src/Dan6/input.txt"));
            //String[] s = sc.nextLine().split(" ");

            //trenutni = new int[s.length];
            trenutni = new int[]{0,2,7,0};
            prejsni = new ArrayList<>();
            /*for (int i = 0; i < s.length; i++) {
                trenutni[i] = Integer.parseInt(s[i]);
            }*/
            add();
            premik();
            int koraki = 0;
            while (!equals()){
                add();
                premik();
                koraki++;
                System.out.println(koraki);
            }
            System.out.println(koraki);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void add() {
        int [] p = new int[trenutni.length];
        int i = 0;
        for (int k : trenutni) {
            p[i++] = k;
        }
        prejsni.add(p);
    }

    static void premik(){
        int index = maxIndex();
        int koliko = trenutni[index];
        trenutni[index] = 0;

        for (int i = koliko; i > 0; i--) {
            trenutni[(index + 1) % trenutni.length]++;
            index++;
        }
        //prejsni.add(trenutni);
    }

    static int maxIndex() {
        int max = 0;
        for (int i = 1; i < trenutni.length; i++) {
            if (trenutni[i] > trenutni[max])
                max = i;
        }
        return max;
    }

    static boolean equals(){

        for (int[] ints : prejsni) {
            for (int j = 0; j < ints.length; j++) {
                if (trenutni[j] != ints[j])
                    return false;
            }
        }
        return true;
    }
}
