package Dan17;

import java.util.ArrayList;
import java.util.List;

public class Dan17 {
    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part2() {
        int odg = 0;
        int poz = 0;
        int input = 316;

        for (int i = 1; i < 50_000_000; i++) {
            poz = ((poz + input) % i) + 1;
            if (poz == 1){
                odg = i;
            }
        }
        System.out.println(odg);
    }

    private static void part1(){
        int input = 316;
        List<Integer> list = new ArrayList<>();
        list.add(0);
        int poz = 0;

        for (int i = 1; i < 2018; i++) {
            poz = ((poz + input) % i) + 1;
            list.add(poz, i);
        }

        System.out.println(list.get(poz + 1));
    }
}
