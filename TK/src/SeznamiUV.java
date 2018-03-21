import java.util.Scanner;

public class SeznamiUV {
    private Sklad<String> sklad;

    public SeznamiUV() {
        sklad = new Sklad<String>();
    }

    public String processInput(String input) {
        Scanner sc = new Scanner(input);
        String token = sc.next();
        String result = "OK";

        switch (token) {
            case "push":
                if (sc.hasNext())
                    sklad.push(sc.next());
                else
                    result = "Error: please specify a string";
                break;
            case "pop":
                if (!sklad.isEmpty())
                    result = sklad.pop();
                else
                    result = "Error: stack is empty";
                break;
            case "reset":
                while (!sklad.isEmpty())
                    sklad.pop();
                break;
            case "count":
                result = String.format("%d", sklad.count());
                break;
        }
        return result;
    }
}
