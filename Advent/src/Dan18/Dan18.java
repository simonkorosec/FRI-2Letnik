package Dan18;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Dan18 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./src/Dan18/input.txt"));
        String[] commands = new String[41];
        int i = 0;
        while (sc.hasNextLine()) {
            commands[i++] = sc.nextLine();
        }

        Map<String, Long> registers = new HashMap<>();
        i = 0;
        long lastPlayed = -1L;

        while (i < commands.length && i >= 0) {
            String[] com = commands[i].split(" ");

            switch (com[0]) {
                case "set":
                    registers.put(com[1], Long.parseLong(com[2]));
                    break;
                case "add":
                    registers.put(com[1], registers.getOrDefault(com[1], 0L) + Long.parseLong(com[2]));
                    break;
                case "mul":
                    registers.put(com[1], registers.getOrDefault(com[1], 0L) * Long.parseLong(com[2]));
                    break;
                case "mod":
                    registers.put(com[1], registers.getOrDefault(com[1], 0L) % Long.parseLong(com[2]));
                    break;
                case "snd":
                    lastPlayed = registers.get(com[1]);
                    break;
                case "rcv":
                    if (registers.getOrDefault(com[1], 0L) != 0) {
                        registers.put(com[1], lastPlayed);
                        System.out.println(lastPlayed);
                        return;
                    }
                    break;
                case "jgz":
                    if (registers.getOrDefault(com[1], 0L) != 0) {
                        i += registers.getOrDefault(com[2], 0L) - 1;
                    }
                    break;
                default:
                    break;
            }
            i++;
        }
    }
}
