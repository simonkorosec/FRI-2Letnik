package Dan11;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class Dan11 {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(new FileReader("src/Dan11/input.txt"));
            String[] ukazi = sc.nextLine().split(",");
            int x = 0, y = 0;

            HashSet<Integer> dist = new HashSet<>();

            for (String smer : ukazi) {
                if (smer.equals("n")) y++;
                if (smer.equals("ne")) x++;
                if (smer.equals("nw")) {
                    x--;
                    y++;
                }
                if (smer.equals("sw")) x--;
                if (smer.equals("se")) {
                    x++;
                    y--;
                }
                if (smer.equals("s")) y--;

                dist.add((Math.abs(x) + Math.abs(y) + Math.abs(x + y)) / 2);
            }

            System.out.println((Math.abs(x) + Math.abs(y) + Math.abs(x + y)) / 2);
            System.out.println(Collections.max(dist));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
