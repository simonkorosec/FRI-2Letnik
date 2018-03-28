
import java.util.Scanner;

public class SeznamiUV {

    private Sklad<String> sklad;
    private PrioritetnaVrsta<String> vrsta;

    public SeznamiUV() {
        sklad = new Sklad<>();
        vrsta = new PrioritetnaVrsta<>();
    }

    public String processInput(String input) {
        Scanner sc = new Scanner(input);
        String token = sc.next();
        String result = "OK";
        switch (token) {
            case "s_add":
                if (sc.hasNext()) {
                    sklad.add(getInput(sc));
                } else {
                    result = "Error: please specify a string";
                }
                break;
            case "s_remove_first":
                if (!sklad.isEmpty()) {
                    result = (String) sklad.removeFirst();
                } else {
                    result = "Error: stack is empty";
                }
                break;
            case "s_size":
                result = String.format("%d", sklad.size());
                break;
            case "s_depth":
                result = String.format("%d", sklad.depth());
                break;
            case "s_reset":
                while (!sklad.isEmpty()) {
                    sklad.pop();
                }
                break;
            case "s_search":
                if (sc.hasNext()) {
                    result = String.format("%d", sklad.search(getInput(sc)));
                } else {
                    result = "Error: please specify a string";
                }
                break;
            case "s_top":
                if (sc.hasNext()) {
                    if (!sklad.isEmpty()) {
                        if (sklad.top(getInput(sc))) {
                            result = "OK";
                        } else {
                            result = "Error: wrong element";
                        }

                    } else {
                        result = "Error: stack is empty";
                    }
                } else {
                    result = "Error: please specify a string";
                }
                break;
            case "s_get_first":
                if (!sklad.isEmpty()) {
                    result = (String) sklad.getFirst();
                } else {
                    result = "Error: stack is empty";
                }
                break;
            case "pq_add":
                if (sc.hasNext()) {
                    vrsta.add(getInput(sc));
                } else {
                    result = "Error: please specify a string";
                }

                break;
            case "pq_remove_first":
                if (!vrsta.isEmpty()) {
                    result = vrsta.removeFirst();
                } else {
                    result = "Error: priority queue is empty";
                }
                break;
            case "pq_get_first":
                if (!vrsta.isEmpty()) {
                    result = vrsta.getFirst();
                } else {
                    result = "Error: priority queue is empty";
                }
                break;
            case "pq_size":
                result = String.format("%d", vrsta.size());
                break;
            case "pq_depth":
                result = String.format("%d", vrsta.depth());
                break;
            case "pq_isEmpty":
                result = (vrsta.isEmpty()) ? "" : "not ";
                result = String.format("Priority queue is %sempty", result);
                break;
            default:
                break;
        }
        return result;
    }

    private String getInput(Scanner sc){
        StringBuilder tmp = new StringBuilder(sc.next());
        if (tmp.toString().startsWith("\"")) {
            tmp = new StringBuilder(tmp.substring(1));
            while (sc.hasNext()) {
                tmp.append(" ").append(sc.next());
            }
            tmp = new StringBuilder(tmp.substring(0, tmp.length() - 1));
        }

        return tmp.toString();
    }
}

