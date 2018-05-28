package seznami;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

public class SeznamiUV {

    private final HashMap<String, Seznam<String>> seznami;
    private Seznam<String> seznam;

    public SeznamiUV() {
        seznami = new HashMap<>();
        seznami.put("pv", new PrioritetnaVrsta<>());
        seznami.put("sk", new Sklad<>());
        seznami.put("bst", new Bst<>());
        seznami.put("bk", new BinomskaKopica<>());
    }

    public String processInput(String input) {
        Scanner sc = new Scanner(input);
        String token;
        String result = "OK";
        if (sc.hasNext()) {
            token = sc.next();
        } else {
            return "Error: enter command";
        }
        if (token.equalsIgnoreCase("exit")){
            return "Have a nice day.";
        }
        if (!token.equals("use") && (null == seznam)) {
            return "Error: please specify a data structure (use {pv|sk|bst|bk})";
        }
        try {
            switch (token) {
                case "use":
                    if (sc.hasNext()) {
                          seznam = seznami.get(sc.next());
                        if (null == seznam) {
                            result = "Error: please specify a correct data structure type {pv|sk|bst|bk}";
                        }
                    } else {
                        result = "Error: please specify a data structure type {pv|sk|bst|bk}";
                    }
                    break;
                case "add":
                    if (sc.hasNext()) {
                        seznam.add(sc.next());
                    } else {
                        result = "Error: please specify a string";
                    }
                    break;
                case "remove_first":
                    result = seznam.removeFirst();
                    break;
                case "get_first":
                    result = seznam.getFirst();
                    break;
                case "size":
                    result = seznam.size() + "";
                    break;
                case "depth":
                    result = seznam.depth() + "";
                    break;
                case "is_empty":
                    result = "Data structure is " + (seznam.isEmpty() ? "" : "not ") + "empty.";
                    break;
                case "reset":
                    while (!seznam.isEmpty()) {
                        seznam.removeFirst();
                    }
                    break;
                case "exists":
                    if (sc.hasNext()) {
                        result = "Element " + (seznam.exists(sc.next()) ? "exists " : "doesn't exist ") + "in data structure.";
                    } else {
                        result = "Error: please specify a string";
                    }
                    break;
                case "remove":
                    if (sc.hasNext()) {
                        result = seznam.remove(sc.next());
                    } else {
                        result = "Error: please specify a string";
                    }
                    break;
                case "asList":
                    List<String> list = seznam.asList();
                    StringBuilder s = new StringBuilder();
                    for (String aList : list) {
                        s.append(aList).append(" ");
                    }
                    result = s.toString().trim();
                    break;
                case "print":
                    seznam.print();
                    break;
                case "save":
                    if (sc.hasNext()) {
                        seznam.save(new FileOutputStream(sc.next()));
                    } else {
                        result = "Error: please specify a file name";
                    }
                    break;
                case "restore":
                    if (sc.hasNext()) {
                        seznam.restore(new FileInputStream(sc.next()));
                    } else {
                        result = "Error: please specify a file name";
                    }
                    break;
                default:
                    result = "Error: invalid command";
            }
        }  catch (IllegalArgumentException e) {
            result = "Error: Duplicated entry";
        } catch (java.util.NoSuchElementException e) {
            result = "Error: data structure is empty";
        } catch (FileNotFoundException e) {
            result = "Error: File not found";
        } catch (IOException e) {
            result = "Error: IO error " + e.getMessage();
        } catch (ClassNotFoundException e) {
            result = "Error: Unknown format";
        }

        return result;
    }

    public String addTmp(Seznam<String> struktura, String key) {
        seznami.put(key, struktura);
        return "OK";
    }
}
