package projekt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

class SeznamiUV {

    private Seznam<Oseba> seznam;

    public SeznamiUV() {
        seznam = new BinomskaKopica<>();
    }

    public String processInput(String input) {
        Scanner sc = new Scanner(input);
        String token;
        String result = "OK";
        if (sc.hasNext()) {
            token = sc.next();
        } else {
            return "Invalid command";
        }
        if (token.equalsIgnoreCase("exit")) {
            return "Goodbye";
        }
        try {
            switch (token) {
                case "add":
                    String[] arg = sc.nextLine().split(",");
                    String EMSO = arg[0].trim();
                    String ime = arg[1].trim();
                    String priimek = arg[2].trim();
                    int starost = Integer.parseInt(arg[3].trim());

                    Oseba o = new Oseba(EMSO, ime, priimek, starost);
                    seznam.add(o);
//                    if (seznam.exists(o)) {
//                        result = "Person already exists";
//                    } else {
//
//                        result = "OK";
//                    }
                    break;
                case "remove":
                    arg = sc.nextLine().split(",");
                    if (arg.length == 2) {
                        ime = arg[0].trim();
                        priimek = arg[1].trim();
                        o = new Oseba();
                        o.setIme(ime);
                        o.setPriimek(priimek);

                        seznam.remove(o);
                    } else {
                        EMSO = arg[0].trim();
                        o = new Oseba();
                        o.setEMSO(EMSO);
                        seznam.remove(o);
                    }
                    break;
                case "search":
                    arg = sc.nextLine().split(",");
                    if (arg.length == 2) {
                        ime = arg[0].trim();
                        priimek = arg[1].trim();
                        o = new Oseba();
                        o.setIme(ime);
                        o.setPriimek(priimek);
                        result = seznam.search(o).toString();
                    } else {
                        EMSO = arg[0].trim();
                        o = new Oseba();
                        o.setEMSO(EMSO);
                        result = seznam.search(o).toString();
                    }
                    break;
                case "count":
                    result = "No. of persons: " + seznam.size() + "";
                    break;
                case "reset":
                    while (!seznam.isEmpty()) {
                        seznam.removeFirst();
                    }
                    break;
                case "print":
                    System.out.println("No. of persons: " + seznam.size() + "");
                    seznam.print();
                    result = "";
                    break;
                case "save":
                    seznam.save(new FileOutputStream(sc.next()));
                    break;
                case "restore":
                    seznam.restore(new FileInputStream(sc.next()));
                    break;
                default:
                    result = "Invalid command";
            }
        } catch (java.util.NoSuchElementException e) {
            result = "Person does not exist";
        } catch (IOException e) {
            result = "IO error: " + e.getMessage();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            result = "Person already exists";
        }
        return result;
    }
}
