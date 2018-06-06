package imenik;

import java.io.*;
import java.util.Comparator;

public class PrioritetnaVrsta<Tip> implements Seznam<Tip> {

    private Object[] heap;
    private int end = 0;
    private final Comparator<Tip> comparator;

    public PrioritetnaVrsta(Comparator<Tip> comparator) {
        this(100, comparator);
    }

    public PrioritetnaVrsta(int maxSize, Comparator<Tip> comparator) {
        heap = new Object[maxSize];
        this.comparator = comparator;
    }

    private void bubbleUp() {
        int eltIdx = end - 1;
        while (eltIdx >= 0) {
            Tip elt = (Tip) heap[eltIdx];
            int childIdx = eltIdx * 2 + 1;
            if (childIdx < end) {
                Tip child = (Tip) heap[childIdx];
                if (childIdx + 1 < end && comparator.compare(child, (Tip) heap[childIdx + 1]) < 0) {
                    child = (Tip) heap[++childIdx];
                }
                if (comparator.compare(elt, child) >= 0) {
                    return;
                }
                swap(eltIdx, childIdx);
            }
            eltIdx = eltIdx % 2 == 1 ? (eltIdx - 1) / 2 : (eltIdx - 2) / 2;
        }
    }

    @Override
    public void add(Tip e) {
        heap[end++] = e;
        bubbleUp();
    }

    private void bubbleDown(int start) {
        int eltIdx = start;
        int childIdx = eltIdx * 2 + 1;
        while (childIdx < end) {
            Tip elt = (Tip) heap[eltIdx];
            Tip child = (Tip) heap[childIdx];
            if (childIdx + 1 < end && comparator.compare(child, (Tip) heap[childIdx + 1]) < 0) {
                child = (Tip) heap[++childIdx];
            }
            if (comparator.compare(elt, child) >= 0) {
                return;
            }
            swap(eltIdx, childIdx);
            eltIdx = childIdx;
            childIdx = eltIdx * 2 + 1;
        }
    }

    @Override
    public Tip removeFirst() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Tip elt = (Tip) heap[0];
        swap(0, --end);
        bubbleDown(0);
        return elt;
    }

    private void swap(int a, int b) {
        Object tmp = heap[a];
        heap[a] = heap[b];
        heap[b] = tmp;
    }

    @Override
    public Tip getFirst() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        return (Tip) heap[0];
    }

    @Override
    public int depth() {
        if (end == 0) {
            return 0;
        }
        return (int) (Math.log(end) / Math.log(2)) + 1;
    }

    @Override
    public boolean isEmpty() {
        return end == 0;
    }

    @Override
    public int size() {
        return end;
    }

    @Override
    public Tip remove(Tip e) {
        for (int i = 0, heapLength = heap.length; i < heapLength; i++) {
            Object aHeap = heap[i];
            if (comparator.compare((Tip) aHeap, e) == 0) {
                swap(i, 0);
                removeFirst();
                return e;
            }
        }

        throw new java.util.NoSuchElementException();
    }

    @Override
    public boolean exists(Tip e) {
        for (Object aHeap : heap) {
            if (comparator.compare((Tip) aHeap, e) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void print() {
        print(0, 0);
    }

    private void print(int index, int numTabs) {
        if (index >= end) {
            return;
        }

        print(2 * index + 2, numTabs + 1);
        for (int i = 0; i < numTabs; ++i) {
            System.out.print('\t');
        }
        System.out.println(heap[index]);
        print(2 * index + 1, numTabs + 1);
    }

    @Override
    public void save(OutputStream outputStream) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeInt(end);
        for (int i = 0; i < end; i++) {
            out.writeObject(heap[i]);
        }
    }

    @Override
    public void restore(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(inputStream);
        end = in.readInt();
        heap = new Object[end];
        for (int i = 0; i < end; i++) {
            heap[i] = in.readObject();
        }
    }
}
