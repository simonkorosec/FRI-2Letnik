import java.util.Scanner;

public class Naloga1 {
    private static boolean pogoj;
    private static Sequence1 skladovi;
    private static Stack glavniSklad;

    public static void main(String[] args) {
        if (true || args.length != 0 && args[0].equals("calc")) {
            Scanner sc = new Scanner(System.in);

            while (sc.hasNextLine()) {
                pogoj = false;
                skladovi = new Sequence1();
                glavniSklad = skladovi.getSklad(0);
                String ukaz = sc.nextLine();

                String[] ukazi = ukaz.split(" ");
                izvajanje(ukazi);
            }
        }
    }

    private static void izvajanje(String[] ukazi) {
        for (int i = 0; i < ukazi.length; i++) {
            String ukaz = ukazi[i];
            if (ukaz.startsWith("?") && !pogoj)
                continue;
            else if (ukaz.startsWith("?") && pogoj)
                ukaz = ukaz.substring(1);

            switch (ukaz) {
                case "then":
                    int st = Integer.parseInt(glavniSklad.pop());
                    pogoj = st != 0;
                    break;
                case "else":
                    pogoj = !pogoj;
                    break;
                case "run":
                    run(Integer.parseInt(glavniSklad.pop()));
                    break;
                case "loop": {
                    int sklad = Integer.parseInt(glavniSklad.pop());
                    int koliko = Integer.parseInt(glavniSklad.pop());
                    for (int j = 0; j < koliko; j++) {
                        run(sklad);
                    }
                    break;
                }
                case "fun": {
                    int kam = Integer.parseInt(glavniSklad.pop());
                    int koliko = Integer.parseInt(glavniSklad.pop());
                    Stack sklad = skladovi.getSklad(kam);
                    for (int k = 0; k < koliko; k++) {
                        sklad.push(ukazi[++i]);
                    }
                    break;
                }
                case "move": {
                    int kam = Integer.parseInt(glavniSklad.pop());
                    int koliko = Integer.parseInt(glavniSklad.pop());
                    Stack sklad = skladovi.getSklad(kam);
                    for (int j = 0; j < koliko; j++) {
                        sklad.push(glavniSklad.pop());
                    }
                    break;
                }
                case "echo":
                    glavniSklad.echo();
                    break;
                case "pop":
                    glavniSklad.pop();
                    break;
                case "dup":
                    glavniSklad.dup();
                    break;
                case "dup2":
                    glavniSklad.dup2();
                    break;
                case "swap":
                    glavniSklad.swap();
                    break;
                case "char":
                    glavniSklad.znak();
                    break;
                case "even":
                    glavniSklad.even();
                    break;
                case "odd":
                    glavniSklad.odd();
                    break;
                case "!":
                    glavniSklad.faktor();
                    break;
                case "len":
                    glavniSklad.len();
                    break;
                case "<>":
                    glavniSklad.primerjajRazlicna();
                    break;
                case "<":
                    glavniSklad.primerjajManjsi();
                    break;
                case "<=":
                    glavniSklad.primerjajManjsiEnak();
                    break;
                case "==":
                    glavniSklad.primerjajEnaka();
                    break;
                case ">":
                    glavniSklad.primerjajVecji();
                    break;
                case ">=":
                    glavniSklad.primerjajVecjiEnak();
                    break;
                case "+":
                    glavniSklad.vsota();
                    break;
                case "-":
                    glavniSklad.razlika();
                    break;
                case "*":
                    glavniSklad.krat();
                    break;
                case "/":
                    glavniSklad.deljeno();
                    break;
                case "%":
                    glavniSklad.ostanek();
                    break;
                case ".":
                    glavniSklad.stakni();
                    break;
                case "rnd":
                    glavniSklad.random();
                    break;
                case "print":
                    Stack sklad = skladovi.getSklad(Integer.parseInt(glavniSklad.pop()));
                    sklad.print();
                    break;
                case "clear":
                    Stack sklad1 = skladovi.getSklad(Integer.parseInt(glavniSklad.pop()));
                    sklad1.clear();
                    break;
                case "reverse":
                    Stack sklad2 = skladovi.getSklad(Integer.parseInt(glavniSklad.pop()));
                    sklad2.reverse();
                    break;
                case "preorder":
                    System.out.println(preorder(Integer.parseInt(glavniSklad.pop()),0));
                    break;
                case "postorder":
                    System.out.println(postorder(Integer.parseInt(glavniSklad.pop()),0).trim());
                    break;
                default:
                    glavniSklad.push(ukaz);
                    break;
            }

        }
    }

    private static void run(int stSklada) {
        String [] ukazi = skladovi.getSklad(stSklada).toString().split(" ");
        if (!ukazi[0].equals(""))
            izvajanje(ukazi);
    }

    private static String preorder(int stopnja, int i) {
        String[] sklad = glavniSklad.getSklad();
        int stEl = glavniSklad.getStElementov();
        StringBuilder sb = new StringBuilder(sklad[i]);
        for (int j = 1; j <= stopnja; j++) {
            int nov = (stopnja * i) + j;
            if (nov < stEl)
                sb.append(" ").append(preorder(stopnja, nov));
        }

        return sb.toString();
    }

    private static String postorder(int stopnja, int i) {
        String[] sklad = glavniSklad.getSklad();
        int stEl = glavniSklad.getStElementov();
        StringBuilder sb = new StringBuilder();
        for (int j = 1; j <= stopnja; j++) {
            int nov = (stopnja * i) + j;
            if (nov < stEl)
                sb.append(postorder(stopnja, nov));
        }

        sb.append(" ").append(sklad[i]);
        return sb.toString();
    }
}

class Stack {

    private int steviloElementov = 0;
    private String[] elementi = new String[64];

    void push(String str) {
        this.elementi[this.steviloElementov++] = str;
    }

    void echo() {
        if (steviloElementov == 0)
            System.out.println();
        else
            System.out.println(this.elementi[this.steviloElementov - 1]);
    }

    String pop() {
        String tmp = this.elementi[this.steviloElementov - 1];
        this.elementi[--this.steviloElementov] = null;
        return tmp;
    }

    void dup() {
        this.push(this.elementi[this.steviloElementov - 1]);
    }

    void dup2() {
        String x = this.elementi[this.steviloElementov - 2];
        String y = this.elementi[this.steviloElementov - 1];
        this.push(x);
        this.push(y);
    }

    void swap() {
        String tmp = this.elementi[this.steviloElementov - 1];
        this.elementi[this.steviloElementov - 1] = this.elementi[this.steviloElementov - 2];
        this.elementi[this.steviloElementov - 2] = tmp;
    }

    void znak() {
        int koda = Integer.parseInt(this.pop());
        this.push(Character.toString((char) koda));
    }

    void even() {
        int vrh = Integer.parseInt(this.pop());
        if (vrh % 2 == 0)
            this.push("1");
        else
            this.push("0");
    }

    void odd() {
        int vrh = Integer.parseInt(this.pop());
        if (vrh % 2 == 0)
            this.push("0");
        else
            this.push("1");
    }

    void faktor() {
        int st = Integer.parseInt(this.pop());
        int fact = 1;
        for (int i = 1; i <= st; i++) {
            fact *= i;
        }
        this.push(Integer.toString(fact));
    }

    void len() {
        this.push(Integer.toString(this.pop().length()));
    }

    void primerjajRazlicna() {
        int y = Integer.parseInt(this.pop());
        int x = Integer.parseInt(this.pop());

        if (x != y)
            this.push("1");
        else
            this.push("0");
    }

    void primerjajManjsi() {
        int y = Integer.parseInt(this.pop());
        int x = Integer.parseInt(this.pop());

        if (x < y)
            this.push("1");
        else
            this.push("0");
    }

    void primerjajManjsiEnak() {
        int y = Integer.parseInt(this.pop());
        int x = Integer.parseInt(this.pop());

        if (x <= y)
            this.push("1");
        else
            this.push("0");
    }

    void primerjajEnaka() {
        int y = Integer.parseInt(this.pop());
        int x = Integer.parseInt(this.pop());

        if (x == y)
            this.push("1");
        else
            this.push("0");
    }

    void primerjajVecji() {
        int y = Integer.parseInt(this.pop());
        int x = Integer.parseInt(this.pop());

        if (x > y)
            this.push("1");
        else
            this.push("0");
    }

    void primerjajVecjiEnak() {
        int y = Integer.parseInt(this.pop());
        int x = Integer.parseInt(this.pop());

        if (x >= y)
            this.push("1");
        else
            this.push("0");
    }

    void vsota() {
        int y = Integer.parseInt(this.pop());
        int x = Integer.parseInt(this.pop());

        this.push(Integer.toString(x + y));
    }

    void razlika() {
        int y = Integer.parseInt(this.pop());
        int x = Integer.parseInt(this.pop());

        this.push(Integer.toString(x - y));

    }

    void krat() {
        int y = Integer.parseInt(this.pop());
        int x = Integer.parseInt(this.pop());

        this.push(Integer.toString(x * y));
    }

    void deljeno() {
        int y = Integer.parseInt(this.pop());
        int x = Integer.parseInt(this.pop());

        this.push(Integer.toString(x / y));
    }

    void ostanek() {
        int y = Integer.parseInt(this.pop());
        int x = Integer.parseInt(this.pop());
        int neg = 1;
        if (x < 0)
            neg = -1;

        int ostan = (Math.abs(x) % Math.abs(y)) * neg ;

        this.push(Integer.toString(ostan));

    }

    void stakni() {
        String y = this.pop();
        String x = this.pop();

        this.push(x + y);

    }

    void random() {
        int x = Integer.parseInt(this.pop());
        int y = Integer.parseInt(this.pop());

        int rnd = x + (int) (Math.random() * ((y - x) + 1));
        this.push(Integer.toString(rnd));

    }

    void print() {
        System.out.println(this.toString());
    }

    void clear() {
        this.steviloElementov = 0;
        this.elementi = new String[64];
    }

    void reverse() {
        for (int i = 0; i < this.steviloElementov / 2; i++) {
            String tmp = this.elementi[this.steviloElementov - i -1];
            this.elementi[this.steviloElementov - i -1] = this.elementi[i];
            this.elementi[i] = tmp;
        }
    }

    public String toString() {
        StringBuilder besedilo = new StringBuilder();

        for (int i = 0; i < this.steviloElementov; i++) {
            besedilo.append(this.elementi[i]).append(" ");
        }
        return besedilo.toString().trim();
    }

    public String[] getSklad() {
        return this.elementi;
    }

    public int getStElementov() {
        return this.steviloElementov;
    }
}

class Sequence1 {
    private final Stack[] skladi = new Stack[42];

    Sequence1() {
        for (int i = 0; i < 42; i++) {
            skladi[i] = new Stack();
        }
    }

    Stack getSklad(int i) {
        return skladi[i];
    }
}