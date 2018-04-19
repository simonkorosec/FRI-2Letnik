package seznami;

import java.util.ArrayList;
import java.util.List;

public class Bst<Tip extends Comparable> implements Seznam<Tip> {

    private ElementBST rootNode;
    private Tip minNodeValue;

    public Bst() {
        rootNode = null;
    }

    private boolean member(Tip e) {
        return member(e, rootNode);
    }

    private boolean member(Tip e, ElementBST node) {
        if (node == null) {
            return false;
        } else if (e.compareTo(node.value) == 0) {
            return true;
        } else if (e.compareTo(node.value) < 0) {
            return member(e, node.left);
        } else {
            return member(e, node.right);
        }
    }

    private void insert(Tip e) {
        rootNode = insertLeaf(e, rootNode);
    }

    private void delete(Tip e) {
        rootNode = delete(e, rootNode);
    }

    private ElementBST insertLeaf(Tip e, ElementBST node) {
        if (node == null) {
            node = new ElementBST(e);
        } else if (e.compareTo(node.value) < 0) {
            node.left = insertLeaf(e, node.left);
        } else if (e.compareTo(node.value) > 0) {
            node.right = insertLeaf(e, node.right);
        } else {
            throw new java.lang.IllegalArgumentException(); //element ze obstaja
        }
        return node;
    }

    private ElementBST delete(Tip e, ElementBST node) {
        int c = node.compare(e);
        if (c == 0) {
            if (node.left == null && node.right == null) {
                node = null;
            } else if (node.left != null && node.right != null) {
                deleteMin(node.right);
                node.value = minNodeValue;
            } else if (node.left == null) {
                node = node.right;
            } else {
                node = node.left;
            }

        } else if (c < 0) { // Izbris v levo poddrevo
            node.left = delete(e, node.left);
        } else {            // Izbris v desno poddrevo
            node.right = delete(e, node.right);
        }
        return node;
    }

    private ElementBST deleteMin(ElementBST node) {
        if (node.left != null){
            return deleteMin(node.left);
        }

        minNodeValue = node.value;
        delete(minNodeValue, rootNode);
        return node;
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
    public Tip getFirst() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        return rootNode.value;
    }

    @Override
    public int size() {
        return countNodes(rootNode);
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
    public Tip remove(Tip e) {
        if (!this.exists(e)) {
            throw new java.util.NoSuchElementException();
        } else {
            delete(e);
        }
        return e;
    }

    @Override
    public boolean exists(Tip e) {
        return member(e);
    }

    @Override
    public List<Tip> asList() {
        return this.inorder(rootNode);
    }

    private List<Tip> inorder(ElementBST node) {
        List<Tip> list = new ArrayList<>();
        if (node.left != null){
            list.addAll(inorder(node.left));
        }
        list.add(node.value);
        if (node.right != null){
            list.addAll(inorder(node.right));
        }
        return list;
    }

    class ElementBST {

        Tip value;
        ElementBST left, right;

        ElementBST(Tip e) {
            this(e, null, null);
        }

        ElementBST(Tip e, ElementBST lt, ElementBST rt) {
            value = e;
            left = lt;
            right = rt;
        }

        int compare(Tip e) {
            return e.compareTo(this.value);
        }
    }
}
