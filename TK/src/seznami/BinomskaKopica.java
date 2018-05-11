package seznami;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BinomskaKopica<Tip extends Comparable> implements Seznam<Tip> {

    private BinomskaKopicaNode<Tip> topNode;

    public BinomskaKopica() {
        topNode = null;
    }

    private void merg(BinomskaKopicaNode<Tip> heap2) {
        BinomskaKopicaNode<Tip> heap1 = topNode;

        while ((heap1 != null) && (heap2 != null)) {
            if (heap1.degree == heap2.degree) {
                BinomskaKopicaNode<Tip> tmp = heap2;
                heap2 = heap2.sibling;
                tmp.sibling = heap1.sibling;
                heap1.sibling = tmp;
                heap1 = tmp.sibling;
            } else if (heap1.degree < heap2.degree) {
                    heap1 = heap1.sibling;
            } else {
                BinomskaKopicaNode<Tip> tmp = heap1;
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

    private void union() {
        BinomskaKopicaNode<Tip> prejsni = null;
        BinomskaKopicaNode<Tip> trenutni = topNode;
        BinomskaKopicaNode<Tip> nasledni = topNode.sibling;

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

    private void heapUnion(BinomskaKopicaNode<Tip> node) {
        merg(node);
        union();
    }

    @Override
    public void add(Tip e) {
        if (topNode == null) {
            topNode = new BinomskaKopicaNode<>(e);
        } else {
            heapUnion(new BinomskaKopicaNode<>(e));
        }
    }

    @Override
    public Tip removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        BinomskaKopicaNode<Tip> trenutni = topNode;
        BinomskaKopicaNode<Tip> prejsni = null;
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

        ArrayList<BinomskaKopicaNode<Tip>> tmp = new ArrayList<>();
        while (trenutni != null) {
            trenutni.parent = null;
            tmp.add(trenutni);
            trenutni = trenutni.sibling;
            tmp.get(tmp.size() - 1).sibling = null;
        }

        for (BinomskaKopicaNode<Tip> node : tmp) {
            if (topNode == null) {
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
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        Tip max = topNode.key;
        BinomskaKopicaNode<Tip> node = topNode.sibling;

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
        BinomskaKopicaNode<Tip> trenutni = topNode;

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

        BinomskaKopicaNode<Tip> node = topNode;
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
        BinomskaKopicaNode<Tip> node = search(e);
        if (node == null) {
            throw new java.util.NoSuchElementException();
        } else {
            node.delete = true;
            siftUp(node);
            delete();
        }

        return e;
    }

    private void delete() {
        BinomskaKopicaNode<Tip> trenutni = topNode;
        BinomskaKopicaNode<Tip> prejsni = null;

        while (!trenutni.delete) {
            prejsni = trenutni;
            trenutni = trenutni.sibling;
        }

        if (prejsni == null) {
            topNode = trenutni.sibling;
        } else {
            prejsni.sibling = trenutni.sibling;
        }

        trenutni = trenutni.child;

        ArrayList<BinomskaKopicaNode<Tip>> tmp = new ArrayList<>();
        while (trenutni != null) {
            trenutni.parent = null;
            tmp.add(trenutni);
            trenutni = trenutni.sibling;
            tmp.get(tmp.size() - 1).sibling = null;
        }

        for (BinomskaKopicaNode<Tip> node : tmp) {
            if (topNode == null) {
                topNode = node;
            } else {
                merg(node);
            }
        }

        if (topNode != null) {
            union();
        }

    }

    private void siftUp(BinomskaKopicaNode<Tip> node) {
        while (node.parent != null) {
            Tip keyParent = node.parent.key;
            node.parent.key = node.key;
            node.key = keyParent;

            node.parent.delete = true;
            node.delete = false;

            node = node.parent;
        }
    }

    @Override
    public boolean exists(Tip e) {
        return null != search(e);
    }

    /**
     * Method that finds the node with the same key.
     *
     * @param key searched key
     * @return node with the same key or null if not found
     */
    private BinomskaKopicaNode<Tip> search(Tip key) {
        BinomskaKopicaNode<Tip> node = null;
        BinomskaKopicaNode<Tip> trenutni = topNode;

        while (trenutni != null) {
            if (trenutni.key.compareTo(key) == 0) {
                node = trenutni;
                break;
            } else if (trenutni.key.compareTo(key) > 0) {
                if (trenutni.child != null) {
                    trenutni = trenutni.child;
                    continue;
                }
            }
            if (trenutni.sibling != null) {
                trenutni = trenutni.sibling;
            } else {
                trenutni = trenutni.parent;
                if (trenutni != null) {
                    trenutni = trenutni.sibling;
                } else {
                    break;
                }
            }
        }

        return node;
    }

    @Override
    public List<Tip> asList() {
        return postorder(topNode);
    }

    private List<Tip> postorder(BinomskaKopicaNode<Tip> node) {
        List<Tip> list = new ArrayList<>();

        if (node != null) {
            list.addAll(postorder(node.child));
            list.add(node.key);
            list.addAll(postorder(node.sibling));
        }

        return list;
    }

    private class BinomskaKopicaNode<T extends Comparable> {
        private T key;
        private int degree;
        private boolean delete;
        private BinomskaKopicaNode<T> parent;
        private BinomskaKopicaNode<T> child;
        private BinomskaKopicaNode<T> sibling;

        BinomskaKopicaNode(T key) {
            this.key = key;
            this.degree = 0;
            this.parent = null;
            this.child = null;
            this.sibling = null;
            this.delete = false;
        }

    }
}