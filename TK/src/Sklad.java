class Element<Tip> {
    public Tip vrednost;
    public Element<Tip> vezava;

    public Element(Tip e) {
        vrednost = e;
    }
}

public class Sklad<Tip> {
    private Element<Tip> vrh;

    public Sklad() {
    }

    public void push(Tip e) {
        Element<Tip> novVrh = new Element<>(e);
        novVrh.vezava = vrh;
        vrh = novVrh;
    }

    public Tip pop() {
        if (null == vrh) {
            throw new java.util.NoSuchElementException();
        }

        Tip e = vrh.vrednost;
        vrh = vrh.vezava;
        return e;
    }

    public boolean isEmpty() {
        return (null == vrh);
    }

    public Tip peek() {
        return vrh.vrednost;
    }

    public int count() {
        Sklad<Tip> pomozni = new Sklad<>();
        int st = 0;

        while (!this.isEmpty()) {
            pomozni.push(this.pop());
            st++;
        }
        while (!pomozni.isEmpty()) {
            this.push(pomozni.pop());
        }

        return st;
    }

    public int search(Tip iskani) {
        Sklad<Tip> pomozni = new Sklad<>();
        int st = 0;
        boolean found = false;

        while (!this.isEmpty()) {
            Tip e = this.pop();
            pomozni.push(e);
            if (e.equals(iskani)) {
                found = true;
                break;
            }
            st++;
        }

        while (!pomozni.isEmpty()) {
            this.push(pomozni.pop());
        }

        return (found) ? st : -1;
    }
}