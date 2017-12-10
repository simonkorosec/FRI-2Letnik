package Dan9;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Dan09 {

    public static void main(String[] args) {
        Scanner sc;
        try {
            sc = new Scanner(new File("src/Dan9/input.txt"));

            String input = sc.nextLine();

            input = input.replaceAll("!.", "");
            int score = 0;
            int depth = 0;
            int garbage = 0;
            boolean inGarbage = false;

            for (int i = 0; i < input.length(); i++) {
                if (inGarbage && input.charAt(i) == '>')
                    inGarbage = false;
                else if (!inGarbage && input.charAt(i) == '<')
                    inGarbage = true;
                else if (inGarbage)
                    garbage++;

                if (!inGarbage && input.charAt(i) == '{')
                    depth++;
                else if (!inGarbage && input.charAt(i) == '}') {
                    score += depth;
                    depth--;
                }
            }
            System.out.println("Groups: " + score);
            System.out.println("Garbage: " + garbage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

