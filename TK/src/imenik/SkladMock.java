package imenik;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SkladMock <Tip> implements Seznam<Tip> {

    @Override
    public void add(Tip e) {
        throw new java.lang.OutOfMemoryError();
    }

    @Override
    public Tip removeFirst() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tip remove(Tip e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tip getFirst() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean exists(Tip e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int depth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void print() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void save(OutputStream outputStream) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void restore(InputStream inputStream) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
