package imenik;

import java.io.*;
import java.util.Comparator;

//drevo uporablja vstavljanje elementa kot list, lahko bi ga tudi dodajali kot koren

public class Bst<Tip> implements Seznam<Tip> {

    class ElementBST {

        Tip value;
        ElementBST left;
        ElementBST right;

        ElementBST(Tip e) {
            this(e, null, null);
        }

        ElementBST(Tip e, ElementBST lt, ElementBST rt) {
            value = e;
            left = lt;
            right = rt;
        }
    }
    private ElementBST rootNode;
    private Tip minNodeValue;
    private Comparator<Tip> comparator;


    public Bst(Comparator<Tip> comparator) {
        rootNode = null;
        this.comparator = comparator;
    }

    private boolean member(Tip e) {
        return member(e, rootNode);
    }

    private boolean member(Tip e, ElementBST node) {
        if (node == null) {
            return false;
        } else if (comparator.compare(e, node.value) == 0) {
            return true;
        } else if (comparator.compare(e, node.value) < 0) {
            return member(e, node.left);
        } else {
            return member(e, node.right);
        }
    }

    private void insert(Tip e) {
        rootNode = insertLeaf(e, rootNode);
    }

    private void delete(Tip e) {
        //PRIMER: rootNode na levi je manjkal
        rootNode = delete(e, rootNode);
    }

    private ElementBST insertLeaf(Tip e, ElementBST node) {
        if (node == null) {
            node = new ElementBST(e);
        } else if (comparator.compare(e, node.value) < 0) {
            node.left = insertLeaf(e, node.left);
        } else if (comparator.compare(e, node.value) > 0) {
            node.right = insertLeaf(e, node.right);
        } else {
            throw new java.lang.IllegalArgumentException(); //element ze obstaja
        }
        return node;
    }

    private ElementBST delete(Tip e, ElementBST node) {
        if (node != null) {
            if (comparator.compare(e, node.value) == 0) {
                if (node.left == null) {
                    node = node.right;
                } else if (node.right == null) {
                    node = node.left;
                } else {
                    node.right = deleteMin(node.right);
                    node.value = minNodeValue;
                }
            } else if (comparator.compare(e, node.value) < 0) {
                node.left = delete(e, node.left);
            } else {
                node.right = delete(e, node.right);
            }
        }
        return node;
    }

    private ElementBST deleteMin(ElementBST node) {
        if (node.left != null) {
            node.left = deleteMin(node.left);
            return node;
        } else {
            //PRIMER: manjka prenos minimalne vrednosti
            minNodeValue = node.value;
            return node.right;
        }
    }

    private int getDepth(ElementBST node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(getDepth(node.left), getDepth(node.right));
    }

    private int countNodes(ElementBST node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    @Override
    public void add(Tip e) {
        insert(e);
    }

    @Override
    public Tip removeFirst() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Tip el = rootNode.value;
        delete(rootNode.value);
        return el;
    }

    @Override
    public Tip remove(Tip e) {
        if (!this.exists(e)) {
            throw new java.util.NoSuchElementException();
        } else {
            delete(e);
        }
        return e;
    }

    @Override
    public Tip getFirst() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        return rootNode.value;
    }

    @Override
    public int depth() {
        return getDepth(rootNode);
    }

    @Override
    public boolean isEmpty() {
        return (rootNode == null);
    }

    @Override
    public int size() {
        return countNodes(rootNode);
    }

    @Override
    public boolean exists(Tip e) {
        return member(e);
    }

    @Override
    public void print() {
        print(rootNode, 0);
    }

    private void print(ElementBST node, int numTabs) {
        if (null == node) {
            return;
        }

        print(node.right, numTabs + 1);
        for (int i = 0; i < numTabs; ++i) {
            System.out.print('\t');
        }
        System.out.println(node.value);
        print(node.left, numTabs + 1);
    }

    @Override
    public void save(OutputStream outputStream) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeInt(this.size());
        save(rootNode, out);
    }

    private void save(ElementBST node, ObjectOutputStream out) throws IOException {
        if (null == node) {
            return;
        }
        save(node.left, out);
        out.writeObject(node.value);
        save(node.right, out);
    }

    @Override
    public void restore(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(inputStream);
        int count = in.readInt();
        rootNode = restore(in, count);
    }

    private ElementBST restore(ObjectInputStream in, int count) throws IOException, ClassNotFoundException {
        if (0 == count) {
            return null;
        }

        ElementBST nodeLeft = restore(in, count / 2);
        ElementBST node = new ElementBST((Tip) in.readObject());
        node.left = nodeLeft;
        node.right = restore(in, (count - 1) / 2);
        return node;
    }
}
