package seznami;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BinomialHeap<Tip extends Comparable> implements Seznam<Tip> {

    private BinomialHeapNode<Tip> topNode;

    public BinomialHeap() {
        topNode = null;
    }

    private void merg(BinomialHeap<Tip> node) {
        BinomialHeapNode<Tip> heap1 = topNode;
        BinomialHeapNode<Tip> heap2 = node.topNode;

        while ((heap1 != null) && (heap2 != null)) {
            if (heap1.degree == heap2.degree) {
                BinomialHeapNode<Tip> tmp = heap2;
                heap2 = heap2.sibling;
                tmp.sibling = heap1.sibling;
                heap1.sibling = tmp;
                heap1 = tmp.sibling;
            } else if (heap1.degree < heap2.degree) {
                if ((heap1.sibling == null) || (heap1.sibling.degree > heap2.degree)) {
                    BinomialHeapNode<Tip> tmp = heap2;
                    heap2 = heap2.sibling;
                    tmp.sibling = heap1.sibling;
                    heap1.sibling = tmp;
                    heap1 = tmp.sibling;
                } else {
                    heap1 = heap1.sibling;
                }
            } else {
                BinomialHeapNode<Tip> tmp = heap1;
                heap1 = heap2;
                heap2 = heap2.sibling;
                heap1.sibling = tmp;
                if (tmp == topNode) {
                    topNode = heap1;
                }
            }
        }

        if (heap1 == null) {
            heap1 = topNode;
            while (heap1.sibling != null) {
                heap1 = heap1.sibling;
            }
            heap1.sibling = heap2;
        }


    }

    private void merg(BinomialHeapNode<Tip> node) {
        BinomialHeap<Tip> t = new BinomialHeap<>();
        t.topNode = node;
        merg(t);
    }

    private void merg(Tip el) {
        BinomialHeap<Tip> t = new BinomialHeap<>();
        t.topNode = new BinomialHeapNode<>(el);
        merg(t);
    }

    private void union() {
        BinomialHeapNode<Tip> prejsni = null;
        BinomialHeapNode<Tip> trenutni = topNode;
        BinomialHeapNode<Tip> nasledni = topNode.sibling;

        while (nasledni != null) {
            if ((trenutni.degree != nasledni.degree) ||
                    ((nasledni.sibling != null) && (nasledni.sibling.degree == trenutni.degree))) {
                prejsni = trenutni;
                trenutni = nasledni;
            } else if (trenutni.key.compareTo(nasledni.key) >= 0) {
                trenutni.sibling = nasledni.sibling;
                nasledni.parent = trenutni;
                nasledni.sibling = trenutni.child;
                trenutni.child = nasledni;
                trenutni.degree++;
            } else {
                if (prejsni == null) {
                    topNode = nasledni;
                } else {
                    prejsni.sibling = nasledni;
                }

                trenutni.parent = nasledni;
                trenutni.sibling = nasledni.child;
                nasledni.child = trenutni;
                nasledni.degree++;
                trenutni = nasledni;
            }

            nasledni = trenutni.sibling;
        }
    }

    private void heapUnion(BinomialHeapNode<Tip> node) {
        merg(node);

        union();
    }

    @Override
    public void add(Tip e) {
        if (topNode == null) {
            topNode = new BinomialHeapNode<>(e);
        } else {
            heapUnion(new BinomialHeapNode<>(e));
        }
    }

    @Override
    public Tip removeFirst() {
        if (topNode == null) {
            throw new NoSuchElementException();
        }

        BinomialHeapNode<Tip> trenutni = topNode;
        BinomialHeapNode<Tip> prejsni = null;
        Tip max = getFirst();

        while (!trenutni.key.equals(max)) {
            prejsni = trenutni;
            trenutni = trenutni.sibling;
        }

        if (prejsni == null) {
            topNode = trenutni.sibling;
        } else {
            prejsni.sibling = trenutni.sibling;
        }

        trenutni = trenutni.child;

        ArrayList<BinomialHeapNode<Tip>> tmp = new ArrayList<>();
        while (trenutni != null) {
            trenutni.parent = null;
            tmp.add(trenutni);
            trenutni = trenutni.sibling;
            tmp.get(tmp.size()-1).sibling = null;
        }

        for (BinomialHeapNode<Tip> node : tmp){
            if (topNode == null){
                topNode = node;
            } else {
                merg(node);
            }
        }

        if (topNode != null) {
            union();
        }

        return max;
    }

    @Override
    public Tip getFirst() {
        if (topNode == null) {
            throw new java.util.NoSuchElementException();
        }

        Tip max = topNode.key;
        BinomialHeapNode<Tip> node = topNode.sibling;

        while (node != null) {
            if (max.compareTo(node.key) < 0) {
                max = node.key;
            }
            node = node.sibling;
        }

        return max;
    }

    private int calcSize() {
        int s = 0;
        BinomialHeapNode<Tip> trenutni = topNode;

        while (trenutni != null) {
            s += Math.pow(2, trenutni.degree);
            trenutni = trenutni.sibling;
        }

        return s;
    }

    @Override
    public int size() {
        return calcSize();
    }

    @Override
    public int depth() {
        if (topNode == null) {
            return 0;
        }

        BinomialHeapNode<Tip> node = topNode;
        while (node.sibling != null) {
            node = node.sibling;
        }
        return node.degree;
    }

    @Override
    public boolean isEmpty() {
        return (topNode == null);
    }

    @Override
    public Tip remove(Tip e) {
        return null;
    }

    @Override
    public boolean exists(Tip e) {
        return false;
    }

    @Override
    public List<Tip> asList() {
        List<Tip> list = new ArrayList<>();
        BinomialHeapNode<Tip> node = topNode;

        while (node != null) {
            list.addAll(postorder(node));
            node = node.sibling;
        }
        return list;
    }

    private List<Tip> postorder(BinomialHeapNode<Tip> node) {
        List<Tip> list = new ArrayList<>();
        if (node.child != null) {
            list.addAll(postorder(node.child));

            if (node.child.sibling != null) {
                list.addAll(postorder(node.child.sibling));
            }
        }

        list.add(node.key);
        return list;
    }

    class BinomialHeapNode<T extends Comparable> {
        private T key;
        private int degree;
        private BinomialHeapNode<T> parent;
        private BinomialHeapNode<T> child;
        private BinomialHeapNode<T> sibling;

        BinomialHeapNode(T key) {
            this.key = key;
            this.degree = 0;
            this.parent = null;
            this.child = null;
            this.sibling = null;
        }

        public T getKey() {
            return key;
        }

        public void setKey(T key) {
            this.key = key;
        }

        public int getDegree() {
            return degree;
        }

        public void setDegree(int degree) {
            this.degree = degree;
        }
    }
}