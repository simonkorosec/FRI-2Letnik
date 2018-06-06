package imenik;

import java.util.HashMap;
import java.io.*;


class SeznamiUV {

    private final HashMap<String, Seznam<Prijatelj>> seznamiPoImenu;
    private HashMap<String, Seznam<Prijatelj>> seznamiPoTelSt;
    private Seznam<Prijatelj> seznamPoImenu;
    private Seznam<Prijatelj> seznamPoTelSt;
    
    private static String memoryError = "Error: not enough memory, operation failed";
   
    public SeznamiUV() {
        seznamiPoImenu = new HashMap<>();
        seznamiPoTelSt = new HashMap<>();
    }
    
    public void addImpl(String key, Seznam<Prijatelj> seznamPoImenu, Seznam<Prijatelj> seznamPoTelSt) {
        seznamiPoImenu.put(key, seznamPoImenu);
        seznamiPoTelSt.put(key, seznamPoTelSt);
    }

    public String processInput(String input) {
        String token;
        String result = "OK";
        String[] params = input.split(" ");


        //moramo preverjati za primer praznega stringa
        if (params.length == 0) {
            return "Error: enter command";
        }
        else{
            token = params[0];
        }

        if (!token.equals("use") && (null == seznamPoImenu)) {
            return "Error: please specify a data structure (use {pv|sk|bst})";
        }
        try {
            switch (token) {
                case "use":
                    if (params.length > 1) {
                        String structType = params[1];
                        seznamPoImenu = seznamiPoImenu.get(structType);
                        seznamPoTelSt = seznamiPoTelSt.get(structType);
                        if (null == seznamPoImenu) {
                            result = "Error: please specify a correct data structure type {pv|sk|bst}";
                        }

                    } else {
                        result = "Error: please specify a data structure type {pv|sk|bst}";
                    }
                    break;
                case "add":
                    if (params.length == 4) {
                        seznamPoImenu.add(new Prijatelj(params[1], params[2], params[3]));
                        seznamPoTelSt.add(new Prijatelj(params[1], params[2], params[3]));
                    } else
                        result = "Error: please specify three strings";
                    break;
                case "removefirst":
                    Prijatelj prijatelj = seznamPoImenu.removeFirst();
                    seznamPoTelSt.remove(prijatelj);
                    result = prijatelj.toString();
                    break;
                case "remove":
                    if (params.length == 3) {
                        prijatelj = seznamPoImenu.remove(new Prijatelj(params[1], params[2], ""));
                        seznamPoTelSt.remove(prijatelj);
                    } else
                        result = "Error: please specify two strings";
                    break;
                case "getfirst":
                    prijatelj = seznamPoImenu.getFirst();
                    result = prijatelj.toString();
                    break;
                case "count":
                    result = seznamPoImenu.size() + "";
                    break;
                case "depth":
                    result = seznamPoImenu.depth() + "";
                    break;
                case "reset":
                    while (!seznamPoImenu.isEmpty()) {
                        seznamPoImenu.removeFirst();
                    }
                    while (!seznamPoTelSt.isEmpty()) {
                        seznamPoTelSt.removeFirst();
                    }
                    break;
                case "exists":
                    result = "No";
                    if (params.length == 3) {
                        if (seznamPoImenu.exists(new Prijatelj(params[1], params[2], "")))
                            result = "Yes";
                    } else if (params.length == 2) {
                        if (seznamPoTelSt.exists(new Prijatelj("", "", params[1])))
                            result = "Yes";
                    } else {
                        result = "Error: please specify two strings";
                    }
                    break;
                case "print":
                    seznamPoImenu.print();
                    result = "OK";
                    break;
                case "save":
                    if (params.length == 2) {
                        String file = params[1];
                        seznamPoImenu.save(new FileOutputStream("i_"+file));
                        seznamPoTelSt.save(new FileOutputStream("t_"+file));
                    } else {
                        result = "Error: please specify a file name";
                    }
                    break;
                case "restore":
                    if (params.length == 2) {
                        String file = params[1];
                        seznamPoImenu.restore(new FileInputStream("i_"+file));
                        seznamPoTelSt.restore(new FileInputStream("t_"+file));
                    } else {
                        result = "Error: please specify a file name";
                    }
                    break;
                default:
                    result = "Error: invalid command";
                    break;
            }
                

        } catch (UnsupportedOperationException e) {
            result = "Error: Operation not supported";
        } catch (IllegalArgumentException e) {
            result = "Error: Duplicated entry";
        } catch (java.util.NoSuchElementException e) {
            result = "Error: structure is empty";
        } catch (IOException e) {
            result = "Error: IO error " + e.getMessage();
        } catch (ClassNotFoundException e) {
            result = "Error: Unknown format";
        } catch (OutOfMemoryError e) {
            return memoryError;
        }

        return result;
    }
}
