package seznami;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PrioritetnaVrsta<Tip extends Comparable> implements Seznam<Tip> {
    private Object[] heap;
    private int end = 0;
    private int maxSize = 0;


    public PrioritetnaVrsta() {
        this(100);
    }

    public PrioritetnaVrsta(int maxSize) {
        this.heap = new Object[maxSize];
        this.maxSize = maxSize;
    }

    private void resize() {
        Object[] tmp = new Object[maxSize * 2];
        System.arraycopy(heap, 0, tmp, 0, heap.length);
        maxSize = maxSize * 2;
        heap = tmp;
    }

    private void bubbleUp() {
        int eltIdx = end - 1;
        while (eltIdx >= 0) {
            Tip elt = (Tip) heap[eltIdx];
            int childIdx = eltIdx * 2 + 1;
            if (childIdx < end) {
                Tip child = (Tip) heap[childIdx];
                if (childIdx + 1 < end && child.compareTo(heap[childIdx + 1]) < 0)
                    child = (Tip) heap[++childIdx];
                if (elt.compareTo(child) >= 0)
                    return;
                swap(eltIdx, childIdx);
            }
            eltIdx = eltIdx % 2 == 1 ? (eltIdx - 1) / 2 : (eltIdx - 2) / 2;
        }
    }

    @Override
    public void add(Tip e) {
        if (end + 1 == maxSize) {
            this.resize();
        }

        heap[end++] = e;
        bubbleUp();
    }

    private void bubbleDown(int start) {
        int eltIdx = start;
        int childIdx = eltIdx * 2 + 1;
        while (childIdx < end) {
            Tip elt = (Tip) heap[eltIdx];
            Tip child = (Tip) heap[childIdx];
            if (childIdx + 1 < end && child.compareTo(heap[childIdx + 1]) < 0)
                child = (Tip) heap[++childIdx];
            if (elt.compareTo(child) >= 0)
                return;
            swap(eltIdx, childIdx);
            eltIdx = childIdx;
            childIdx = eltIdx * 2 + 1;
        }
    }

    @Override
    public Tip removeFirst() {
        if (this.isEmpty())
            throw new java.util.NoSuchElementException();

        Tip elt = (Tip) heap[0];
        swap(0, --end);
        bubbleDown(0);
        return elt;
    }

    @Override
    public Tip getFirst() {
        if (this.isEmpty()) throw new
                java.util.NoSuchElementException();
        return (Tip) heap[0];
    }

    @Override
    public int depth() {
        if (end == 0) return 0;
        return (int)
                (Math.log(end) / Math.log(2)) + 1;
    }

    @Override
    public boolean isEmpty() {
        return end == 0;
    }

    @Override
    public Tip remove(Tip e) {
        PrioritetnaVrsta<Tip> tmp = new PrioritetnaVrsta<>(this.maxSize);
        Tip rez = null;

        while (!this.isEmpty()) {
            Tip p = this.removeFirst();

            if (p.equals(e)) {
                rez = p;
                break;
            }

            tmp.add(p);
        }

        while (!tmp.isEmpty()) {
            this.add(tmp.removeFirst());
        }

        return rez;
    }

    @Override
    public boolean exists(Tip e) {
        PrioritetnaVrsta<Tip> tmp = new PrioritetnaVrsta<>(this.maxSize);
        boolean rez = false;

        while (!this.isEmpty()) {
            Tip p = this.removeFirst();
            tmp.add(p);
            if (p.equals(e)) {
                rez = true;
                break;
            }
        }

        while (!tmp.isEmpty()) {
            this.add(tmp.removeFirst());
        }

        return rez;
    }

    @Override
    public List<Tip> asList() {
        List<Tip> list = new ArrayList<>();

        for (int i = 0; i < end; i++) {
            list.add((Tip) heap[i]);
        }

        return list;
    }

    @Override
    public void print() {
        for (int i = 0; i < this.size(); i++) {
            System.out.print(heap[i] + " ");
            if (i == this.size() -1 ){
                System.out.print("\n");
            }
        }
    }

    @Override
    public void save(OutputStream outputStream) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeInt(this.size());
        for (int i = 0; i < this.size(); i++) {
            out.writeObject(heap[i]);
        }
    }

    @Override
    public void restore(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(inputStream);
        int count = in.readInt();
        heap = new Object[count];
        for (int i = 0; i < count; i++) {
            heap[i] = in.readObject();
        }

    }

    @Override
    public int size() {
        return end;
    }

    private void swap(int a, int b) {
        Object tmp = heap[a];
        heap[a] = heap[b];
        heap[b] = tmp;
    }

}