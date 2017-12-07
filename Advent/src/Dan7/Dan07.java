package Dan7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Dan07 {

    public static void main(String[] args) {

        HashMap<String, Prog> programs = new HashMap<>();
        HashSet<String> allProgs = new HashSet<>();
        HashSet<String> parents = new HashSet<>();

        try {
            Scanner sc = new Scanner(new File("src/Dan7/input.txt"));
            while (sc.hasNextLine()) {
                String[] str = sc.nextLine().replaceAll(",", "").split("\\s+");
                allProgs.add(str[0]);

                programs.put(str[0], new Prog(str[0], Integer.parseInt(str[1].replaceAll("[()]", ""))));

                if (str.length > 2) {
                    parents.addAll(Arrays.asList(str).subList(3, str.length));
                    programs.get(str[0]).setSons(str);
                }
            }

            allProgs.removeAll(parents);
            System.out.println("Root: " + allProgs);

            String root = "rsalq";
            for (String s : allProgs) {
                root = s;
            }

            for (String o : programs.get(root).sons) {
                System.out.println(o + "("+programs.get(o).weight+")" + " -> " + teza(programs, o));
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static int teza(HashMap<String, Prog> programs, String o) {
        int sum = programs.get(o).weight;

        if (programs.get(o).sons != null){
            for (String k : programs.get(o).sons) {
                sum += teza(programs, k);
            }
        }

        return sum;
    }

}

class Prog {
    String ime;
    String [] sons;
    int weight;

    Prog(String ime, int weight) {
        this.ime = ime;
        this.weight = weight;
        sons = null;
    }

    void setSons(String [] sons){
        this.sons = new String[sons.length - 3];
        System.arraycopy(sons, 3, this.sons, 0, sons.length-3);
    }
}
