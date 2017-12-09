package Dan6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Dan06 {

    private static int [] trenutni;
    private static ArrayList<int[]> prejsni;

    public static void main(String[] args) {
        prejsni = new ArrayList<>();

        try (Scanner sc = new Scanner(new File("src/Dan6/input.txt"))) {
            String [] s = sc.nextLine().split("\\s+");
            System.out.println(Arrays.toString(s));
            trenutni = Arrays.stream(s).mapToInt(Integer::parseInt).toArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int i = 0;

        do {
            add();
            pomik();
            i++;
        } while (equals());

        System.out.println(i);

    }

    private static void add() {
        int [] p = new int[trenutni.length];
        System.arraycopy(trenutni, 0, p, 0, trenutni.length);

        prejsni.add(p);
    }

    private static boolean equals() {
        int k = 0;
        for (int[] t : prejsni) {
            if (Arrays.equals(trenutni, t)) {
                System.out.println(prejsni.size() - k);
                return false;
            }
            k++;
        }
        return true;

    }

    private static void pomik() {
        int index = getMax();
        int koliko = trenutni[index];
        trenutni[index] = 0;
        for (int i = koliko; i > 0; i--){
            index = (index + 1) % trenutni.length;
            trenutni[index]++;
        }
    }

    private static int getMax() {
        int max = 0;
        for (int i = 1; i < trenutni.length; i++) {
            if (trenutni[i] > trenutni[max])
                max = i;
        }
        return max;
    }

}
