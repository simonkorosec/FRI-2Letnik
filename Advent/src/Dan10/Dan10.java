package Dan10;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Dan10 {
    public static void main(String[] args) {
        int[] array = new int[256];
        for (int i = 0; i < 256; i++) {
            array[i] = i;
        }

        //int [] lenghts = new int[]{3,4,1,5};
        try {
            Scanner sc = new Scanner(new File("src/Dan10/input.txt"));
            String s = null;
            while (sc.hasNextLine()) {
                s = sc.nextLine();
            }
            if (s == null)
                return;
            ArrayList<Integer> lenghts = new ArrayList<>();
            for (int i = 0; i < s.length(); i++) {
                int value = (int) s.charAt(i);
                lenghts.add(value);
            }
            for (int v : new int[]{17, 31, 73, 47, 23}){
                lenghts.add(v);
            }

            int skip = 0;
            int poz = 0;

            for (int j = 0; j < 64; j++) {
                for (int length : lenghts) {
                    for (int i = 0; i < length / 2; i++) {
                        int prvi = (poz + i) % array.length;
                        int drugi = (poz + length - i - 1) % array.length;
                        int tmp = array[prvi];
                        array[prvi] = array[drugi];
                        array[drugi] = tmp;
                    }
                    poz = (poz + length + skip) % array.length;
                    skip++;
                }
            }
            System.out.println(Arrays.toString(array));
            //System.out.println(array[0] * array[1]);

            int [] densHash = new int[16];
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    densHash[i] ^= array[i*16 + j];
                }
            }

            for (int aDensHash : densHash) {
                System.out.printf("%02x", aDensHash);
            }
            System.out.println();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
