package projekt;

import java.io.Serializable;
import java.util.Comparator;

class Oseba implements Comparable, Serializable {
    private String EMSO;
    private String ime;
    private String priimek;
    private int starost;

    public Oseba() {
        EMSO = null;
        ime = null;
        priimek = null;
    }

    public Oseba(String EMSO, String ime, String priimek, int starost) {
        this.EMSO = EMSO;
        this.ime = ime;
        this.priimek = priimek;
        this.starost = starost;
    }

    public String getEMSO() {
        return EMSO;
    }

    public void setEMSO(String EMSO) {
        this.EMSO = EMSO;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPriimek() {
        return priimek;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }


    @Override
    public String toString() {
        return String.format("%s | %s, %s | %d - %s", EMSO, priimek, ime, starost, (starost >= 18) ? "lahko voli" : "ne more voliti");
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof String) {
            return compareToEMSO((String) o);
        } else if (o instanceof String[]) {
            return compareToImePriimek((String[]) o);
        } else {
            return EMSO.compareTo(((Oseba) o).EMSO);
        }
    }

    private int compareToImePriimek(String[] s) {
        String ime = s[0];
        String priimek = s[1];

        int c = this.priimek.compareTo(priimek);
        if (c == 0) {
            return this.ime.compareTo(ime);
        } else {
            return c;
        }
    }

    private int compareToEMSO(String EMSO) {
        return this.EMSO.compareTo(EMSO);
    }

}

class OsebaComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        int c = ((Oseba) o1).getPriimek().compareTo(((Oseba) o2).getPriimek());
        if (c == 0) {
            return ((Oseba) o1).getIme().compareTo(((Oseba) o2).getIme());
        } else {
            return c;
        }
    }
}