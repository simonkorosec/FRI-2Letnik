package Dan5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dan05 {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("src/Dan5/input.txt"))) {
            ArrayList<Integer> array2 = new ArrayList<>();
            while (sc.hasNextInt())
                array2.add(sc.nextInt());

            int steps = 0;
            int poz = 0;

            while(poz < array2.size()){
                int val = array2.get(poz);
                if (val >= 3)
                    array2.set(poz, val - 1);
                else
                    array2.set(poz, val + 1);
                poz += val;
                steps++;
            }

            System.out.printf("Steps: %d", steps);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
