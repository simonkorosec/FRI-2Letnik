package Dan13;

import java.util.*;
import java.io.*;
public class Day13 {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File("src/Dan13/input.txt"));
        Day13 d = new Day13();
        d.build(in);
        //d.test();
        d.runA();
        d.runB();
    }

    private Wall[] firewall;

    public void build(Scanner in) {
        firewall = new Wall[100];
        while(in.hasNextLine()) {
            String[] args  = in.nextLine().split(": ");
            firewall[Integer.parseInt(args[0])] = new Wall(Integer.parseInt(args[1]));
        }
    }

    public void test() {
        firewall = new Wall[100];
        firewall[0] = new Wall(3);
        firewall[1] = new Wall(2);
        firewall[4] = new Wall(4);
        firewall[6] = new Wall(4);
    }

    public void runA() {
        System.out.println(sev(0));
    }

    public void runB() {
        int start = 0;
        while(!safe(start)) start++;
        System.out.println(start);
    }

    public int sev(int time) {
        int sev = 0;
        for(int i = 0; i < 100; i++) {
            int ind = i + time;
            if(firewall[i] != null && !firewall[i].open(ind)) {
                sev += i * firewall[i].size;
            }
        }
        return sev;
    }

    public boolean safe(int time) {
        for(int i = 0; i < 100; i++) {
            int ind = i + time;
            if(firewall[i] != null && !firewall[i].open(ind)) {
                return false;
            }
        }
        return true;
    }

    public class Wall {
        public int size;

        public Wall(int size) {
            this.size = size;
        }

        public boolean open(int time) {
            return !(time % ((size-1) * 2) == 0);
        }
    }
}