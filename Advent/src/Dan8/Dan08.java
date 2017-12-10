package Dan8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Dan08 {

    public static void main(String[] args) {
        HashMap<String, Integer> registri = new HashMap<>();
        int max = 0;

        try {
            Scanner sc = new Scanner(new File("src/Dan8/input.txt"));

            while (sc.hasNextLine()){
                String [] ukaz = sc.nextLine().split("\\s+");
                String reg1 = ukaz[0];
                int koliko = Integer.parseInt(ukaz[2]);
                String reg2 = ukaz[4];

                if (!registri.containsKey(reg1))
                    registri.put(reg1, 0);
                if (!registri.containsKey(reg2))
                    registri.put(reg2, 0);

                String pog1 = ukaz[5];
                int pog2 = Integer.parseInt(ukaz[6]);
                if (pogoj(registri.get(reg2), pog1, pog2)){
                    switch (ukaz[1]){
                        case "inc":
                            registri.put(reg1, registri.get(reg1)+koliko);
                            break;
                        case "dec":
                            registri.put(reg1, registri.get(reg1)-koliko);
                            break;

                    }
                }

                HashSet<Integer> tmp = new HashSet<>(registri.values());
                tmp.add(max);
                max = Collections.max(tmp);

            }

            System.out.println(max);
            System.out.println(Collections.max(registri.values()));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private static boolean pogoj(Integer integer, String pog1, int pog2) {
        switch (pog1){
            case ">":
                return integer > pog2;
            case ">=":
                return integer >= pog2;
            case "==":
                return integer == pog2;
            case "!=":
                return integer != pog2;
            case "<=":
                return integer <= pog2;
            case "<":
                return integer < pog2;
            default:
                return false;
        }
    }
}
