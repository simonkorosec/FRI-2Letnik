package projekt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

public class BinKopicaMock<Tip> implements Seznam<Tip>, Serializable {

    @Override
    public void add(Tip e) {
        throw new java.lang.OutOfMemoryError();
    }

    @Override
    public Tip removeFirst() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Tip getFirst() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int depth() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Tip search(Tip e) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Tip remove(Tip e) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean exists(Tip e) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public List<Tip> asList() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void print() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void save(OutputStream outputStream) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void restore(InputStream inputStream) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
