package seznami;

import java.io.*;
import java.util.List;

public class BstMock<Tip extends Comparable> implements Seznam<Tip> {

    @Override
    public void add(Tip e) {
        throw new UnsupportedOperationException("Not yet implemented");
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
        throw new IOException("There is not enough space on the disk");
    }

    @Override
    public void restore(InputStream inputStream) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
