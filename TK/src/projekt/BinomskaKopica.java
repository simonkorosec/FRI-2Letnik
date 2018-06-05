package projekt;

import java.io.*;
import java.util.*;

public class BinomskaKopica<Tip> implements Seznam<Tip>, Serializable {

    private BinomskaKopicaNode<Tip> topNode;
    private Comparator<Tip> comparator;


    BinomskaKopica() {
        topNode = null;
    }

    public BinomskaKopica(Comparator<Tip> comparator) {
        super();
        this.comparator = comparator;
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
                //} else if (trenutni.key.compareTo(nasledni.key) >= 0) {
            } else if (comparator.compare(trenutni.key, nasledni.key) >= 0) {
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
    public void add(Tip e) throws IllegalArgumentException {
        if (topNode == null) {
            topNode = new BinomskaKopicaNode<>(e);
        } else {
            if (exists(e)) {
                throw new IllegalArgumentException();
            }
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
            throw new NoSuchElementException();
        }

        Tip max = topNode.key;
        BinomskaKopicaNode<Tip> node = topNode.sibling;

        while (node != null) {
            if (comparator.compare(max, node.key) < 0) {
            //if (max.compareTo(node.key) < 0) {
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
    public Tip search(Tip e) {
        BinomskaKopicaNode<Tip> node = null;
        node = find(e);
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.key;
    }

    @Override
    public Tip remove(Tip e) {
        BinomskaKopicaNode<Tip> node = find(e);
        if (node == null) {
            throw new NoSuchElementException();
        } else {
            Tip key = node.key;
            node.delete = true;
            siftUp(node);
            delete();
            return key;
        }
    }

/*
    public Tip remove(String EMSO) {
        BinomskaKopicaNode<Tip> node = find(EMSO);
        if (node == null) {
            throw new NoSuchElementException();
        } else {
            node.delete = true;
            siftUp(node);
            delete();
        }

        return node.key;
    }

    public Tip remove(String ime, String priimek) {
        BinomskaKopicaNode<Tip> node = find(ime, priimek);
        if (node == null) {
            throw new NoSuchElementException();
        } else {
            node.delete = true;
            siftUp(node);
            delete();
        }

        return node.key;
    }
*/

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
        return null != find(e);
    }

    private BinomskaKopicaNode<Tip> find(Tip key) {
        BinomskaKopicaNode<Tip> node = null;
        BinomskaKopicaNode<Tip> trenutni = topNode;

        while (trenutni != null) {
            if (comparator.compare(trenutni.key, key) == 0) {
                node = trenutni;
                break;
            } else if (comparator.compare(trenutni.key, key) > 0) {
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
        List<Tip> list = new ArrayList<>();
        while (!isEmpty()){
            list.add(removeFirst());
        }
        for (Tip e : list) {
            add(e);
        }
        Collections.reverse(list);
        return list;

        //return postorder(topNode);
    }


    @Override
    public void print() {
        List<Tip> list = asList();
        //list.sort(new ComperatorImePriimek());
        for (Tip t : list) {
            System.out.println("\t" + t);
        }
    }

    @Override
    public void save(OutputStream outputStream) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(topNode);
    }

    @Override
    public void restore(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(inputStream);
        topNode = (BinomskaKopicaNode<Tip>) in.readObject();
    }

    private class BinomskaKopicaNode<T> implements Serializable {
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

class ComperatorImePriimek implements Comparator<Oseba>, Serializable {

    @Override
    public int compare(Oseba o1, Oseba o2) {
        int c = o1.getPriimek().compareTo(o2.getPriimek());
        if (c == 0) {
            return o1.getIme().compareTo(o2.getIme());
        } else {
            return c;
        }
    }
}

class ComperatorEMSO implements Comparator<Oseba>, Serializable {

    @Override
    public int compare(Oseba o1, Oseba o2) {
        return o1.getEMSO().compareTo(o2.getEMSO());
    }
}