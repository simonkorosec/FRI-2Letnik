package Dan12;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Dan12 {
    public static void main(String[] args) {
        HashSet<String> group = new HashSet<>();
        HashMap<String, Link> linkHashMap = new HashMap<String, Link>();

        try {
            Scanner sc = new Scanner(new File("src/Dan12/input.txt"));

            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().replaceAll(",", "").split(" ");
                linkHashMap.put(line[0], new Link(line));
            }

            int st = 0;
            for (String neki : linkHashMap.keySet()) {
                group.add(neki);
                int prejsni = 0;
                while (prejsni != group.size()) {
                    prejsni = group.size();
                    for (String key : linkHashMap.keySet()) {
                        if (group.contains(key)) {
                            group.addAll(Arrays.asList(linkHashMap.get(key).links));
                        }
                        for (String v : linkHashMap.get(key).links) {
                            if (group.contains(v)) {
                                group.addAll(Arrays.asList(linkHashMap.get(key).links));
                                group.add(key);
                            }
                        }
                    }
                }
                st++;
                for (String k : group) {
                    linkHashMap.remove(k);
                }
                group = new HashSet<>();
            }
            System.out.println(st);
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}


class Link {
    String [] links;

    Link(String[] links) {
        this.links = new String[links.length-2];
        System.arraycopy(links, 2, this.links, 0, links.length - 2);
    }
}