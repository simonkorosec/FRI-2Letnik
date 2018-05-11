package seznami;

import java.util.ArrayList;
import java.util.List;

class Element<Tip> {

    public final Tip vrednost;
    public Element<Tip> vezava;

    Element(Tip e) {
        this.vrednost = e;
    }
}

public class Sklad<Tip extends Comparable> implements Seznam<Tip> {

    private Element<Tip> vrh;

    Sklad() {
    }

    public void push(Tip e) {
        Element<Tip> novVrh = new Element<>(e);
        novVrh.vezava = vrh;
        vrh = novVrh;
    }

    public Tip pop() {
        if (vrh == null) {
            throw new java.util.NoSuchElementException();
        }
        Tip e = vrh.vrednost;
        vrh = vrh.vezava;
        return e;
    }

    @Override
    public boolean isEmpty() {
        return (vrh == null);
    }

    public Tip peek() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        return vrh.vrednost;
    }

    public int count() {
        if (isEmpty()) {
            return 0;
        }

        int stElementov = 0;
        Element<Tip> tmp = vrh;
        while (tmp != null) {
            stElementov++;
            tmp = tmp.vezava;
        }
        return stElementov;
    }

    public boolean top(Tip e) {
        if (vrh == null) {
            throw new java.util.NoSuchElementException();
        }
        return vrh.vrednost.equals(e);
    }

    public int search(Tip e) {
        Element<Tip> tmp = vrh;
        int i = 0;
        while (null != tmp) {
            if (tmp.vrednost.equals(e)) {
                return i;
            }
            i++;
            tmp = tmp.vezava;
        }
        return -1;
    }

    @Override
    public void add(Tip e) {
        this.push(e);
    }

    @Override
    public Tip removeFirst() {
        return this.pop();
    }

    @Override
    public Tip getFirst() {
        return peek();
    }

    @Override
    public int size() {
        return this.count();
    }

    @Override
    public int depth() {
        return this.count();
    }

    @Override
    public Tip remove(Tip e) {
        Sklad<Tip> tmp = new Sklad<>();
        Tip rez = null;
        while (!this.isEmpty()) {
            Tip p = this.pop();
            if (p.equals(e)) {
                rez = p;
                break;
            }
            tmp.push(p);
        }

        while (!tmp.isEmpty()) {
            this.push(tmp.pop());
        }

        return rez;
    }

    @Override
    public boolean exists(Tip e) {
        return (this.search(e) != -1);
    }

    @Override
    public List<Tip> asList() {
        List<Tip> list = new ArrayList<>();
        Sklad<Tip> tmp = new Sklad<>();

        while (!this.isEmpty()) {
            Tip e = this.pop();
            tmp.add(e);
            list.add(e);
        }

        while (!tmp.isEmpty()) {
            this.add(tmp.pop());
        }

        return list;
    }

    @Override
    public void print() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
