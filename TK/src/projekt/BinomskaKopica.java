package projekt;

import java.io.*;
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
    public void add(Tip e) throws IllegalArgumentException{
        if (topNode == null) {
            topNode = new BinomskaKopicaNode<>(e);
        } else {
            if (exists(e)){
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
    public Tip search(Tip e) {
        BinomskaKopicaNode<Tip> node = null;
        if (e instanceof Oseba) {
            if (((Oseba) e).getEMSO() != null) {
                node = find(((Oseba) e).getEMSO());
            } else {
                node = find(((Oseba) e).getIme(), ((Oseba) e).getPriimek());
            }
        }

        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.key;
    }

    @Override
    public Tip remove(Tip e) {
        if (e instanceof Oseba) {
            if (((Oseba) e).getEMSO() != null) {
                remove(((Oseba) e).getEMSO());
            } else {
                remove(((Oseba) e).getIme(), ((Oseba) e).getPriimek());
            }
        }

        return e;
    }

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
        //if (e instanceof Oseba) {
            return exists(((Oseba) e).getEMSO()) || exists(((Oseba) e).getIme(), ((Oseba) e).getPriimek());
        //} else {
        //    return null != find(e);
        //}
    }

    public boolean exists(String EMSO) {
        return null != find(EMSO);
    }

    public boolean exists(String ime, String priimek) {
        return null != find(ime, priimek);
    }

   /* private BinomskaKopicaNode<Tip> find(Tip key) {
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
*/

    private BinomskaKopicaNode<Tip> find(String EMSO) {
        BinomskaKopicaNode<Tip> node = null;
        BinomskaKopicaNode<Tip> trenutni = topNode;

        while (trenutni != null) {
            if (trenutni.key.compareTo(EMSO) == 0) {
                node = trenutni;
                break;
            } else if (trenutni.key.compareTo(EMSO) > 0) {
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

    private BinomskaKopicaNode<Tip> find(String ime, String priimek) {
        BinomskaKopicaNode<Tip> node = null;
        BinomskaKopicaNode<Tip> trenutni = topNode;
        String[] s = new String[]{ime, priimek};

        while (trenutni != null) {
            if (trenutni.key.compareTo(s) == 0) {
                node = trenutni;
                break;
            } else /*if (trenutni.key.compareTo(s) > 0)*/ {
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

    @Override
    public void print() {
        List<Tip> list = asList();
        list.sort(new OsebaComparator());
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

    private class BinomskaKopicaNode<T extends Comparable> implements Serializable{
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