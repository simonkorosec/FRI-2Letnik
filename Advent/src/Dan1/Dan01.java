package Dan1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Dan01 {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(new File("src/Dan1/input.txt"));
            String s = sc.nextLine();
            int vsota = 0;
            int premik = s.length() / 2;
            for (int i = 0; i < s.length(); i++) {
                int prvi = Integer.parseInt(String.valueOf(s.charAt(i % s.length())));
                int drugi = Integer.parseInt(String.valueOf(s.charAt((i+premik) % s.length())));
                if (prvi == drugi)
                    vsota += prvi;
            }
            System.out.printf("%d", vsota);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
