public class Izziv5 {
    public static void main(String[] args) {
        Oseba o = new Oseba();
        System.out.println(o.ime);
    }
}


class Oseba {

    String ime, primerk;
    int letoR;
    static int smer = 1;
    static int atr = 0;

    static final String [] imena = new String[]{"Adelina", "Adrijan", "Aleš", "Eva", "Stanislav", "Marija"};
    static final String [] priimki = new String[]{"Rožič", "Repe", "Andrejka", "Perne", "Berger"};

    public Oseba() {
        int i = (int) (Math.random() * imena.length);
        this.ime = imena[i];
    }
}
