import java.util.Scanner;

public class Naloga0 {

    public static void main(String[] args) {
        if (args.length == 0){
            System.out.printf("Podaj argumente prijatelj.");
            System.exit(1);
        } else if (args[0].equals("smisel")){
            System.out.printf("Zivljenje je lepo, ce programiras.");
            System.exit(42);
        } else if (args[0].equals("eho")) {
            for (int i = 1; i < args.length; i++) {
                System.out.printf("%s ", args[i]);
            }
        } else if (args[0].equals("macka")) {
            Scanner sc = new Scanner(System.in);
            StringBuilder besedilo = new StringBuilder();
            try {
                while (sc.hasNextLine()) {
                    besedilo.append(sc.nextLine());
                    besedilo.append("\n");
                }
                System.out.printf("%s", besedilo.toString());
            } catch (Exception e) {}
        } else {
            System.out.printf("Podaj argumente prijatelj.");
            System.exit(1);
        }
    }
}
