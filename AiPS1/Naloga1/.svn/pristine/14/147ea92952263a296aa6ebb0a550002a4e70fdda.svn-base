import java.util.Scanner;

public class Naloga1 {
    private static boolean pogoj = false;
    private static final Sequence skladovi = new Sequence();
    private static final Stack glavniSklad = skladovi.getSklad(0);

    public static void main(String[] args) {
		if (args.length != 0 && args[0].equals("calc")) {
			Scanner sc = new Scanner(System.in);

			String[] ukazi = sc.nextLine().split(" ");
			izvajanje(ukazi);
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
                default:
                    glavniSklad.push(ukaz);
                    break;
            }

        }
    }

    private static void run(int stSklada) {
        String [] ukazi = skladovi.getSklad(stSklada).toString().split(" ");
        izvajanje(ukazi);
    }

}

class Stack {

    private int steviloElementov = -1;
    private String[] elementi = new String[64];

    
    void push(String str) {
        this.elementi[++this.steviloElementov] = str;
    }

    
    void echo() {
        if (steviloElementov == -1)
            System.out.println();
        else
            System.out.println(this.elementi[this.steviloElementov]);
    }

    
    String pop() {
        String tmp = this.elementi[this.steviloElementov];
        this.elementi[this.steviloElementov--] = null;
        return tmp;
    }

    
    void dup() {
        this.push(this.elementi[this.steviloElementov]);
    }

    
    void dup2() {
        String x = this.elementi[this.steviloElementov - 1];
        String y = this.elementi[this.steviloElementov];
        this.push(x);
        this.push(y);
    }

    
    void swap() {
        String tmp = this.elementi[this.steviloElementov];
        this.elementi[this.steviloElementov] = this.elementi[this.steviloElementov - 1];
        this.elementi[this.steviloElementov - 1] = tmp;
    }

    
    void znak() {
        int koda = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.elementi[this.steviloElementov] = Character.toString((char) koda);
    }

    
    void even() {
        int vrh = Integer.parseInt(this.elementi[this.steviloElementov]);
        if (vrh % 2 == 0)
            this.elementi[this.steviloElementov] = "1";
        else
            this.elementi[this.steviloElementov] = "0";
    }

    
    void odd() {
        int vrh = Integer.parseInt(this.elementi[this.steviloElementov]);
        if (vrh % 2 == 0)
            this.elementi[this.steviloElementov] = "0";
        else
            this.elementi[this.steviloElementov] = "1";
    }

    
    void faktor() {
        int st = Integer.parseInt(this.elementi[this.steviloElementov]);
        int fact = 1;
        for (int i = 1; i <= st; i++) {
            fact *= i;
        }
        this.elementi[this.steviloElementov] = Integer.toString(fact);
    }

    
    void len() {
        int dolzina = this.elementi[this.steviloElementov].length();
        this.elementi[this.steviloElementov] = Integer.toString(dolzina);
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
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        this.elementi[this.steviloElementov] = Integer.toString(x + y);

    }

    
    void razlika() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        this.elementi[this.steviloElementov] = Integer.toString(x - y);

    }

    
    void krat() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        this.elementi[this.steviloElementov] = Integer.toString(x * y);

    }

    
    void deljeno() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        this.elementi[this.steviloElementov] = Integer.toString(x / y);

    }

    
    void ostanek() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        this.elementi[this.steviloElementov] = Integer.toString(x % y);

    }

    
    void stakni() {
        String x = this.elementi[this.steviloElementov - 1];
        String y = this.elementi[this.steviloElementov];
        this.pop();

        this.elementi[this.steviloElementov] = x + y;

    }

    
    void random() {
        int x = Integer.parseInt(this.pop());
        int y = Integer.parseInt(this.pop());

        int rnd = x + (int) (Math.random() * (y - x) + 1);
        this.push(Integer.toString(rnd));

    }

    
    void print() {
        System.out.println(this.toString());
    }

    
    void clear() {
        this.steviloElementov = -1;
        this.elementi = new String[64];
    }

    
    void reverse() {
        for (int i = 0; i <= this.steviloElementov / 2; i++) {
            String tmp = this.elementi[this.steviloElementov - i];
            this.elementi[this.steviloElementov - i] = this.elementi[i];
            this.elementi[i] = tmp;
        }
    }

    
    public String toString() {
        StringBuilder besedilo = new StringBuilder();

        for (int i = 0; i <= this.steviloElementov; i++) {
            besedilo.append(this.elementi[i]).append(" ");
        }
        return besedilo.toString().trim();
    }
}

class Sequence {
    private final Stack[] skladi = new Stack[42];

    Sequence() {
        for (int i = 0; i < 42; i++) {
            skladi[i] = new Stack();
        }
    }

    Stack getSklad(int i) {
        return skladi[i];
    }
}