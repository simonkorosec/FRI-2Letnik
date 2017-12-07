package Dan4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Dan04 {

    private static boolean isAnagram(String[] vnos) {
        for (int i = 0; i < vnos.length; i++) {
            String s1 = vnos[i];
            for (int j = i + 1; j < vnos.length; j++) {
                String s2 = vnos[j];
                char[] w1 = s1.toCharArray();
                char[] w2 = s2.toCharArray();
                Arrays.sort(w1);
                Arrays.sort(w2);

                if (Arrays.equals(w1, w2))
                    return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(new File("src/Dan4/input.txt"))) {
            int sum = 0;
            int sum2 = 0;
            while (sc.hasNextLine()) {
                String[] s = sc.nextLine().split(" ");
                HashSet<String> hs = new HashSet<>(Arrays.asList(s));


                if (s.length == hs.size())
                    sum++;

                if (isAnagram(s))
                    sum2++;
            }

            System.out.println("Part 1: " + sum);
            System.out.println("Part 2: " + sum2);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
