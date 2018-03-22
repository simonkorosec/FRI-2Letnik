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
                if (sc.hasNext()) {
                    StringBuilder sb = new StringBuilder();
                    while (sc.hasNext()) {
                        sb.append(sc.next()).append(" ");
                    }
                    sklad.push(sb.toString().trim().replace("\"", ""));
                } else {
                    result = "Error: please specify a string";
                }
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
            case "top":
                if (sc.hasNext()) {
                    if (sklad.isEmpty()){
                        result = "Error: stack is empty";
                        break;
                    }

                    StringBuilder sb = new StringBuilder();
                    while (sc.hasNext()) {
                        sb.append(sc.next()).append(" ");
                    }
                    String primerjava = sb.toString().trim().replace("\"", "");
                    String top = sklad.peek();

                    result = (top.equals(primerjava)) ? "OK" : "Error: wrong element";

                } else {
                    result = "Error: please specify a string";
                }
                break;
            case "search":
                if (sc.hasNext()) {
                    if (sklad.isEmpty()){
                        result = "Error: stack is empty";
                        break;
                    }

                    StringBuilder sb = new StringBuilder();
                    while (sc.hasNext()) {
                        sb.append(sc.next()).append(" ");
                    }
                    String iskanje = sb.toString().trim().replace("\"", "");
                    int poz = sklad.search(iskanje);
                    result = String.format("%d", poz);

                } else {
                    result = "Error: please specify a string";
                }
                break;
        }
        return result;
    }
}
