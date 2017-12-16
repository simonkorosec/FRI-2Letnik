package Dan13;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Dan13 {
    public static void main(String[] args) {
        int sum = 0;
        HashMap<Integer, Integer> layers = new HashMap<>();
        HashMap<Integer, Integer> pozition = new HashMap<>();
        HashMap<Integer, Integer> smer = new HashMap<>();


        try {
            Scanner sc = new Scanner(new File("src/Dan13/test.txt"));
            while (sc.hasNextLine()){
                String [] s = sc.nextLine().split(":");
                layers.put(Integer.parseInt(s[0]), Integer.parseInt(s[1].trim()));
                pozition.put(Integer.parseInt(s[0]), 0);
                smer.put(Integer.parseInt(s[0]), 1);
            }
            sc.close();

            int max = Collections.max(layers.keySet());
            int wait = 0;

            while (true) {
                int c = wait;
                for (int lokacija = -1; lokacija <= max; ) {
                    if (c < 1)
                        lokacija++;
                    else
                        c--;

                    if (pozition.containsKey(lokacija) && pozition.get(lokacija) == 0) {
                        sum += (lokacija * layers.get(lokacija));
                    }

                    for (int k : layers.keySet()) {
                        int p = pozition.get(k) + smer.get(k);
                        if (p == 0)
                            smer.put(k, 1);
                        if (p == layers.get(k) - 1)
                            smer.put(k, -1);

                        pozition.put(k, p);
                    }
                }
                System.out.println(wait + " " + sum);
                if (sum == 0)
                    break;
                wait++;
                sum = 0;
                for (int k : pozition.keySet()) {
                    pozition.put(k, 0);
                    smer.put(k, 1);
                }
            }

            System.out.println(wait);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
