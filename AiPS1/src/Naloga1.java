public class Naloga1 {
    boolean pogoj = false;


}

interface StackInterface{
    //int StElementov = 0;

    void dodaj(String str);

    void echo();
    void pop();
    void dup();
    void dup2();
    void swap();

    void znak();
    void even();
    void odd();
    void faktor();
    void len();

    void primerjajRazlicna();
    void primerjajManjsi();
    void primerjajManjsiEnak();
    void primerjajEnaka();
    void primerjajVecji();
    void primerjajVecjiEnak();

    void vsota();
    void razlika();
    void krat();
    void deljeno();
    void ostanek();
    void stakni();
    void random();

    void then();
    void elseT();

    void print();
    void clear();
    void run();
    void move();
    void reverse();


}

class Stack implements StackInterface {

    int steviloElementov = -1;
    String [] elementi = new String[64];

    @Override
    public void dodaj(String str) {
        this.elementi[this.steviloElementov++] = str;
    }

    @Override
    public void echo() {
        if (steviloElementov == -1)
            System.out.println();
        else
            System.out.println(this.elementi[this.steviloElementov]);
    }

    @Override
    public void pop() {
        this.elementi[this.steviloElementov--] = null;
    }

    @Override
    public void dup() {
        this.elementi[++this.steviloElementov] = this.elementi[this.steviloElementov - 1];
    }

    @Override
    public void dup2() {
        String x = this.elementi[this.steviloElementov - 1];
        String y = this.elementi[this.steviloElementov];
        this.elementi[++this.steviloElementov] = x;
        this.elementi[++this.steviloElementov] = y;
    }

    @Override
    public void swap() {
        String tmp = this.elementi[this.steviloElementov];
        this.elementi[this.steviloElementov] = this.elementi[this.steviloElementov - 1];
        this.elementi[this.steviloElementov - 1] = tmp;
    }

    @Override
    public void znak() {
        int koda = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.elementi[this.steviloElementov] = Character.toString((char) koda);
    }

    @Override
    public void even() {
        int vrh = Integer.parseInt(this.elementi[this.steviloElementov]);
        if (vrh % 2 == 0)
            this.elementi[this.steviloElementov] = "1";
        else
            this.elementi[this.steviloElementov] = "0";
    }

    @Override
    public void odd() {
        int vrh = Integer.parseInt(this.elementi[this.steviloElementov]);
        if (vrh % 2 == 0)
            this.elementi[this.steviloElementov] = "0";
        else
            this.elementi[this.steviloElementov] = "1";
    }

    @Override
    public void faktor() {
        int st = Integer.parseInt(this.elementi[this.steviloElementov]);
        int fact = 1;
        for (int i = 1; i <= st; i++) {
            fact*=i;
        }
        this.elementi[this.steviloElementov] = Integer.toString(fact);
    }

    @Override
    public void len() {
        int dolzina = this.elementi[this.steviloElementov].length();
        this.elementi[this.steviloElementov] = Integer.toString(dolzina);
    }

    @Override
    public void primerjajRazlicna() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        if (x != y)
            this.elementi[this.steviloElementov] = "1";
        else
            this.elementi[this.steviloElementov] = "0";
    }

    @Override
    public void primerjajManjsi() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        if (x < y)
            this.elementi[this.steviloElementov] = "1";
        else
            this.elementi[this.steviloElementov] = "0";
    }

    @Override
    public void primerjajManjsiEnak() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        if (x <= y)
            this.elementi[this.steviloElementov] = "1";
        else
            this.elementi[this.steviloElementov] = "0";
    }

    @Override
    public void primerjajEnaka() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        if (x == y)
            this.elementi[this.steviloElementov] = "1";
        else
            this.elementi[this.steviloElementov] = "0";
    }

    @Override
    public void primerjajVecji() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        if (x > y)
            this.elementi[this.steviloElementov] = "1";
        else
            this.elementi[this.steviloElementov] = "0";
    }

    @Override
    public void primerjajVecjiEnak() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        if (x >= y)
            this.elementi[this.steviloElementov] = "1";
        else
            this.elementi[this.steviloElementov] = "0";
    }

    @Override
    public void vsota() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        this.elementi[this.steviloElementov] = Integer.toString(x+y);

    }

    @Override
    public void razlika() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        this.elementi[this.steviloElementov] = Integer.toString(x-y);

    }

    @Override
    public void krat() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        this.elementi[this.steviloElementov] = Integer.toString(x*y);

    }

    @Override
    public void deljeno() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        this.elementi[this.steviloElementov] = Integer.toString(x/y);

    }

    @Override
    public void ostanek() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);
        this.pop();

        this.elementi[this.steviloElementov] = Integer.toString(x%y);

    }

    @Override
    public void stakni() {
        String x = this.elementi[this.steviloElementov - 1];
        String y = this.elementi[this.steviloElementov];
        this.pop();

        this.elementi[this.steviloElementov] = x + y;

    }

    @Override
    public void random() {
        int x = Integer.parseInt(this.elementi[this.steviloElementov - 1]);
        int y = Integer.parseInt(this.elementi[this.steviloElementov]);

        int rnd = x + (int)(Math.random() * (y - x) + 1);
        this.dodaj(Integer.toString(rnd));

    }

    @Override
    public void then() {

    }

    @Override
    public void elseT() {

    }

    @Override
    public void print() {
        for (int i = 0; i <= this.steviloElementov; i++) {
            System.out.printf("%s ", this.elementi[this.steviloElementov]);
        }
        System.out.printf("\n");
    }

    @Override
    public void clear() {
        this.steviloElementov = -1;
        this.elementi = new String[64];
    }

    @Override
    public void run() {

    }

    @Override
    public void move() {

    }

    @Override
    public void reverse() {
        for (int i = 0; i <= this.steviloElementov/2; i++) {
            String tmp = this.elementi[this.steviloElementov];
            this.elementi[this.steviloElementov] = this.elementi[i];
            this.elementi[i] = tmp;
        }
    }
}

class  Sequence {
    Stack [] skladi = new Stack[42];

    Sequence() {
        for (int i = 0; i < 42; i++) {
            skladi[i] = new Stack();
        }
    }
}