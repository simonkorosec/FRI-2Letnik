package psa.naloga1;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the (unbalanced) Binary Search Tree set node.
 */
public class BSTSetNode {
    private static int counter;
    private BSTSetNode left, right, parent;
    private int key;

    public BSTSetNode(BSTSetNode l, BSTSetNode r, BSTSetNode p, int key) {
        super();
        this.left = l;
        this.right = r;
        this.parent = p;
        this.key = key;
    }

    public BSTSetNode getLeft() {
        return left;
    }

    public void setLeft(BSTSetNode left) {
        this.left = left;
    }

    public BSTSetNode getRight() {
        return right;
    }

    public void setRight(BSTSetNode right) {
        this.right = right;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int compare(BSTSetNode node) {
        counter++;
        return node.key - this.key;
    }

    public int getCounter() {
        return counter;
    }

    public void resetCounter() {
        counter = 0;
    }

    /**
     * If the element doesn't exist yet, adds the given element to the subtree.
     *
     * @param element Given key wrapped inside an empty BSTNode instance
     * @return true, if the element was added; false otherwise.
     */
    public boolean add(BSTSetNode element) {
        int comp = this.compare(element);

        if (comp == 0) {
            return false;
        } else if (comp < 0) {
            if (this.left == null) {
                this.setLeft(element);
                this.left.parent = this;
                return true;
            } else {
                return this.left.add(element);
            }
        } else {
            if (this.right == null) {
                this.setRight(element);
                this.right.parent = this;
                return true;
            } else {
                return this.right.add(element);
            }
        }
    }

    /**
     * Finds and removes the element with the given key from the subtree.
     *
     * @param element Given key wrapped inside an empty BSTNode instance
     * @return true, if the element was found and removed; false otherwise.
     */
    public boolean remove(BSTSetNode element) {
        int comp = this.compare(element);


        if (comp == 0) {
            if (this.left == null && this.right == null) { // list
                int c = this.compare(this.parent);
                if (c <= 0) {
                    this.parent.setRight(null);
                } else {
                    this.parent.setLeft(null);
                }
                this.parent = null;
            } else if (this.right != null && this.left != null) { // 2 otroka
                BSTSetNode min = getRight().findMin();
                this.setKey(min.getKey());
                this.right.remove(min);

            } else if (this.left != null) {     // samo levi otrok
                if (this.key > this.parent.getKey()) {
                    this.parent.setRight(this.left);
                    this.left.parent = this.parent;

                    this.left = null;
                    this.parent = null;

                } else if (this.key < this.parent.getKey()) {
                    this.parent.setLeft(this.left);
                    this.left.parent = this.parent;

                    this.left = null;
                    this.parent = null;
                }
            } else {    // samo desn otrok
                if (this.key >= this.parent.getKey()) {
                    this.parent.setRight(this.right);
                    this.right.parent = this.parent;

                    this.right = null;
                    this.parent = null;

                } else if (this.key < this.parent.getKey()) {
                    this.parent.setLeft(this.right);
                    this.right.parent = this.parent;

                    this.right = null;
                    this.parent = null;
                }
            }


            return true;

        } else if (comp < 0) {
            if (this.left == null) {
                return false;
            }
            return this.left.remove(element);
        } else {
            if (this.right == null) {
                return false;
            }
            return this.right.remove(element);
        }

    }

    /**
     * Checks whether the element with the given key exists in the subtree.
     *
     * @param element Query key wrapped inside an empty BSTNode instance
     * @return true, if the element is contained in the subtree; false otherwise.
     */
    public boolean contains(BSTSetNode element) {
        int comp = this.compare(element);

        if (comp == 0) {
            return true;
        } else if (comp < 0) {
            if (this.left != null) {
                return this.left.contains(element);
            }
            return false;
        } else {
            if (this.right != null) {
                return this.right.contains(element);
            }
            return false;
        }

    }

    /**
     * Finds the smallest element in the subtree.
     * This function is in some cases used during
     * the removal of the element.
     *
     * @return Smallest element in the subtree
     */
    public BSTSetNode findMin() {
        if (this.left == null) {
            return this;
        }
        return this.left.findMin();
    }

    /**
     * Depth-first pre-order traversal of the BST.
     *
     * @return List of elements stored in BST obtained by pre-order traversing the tree.
     */
    List<Integer> traversePreOrder() {

        List<Integer> list = new LinkedList<>();
        list.add(this.getKey());
        if (this.left != null) {
            list.addAll(this.getLeft().traversePreOrder());
        }
        if (this.right != null) {
            list.addAll(this.getRight().traversePreOrder());
        }
        return list;
    }

    /**
     * Depth-first in-order traversal of the BST.
     *
     * @return List of elements stored in BST obtained by in-order traversing the tree.
     */
    List<Integer> traverseInOrder() {
        List<Integer> list = new LinkedList<>();
        if (this.left != null) {
            list.addAll(this.getLeft().traverseInOrder());
        }
        list.add(this.getKey());
        if (this.right != null) {
            list.addAll(this.getRight().traverseInOrder());
        }
        return list;
    }

    /**
     * Depth-first post-order traversal of the BST.
     *
     * @return List of elements stored in BST obtained by post-order traversing the tree.
     */
    List<Integer> traversePostOrder() {
        List<Integer> list = new LinkedList<>();
        if (this.left != null) {
            list.addAll(this.getLeft().traversePostOrder());
        }
        if (this.right != null) {
            list.addAll(this.getRight().traversePostOrder());
        }
        list.add(this.getKey());
        return list;
    }

    /**
     * Breadth-first (or level-order) traversal of the BST.
     *
     * @return List of elements stored in BST obtained by breadth-first traversal of the tree.
     */
    List<Integer> traverseLevelOrder() {
        List<Integer> list = new LinkedList<>();
        List<BSTSetNode> zaPregled = new LinkedList<>();
        zaPregled.add(this);

        while (!zaPregled.isEmpty()) {
            BSTSetNode el = zaPregled.remove(0);
            list.add(el.getKey());

            if (el.getLeft() != null) {
                zaPregled.add(el.getLeft());
            }
            if (el.getRight() != null) {
                zaPregled.add(el.getRight());
            }
        }

        return list;
    }
}
