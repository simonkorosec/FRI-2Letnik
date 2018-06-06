package projekt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Volilci {

    public static void main(String[] args) {
        SeznamiUV seznamiUV = new SeznamiUV();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        String[] cmd;
        String output;

        try {
            do {
                System.out.print("command>");
                cmd = br.readLine().split(" ");
                input = cmd[0];
                if (input.equals("exit")) {
                    System.out.print(">> Goodbye");
                } else {
                    switch (input) {
                        case "add":
                            input += " ";
                            System.out.print("add> EMSO: ");
                            String emso = br.readLine();
                            input += emso + ",";
                            System.out.print("add> IME: ");
                            input += br.readLine() + ",";
                            System.out.print("add> PRIIMEK: ");
                            input += br.readLine() + ",";
                            System.out.print("add> STAROST: ");
                            input += br.readLine();
                            if (checkEMSO(emso)) {
                                System.out.println(">> Invalid input data");
                                continue;
                            }
                            break;
                        case "remove":
                            if (cmd.length == 1) {
                                input += " ";
                                System.out.print("remove> IME: ");
                                input += br.readLine() + ",";
                                System.out.print("remove> PRIIMEK: ");
                                input += br.readLine();
                            } else if (cmd.length == 2) {
                                if (checkEMSO(cmd[1])){
                                    System.out.println(">> Invalid input data");
                                    continue;
                                }
                                input += " " + cmd[1];
                            } else {
                                System.out.println(">> Invalid input data");
                                continue;
                            }
                            break;
                        case "search":
                            if (cmd.length == 1) {
                                input += " ";
                                System.out.print("search> IME: ");
                                input += br.readLine() + ",";
                                System.out.print("search> PRIIMEK: ");
                                input += br.readLine();
                            } else if (cmd.length == 2) {
                                if (checkEMSO(cmd[1])){
                                    System.out.println(">> Invalid input data");
                                    continue;
                                }
                                input += " " + cmd[1];
                            } else {
                                System.out.println(">> Invalid input data");
                                continue;
                            }
                            break;
                        case "save":
                            if (cmd.length == 2) {
                                input += " " + cmd[1];
                            } else {
                                System.out.println(">> Invalid input data");
                                continue;
                            }
                            break;
                        case "restore":
                            if (cmd.length == 2) {
                                input += " " + cmd[1];
                            } else {
                                System.out.println(">> Invalid input data");
                                continue;
                            }
                            break;
                        case "reset":
                            System.out.print("reset> Are you sure(y|n): ");
                            if (!br.readLine().equalsIgnoreCase("y")) {
                                continue;
                            }
                            break;
                        case "count":
                            break;
                        case "print":
                            System.out.print(">> ");
                            break;
                        default:
                            System.out.println(">> Invalid command");
                            continue;
                    }
                    output = seznamiUV.processInput(input);
                    if (!input.equalsIgnoreCase("print")) {
                        System.out.println(">> " + output);
                    }
                }
            } while (!input.equalsIgnoreCase("exit"));
        } catch (IOException e) {
            System.err.println("[Error]: Failed to retrieve the next command.");
            System.exit(1);
        }
    }

    /**
     *
     * @param emso
     * @return true če je emsa neveljavan false drugače
     */
    private static boolean checkEMSO(String emso) {
        return emso.length() != 13 || (!emso.contains("500") && !emso.contains("505"));
    }
}