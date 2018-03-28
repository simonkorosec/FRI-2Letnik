
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
            case "push":
                if (sc.hasNext()) {
                    StringBuilder tmp = new StringBuilder(sc.next());
                    if (tmp.toString().startsWith("\"")) {
                        tmp = new StringBuilder(tmp.substring(1));
                        while (sc.hasNext()) {
                            tmp.append(" ").append(sc.next());
                        }
                        tmp = new StringBuilder(tmp.substring(0, tmp.length() - 1));
                    }
                    sklad.push(tmp.toString());
                } else {
                    result = "Error: please specify a string";
                }
                break;
            case "pop":
                if (!sklad.isEmpty()) {
                    result = sklad.pop();
                } else {
                    result = "Error: stack is empty";
                }
                break;
            case "count":
                result = String.format("%d", sklad.count());
                break;
            case "reset":
                while (!sklad.isEmpty()) {
                    sklad.pop();
                }
                break;
            case "search":
                if (sc.hasNext()) {
                    result = String.format("%d", sklad.search(sc.next()));
                } else {
                    result = "Error: please specify a string";
                }
                break;
            case "top":
                if (sc.hasNext()) {
                    if (!sklad.isEmpty()) {
                        if (sklad.top(sc.next())) {
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

            case "pq_add":
                if (sc.hasNext()) {
                    String val = sc.next();
                    vrsta.add(val);
                } else
                    result = "Error: please specify a string";
                break;

            case "pq_remove_first":
                if (!vrsta.isEmpty())
                    result = vrsta.removeFirst();
                else
                    result = "Error: priority queue is empty";
                break;

            case "pq_get_first":
                if (!vrsta.isEmpty())
                    result = vrsta.getFirst();
                else
                    result = "Error: priority queue is empty";
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
}
