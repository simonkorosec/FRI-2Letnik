package projekt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

class SeznamiUV {

    private Seznam<Oseba> seznamEMSO;
    private Seznam<Oseba> seznamIME;

    public SeznamiUV() {
        seznamEMSO = new BinomskaKopica<>(new ComperatorEMSO());
        seznamIME = new BinomskaKopica<>(new ComperatorImePriimek());
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
                    seznamEMSO.add(o);
                    seznamIME.add(o);
                    break;
                case "remove":
                    arg = sc.nextLine().split(",");
                    if (arg.length == 2) {
                        ime = arg[0].trim();
                        priimek = arg[1].trim();
                        o = new Oseba();
                        o.setIme(ime);
                        o.setPriimek(priimek);

                        o = seznamIME.remove(o);
                        seznamEMSO.remove(o);
                    } else {
                        EMSO = arg[0].trim();
                        o = new Oseba();
                        o.setEMSO(EMSO);
                        o = seznamEMSO.remove(o);
                        seznamIME.remove(o);
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
                        result = seznamIME.search(o).toString();
                    } else {
                        EMSO = arg[0].trim();
                        o = new Oseba();
                        o.setEMSO(EMSO);
                        result = seznamEMSO.search(o).toString();
                    }
                    break;
                case "count":
                    result = "No. of persons: " + seznamEMSO.size() + "";
                    break;
                case "reset":
                    while (!seznamEMSO.isEmpty()) {
                        seznamEMSO.removeFirst();
                    }
                    while (!seznamIME.isEmpty()) {
                        seznamIME.removeFirst();
                    }
                    break;
                case "print":
                    System.out.println("No. of persons: " + seznamIME.size() + "");
                    seznamIME.print();
                    result = "";
                    break;
                case "save":
                    String filename = sc.next();
                    seznamEMSO.save(new FileOutputStream("E_"+filename));
                    seznamIME.save(new FileOutputStream("I_"+filename));
                    break;
                case "restore":
                    filename = sc.next();
                    seznamEMSO.restore(new FileInputStream("E_"+filename));
                    seznamIME.restore(new FileInputStream("I_"+filename));
                    break;
                default:
                    result = "Invalid command";
            }
        } catch (java.util.NoSuchElementException e) {
            result = "Person does not exist";
        } catch (IOException e) {
            result = "I/O Error: " + e.getMessage();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            result = "Person already exists";
        }
        return result;
    }
}
