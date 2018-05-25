

import java.io.IOException;

public class Valuta {
    
    private String oznaka;
    private long znesek;
    private int centi;
    
    public Valuta(double doubleZnesek, String oznaka) {
        this.oznaka = oznaka;
        setZnesek(doubleZnesek);
       
    }
    
    private void setZnesek(double znesek) {
        this.znesek = new Double(znesek).longValue();
        this.centi = (int) ((znesek * 100.0) % 100);
    }
    
    public Valuta vEvre(MenjalnoRazmerje mr) {
        if (oznaka.equals("EUR")) 
            return this;
        else {
            double input = znesek + centi / 100.0;
            double razmerje;
            try {
                razmerje = mr.getRazmerje(oznaka, "EUR");
                // razmerje = mr.getRazmerje(oznaka, "CUD");
                double output = input * razmerje;
                return new Valuta(output, "EUR");
            } catch (IOException ex) {
                return null;
            }
        }
    }

    public boolean equals(Object o) {
        if (o instanceof Valuta) {
            Valuta v = (Valuta) o;
            return (this.oznaka.equals(v.oznaka)
                    && this.znesek == v.znesek && this.centi == v.centi);
        }
        return false;
    }
    
    public String toString() {
        return znesek + "," + Math.abs(centi) + " " + oznaka;
    }
}
