package Dan16;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dan16 {
    public static void main(String[] args) {
        try {
            String[] ukazi = new Scanner(new File("src/Dan16/input.txt")).nextLine().split(",");
            String prog = "abcdefghijklmnop";

            System.out.println(dance(ukazi, prog)); // part 1

            System.out.println(part2(ukazi, prog)); // part 2


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String part2(String[] ukazi, String prog) {
        List<String> strings = new ArrayList<>();

        boolean stopDance = false;
        for (int i = 0; i < 1000000000L; i++) {
            if (!stopDance) {
                prog = dance(ukazi, prog);
                if (!strings.contains(prog)) {
                    strings.add(prog);
                } else {
                    stopDance = true;
                }
            }
        }


        return strings.get(1000000000 % strings.size() -1);
    }

    private static String dance(String[] ukazi, String prog) {
        String tmp = prog;

        for (String u : ukazi) {
            char p = u.charAt(0);
            if (p == 's'){
                Integer spin = Integer.valueOf(u.substring(1, u.length()));
                tmp = tmp.substring(16 - spin, 16) + tmp.substring(0, 16 - spin);
            } else if (p == 'x') {
                String[] tokens = u.substring(1, u.length()).split("/");
                Integer A = Integer.valueOf(tokens[0]);
                Integer B = Integer.valueOf(tokens[1]);
                StringBuilder sb = new StringBuilder(tmp);
                char c1 = tmp.charAt(A);
                char c2 = tmp.charAt(B);
                sb.setCharAt(A, c2);
                sb.setCharAt(B, c1);
                tmp = sb.toString();
            } else if (p == 'p') {
                String[] tokens = u.substring(1, u.length()).split("/");
                char c1 = tokens[0].charAt(0);
                char c2 = tokens[1].charAt(0);
                int i1 = tmp.indexOf(tokens[0]);
                int i2 = tmp.indexOf(tokens[1]);
                StringBuilder sb = new StringBuilder(tmp);
                sb.setCharAt(i1, c2);
                sb.setCharAt(i2, c1);
                tmp = sb.toString();
            }
        }
        return tmp;
    }

}
