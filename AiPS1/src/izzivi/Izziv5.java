package izzivi;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Izziv5 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Vnesite velikost polja: ");
        int n = sc.nextInt();
        Oseba [] osebe = new Oseba[n];
        for (int i = 0; i < n; i++) {
            osebe[i] = new Oseba();
        }

        while (true){
            Oseba [] tmp = osebe;
            System.out.println(Arrays.toString(tmp));
            System.out.print("Vnesite atribut: ");
            Oseba.setAtr(sc.nextInt());
            System.out.print("Vnesite smer urejanja: ");
            Oseba.setSmer(sc.nextInt());
            bubbleSort(tmp);
            System.out.print("Želite ponoviti? Y/N");
            if (sc.next().equals("N")){
                break;
            }
        }
    }

    private static void bubbleSort(Oseba[] array) {
        int locilo;
            izpis(array, -1);
        int zadnjiSwap = 1;
        for (int i = 1; i < array.length; i++) {
            boolean u = true;
            int trenutni = 0;
            for (int j = array.length - 1; j >= zadnjiSwap; j--) {
                if (array[j - 1].compareTo(array[j]) > 0) {
                    Oseba tmp = array[j-1];
                    array[j-1] = array[j];
                    array[j] = tmp;

                    trenutni = j;
                    u = false;
                }
            }
            if (u) continue;
            zadnjiSwap = trenutni;
            locilo = zadnjiSwap;
            izpis(array, locilo);
        }

    }

    private static void izpis(Oseba[] array, int locilo) {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < array.length; k++) {
            sb.append(array[k]).append(" ");
            if (locilo == k + 1)
                sb.append("| ");
        }
        System.out.println(sb.toString().trim());
    }

}


