package izzivi.Vaja3;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Izziv5 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.printf("Vnesite velikost polja: ");
        int n = sc.nextInt();
        Oseba [] osebe = new Oseba[n];
        for (int i = 0; i < n; i++) {
            osebe[i] = new Oseba();
        }

        while (true){
            Oseba [] tmp = osebe;
            System.out.println(Arrays.toString(tmp));
            System.out.printf("Vnesite atribut: ");
            Oseba.setAtr(sc.nextInt());
            System.out.printf("Vnesite smer urejanja: ");
            Oseba.setSmer(sc.nextInt());
            bubbleSort(tmp);
            System.out.printf("Želite ponoviti? Y/N");
            if (sc.next().equals("N")){
                break;
            }
        }
    }

    private static void bubbleSort(Oseba[] array) {
        int locilo;
            izpis(array, -1);
        int zadnjiSwap = 1;
        for (int i = 1; i < array.length; i++) {
            boolean u = true;
            int trenutni = 0;
            for (int j = array.length - 1; j >= zadnjiSwap; j--) {
                if (array[j - 1].compareTo(array[j]) > 0) {
                    Oseba tmp = array[j-1];
                    array[j-1] = array[j];
                    array[j] = tmp;

                    trenutni = j;
                    u = false;
                }
            }
            if (u) continue;
            zadnjiSwap = trenutni;
            locilo = zadnjiSwap;
            izpis(array, locilo);
        }

    }

    private static void izpis(Oseba[] array, int locilo) {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < array.length; k++) {
            sb.append(array[k]).append(" ");
            if (locilo == k + 1)
                sb.append("| ");
        }
        System.out.println(sb.toString().trim());
    }

}


class Oseba implements Comparable {

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
