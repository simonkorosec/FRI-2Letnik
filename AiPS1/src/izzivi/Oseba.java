package izzivi;

import java.util.concurrent.ThreadLocalRandom;

public class Oseba implements Comparable {

    private static final String[] imena = new String[]{"Adelina","Adian","Adna","Adin","Adriana","Adnan","Aiša","Adrijan","Ajna","Ajdin","Ajša","Alan","Aleksandra","Aldin","Alenka","Aleksa","Alisa","Aleksandar","Amadeja","Alem","Amalija","Aleš","Amelie","Aleš","Amna","Alexander","Amra","Ali","Ana","Aljoša","Ana Marija","Amin","Anabela","Andrej","Anaja","Anel","Anamarija","Anes","Anastasia","Anis","Anastazija","Anton","Angelina","Arian","Anita","Armin","Anja","Aron","Anna","Bojan","Ariana","Borut","Aurora","Boštjan","Barbara","Damjan","Barbara","Dan","Blažka","Daniel","Danaja","Danijel","Deja","Dario","Dijana","Daris","Dora","Davor","Doroteja","Dejan","Dunja","Dino","Eli","Drejc","Elisa","Edi","Eliza","Eldin","Elizabeta","Eman","Emili","Emil","Emilija","Enis","Eneja","Ervin","Enja","Florjan"};
    private static final String[] priimki = new String[]{"Novak","Horvat","Kovačič","Krajnc","Zupančič","Potočnik","Kovač","Mlakar","Kos","Vidmar","Golob","Turk","Kralj","Božič","Korošec","Bizjak","Zupan","Hribar","Kotnik","Kavčič","Rozman","Kastelic","Oblak","Petek","Žagar","Hočevar","Kolar","Košir","Koren","Klemenčič","Zajc","Knez","Medved","Zupanc","Petrič","Pirc","Hrovat","Pavlič","Kuhar","Lah","Uršič","Tomažič","Zorko","Sever","Erjavec","Babič","Jereb","Jerman","Majcen","Pušnik","Kranjc","Breznik","Rupnik","Lesjak","Perko","Dolenc","Pečnik","Močnik","Furlan","Pavlin","Vidic","Logar","Kovačević","Jenko","Ribič","Tomšič","Žnidaršič"};
    static private int smer = 1;
    static private int atr = 0;
    private String ime, priimek;
    private int letoR;

    Oseba() {
        int i = (int) (Math.random() * imena.length);
        this.ime = imena[i];
        i = (int) (Math.random() * priimki.length);
        this.priimek = priimki[i];
        this.letoR = ThreadLocalRandom.current().nextInt(1970, 2018);
    }

    static void setSmer(int smer) {
        Oseba.smer = smer;
    }

    static void setAtr(int atr) {
        Oseba.atr = atr;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Oseba) {
            switch (atr) {
                case 0:
                    return this.ime.compareTo(((Oseba) o).getIme()) * smer;
                case 1:
                    return this.priimek.compareTo(((Oseba) o).getPriimek()) * smer;
                case 2:
                    return (this.letoR - ((Oseba) o).getLetoR()) * smer;
            }
        }
        return 0;
    }

    private String getIme() {
        return ime;
    }

    private String getPriimek() {
        return priimek;
    }

    private int getLetoR() {
        return letoR;
    }

    @Override
    public String toString() {
        switch (atr) {
            case 0:
                return this.ime;
            case 1:
                return this.priimek;
            case 2:
                return Integer.toString(this.letoR);
        }
        return null;
    }

}
