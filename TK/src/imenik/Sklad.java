package imenik;

import java.io.*;
import java.util.Comparator;


class Element<Tip> implements Serializable{

    Tip vrednost;
    Element<Tip> vezava;

    Element(Tip e) {
        vrednost = e;
    }
}

public class Sklad<Tip> implements Seznam<Tip> {

    private Element<Tip> vrh;
    private Comparator<Tip> comparator;

    public Sklad(Comparator<Tip> comparator) {
        this.comparator = comparator;
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

    private Tip peek() {
        if (null == vrh) {
            throw new java.util.NoSuchElementException();
        }
        return vrh.vrednost;
    }

    public boolean isEmpty() {
        return (null == vrh);
    }

    private int search(Tip e) {
        Element<Tip> temp = vrh;
        int i = 1;
        while (null != temp) {
            if (0 == comparator.compare(temp.vrednost, e)) {
                return i;
            }
            i++;
            temp = temp.vezava;
        }
        return -1;
    }

    private int count() {
        int result = 0;
        Element<Tip> temp = vrh;
        while (null != temp) {
            ++result;
            temp = temp.vezava;
        }
        return result;
    }

    @Override
    public void add(Tip e) {
        push(e);
    }

    @Override
    public Tip removeFirst() {
        return pop();
    }

    @Override
    public Tip remove(Tip e) {
        Sklad<Tip> tmpSklad = new Sklad<>(comparator);
        Tip vrn = null;
        while (!this.isEmpty()) {
            Tip tmp = this.pop();
            if (comparator.compare(e, tmp) == 0) {
                vrn = e;
                break;
            }
            tmpSklad.push(tmp);
        }
        while (!tmpSklad.isEmpty()) {
            this.push(tmpSklad.pop());
        }

        if (vrn == null) {
            throw new java.util.NoSuchElementException();
        } else {
            return vrn;
        }
    }

    @Override
    public Tip getFirst() {
        return peek();
    }

    @Override
    public int size() {
        return count();
    }

    @Override
    public int depth() {
        return count();
    }

    @Override
    public boolean exists(Tip e) {
        return search(e) != -1;
    }

    @Override
    public void print() {
        Sklad<Tip> tmpSklad = new Sklad<>(comparator);
        while (!this.isEmpty()) {
            Tip tmp = this.pop();
            System.out.println(tmp);
            tmpSklad.push(tmp);
        }
        while (!tmpSklad.isEmpty()) {
            this.push(tmpSklad.pop());
        }
    }

    @Override
    public void save(OutputStream outputStream) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(vrh);
    }

    @Override
    public void restore(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(inputStream);
        vrh = (Element<Tip>) in.readObject();
    }
}
