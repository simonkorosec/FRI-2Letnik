package aps2.patricia;

import java.util.ArrayList;

/**
 * @author matevz
 */
public class PatriciaSetNode {
    /**
     * Linked list of children ordered by their labels.
     */
    public PatriciaSetNode firstChild;
    public PatriciaSetNode nextSibling;
    /**
     * The parent node.
     */
    PatriciaSetNode parent;
    /**
     * Label on the incoming edge.
     * The length of label is always greater than 0, except for the root node,
     * where the label is empty.
     */
    private String label;
    /**
     * True, if the node is terminal node (ie. the node corresponding to the inserted key)
     */
    private boolean terminal;

    PatriciaSetNode(String label, boolean terminal) {
        this.label = label;
        this.terminal = terminal;
        this.parent = null;
    }

    /**
     * Adds a given node as a new child.
     * If a child with the same first outgoing character already exists, insertion
     * fails. Otherwise returns True.
     */
    public boolean addChild(PatriciaSetNode node) {
        if (firstChild == null) {
            firstChild = node;
            node.parent = this;
            return true;
        } else if (firstChild.label.compareTo(node.label) > 0) {
            node.nextSibling = firstChild;
            node.parent = this;
            firstChild = node;
            return true;
        } else {
            return firstChild.addSibling(null, node);
        }
    }


    /**
     * Adds a node as a sibling to current node
     *
     * @param prevNode  The previus node
     * @param node      The node that will be added
     * @return          true if inserted, false otherwise
     */
    private boolean addSibling(PatriciaSetNode prevNode, PatriciaSetNode node) {
        if (label.charAt(0) == node.label.charAt(0)) {
            return false;
        } else if (label.charAt(0) > node.label.charAt(0)) {
            node.parent = this.parent;
            node.nextSibling = this;
            prevNode.nextSibling = node;
            return true;
        } else if (nextSibling == null) {
            nextSibling = node;
            node.parent = this.parent;
            return true;
        } else {
            return nextSibling.addSibling(this, node);
        }
    }

    /**
     * Removes a child with the given first outgoing character. Returns true, if
     * a child was removed; otherwise false.
     */
    public boolean removeChild(char c) {
        if (firstChild == null){
            return false;
        } else if (firstChild.label.charAt(0) == c){
            firstChild = firstChild.nextSibling;
            return true;
        } else {
            return firstChild.removeSibling(null, c);
        }

    }

    /**
     * Removes sibling with given character
     *
     * @param prevNode  The previus node
     * @param c         The charater by which we delete the node
     * @return          true if removed, false otherwise
     */
    private boolean removeSibling(PatriciaSetNode prevNode, char c) {
        if (label.charAt(0) == c){
            prevNode.nextSibling = this.nextSibling;
            return true;
        } else if (nextSibling == null){
            return false;
        } else {
            return nextSibling.removeSibling(this, c);
        }
    }

    /**
     * Returns the child with the given first character on its label or null, if
     * such a child doesn't exist.
     */
    public PatriciaSetNode getChild(char c) {
        return (firstChild == null) ? null : firstChild.getSibling(c);
    }

    /**
     * Finds sibling by firt character
     *
     * @param c The chraracter that we search by
     * @return  PatriciaSetNode which label starts with c or null if not found
     */
    private PatriciaSetNode getSibling(char c) {
        if (label.charAt(0) == c) {
            return this;
        } else if (nextSibling == null) {
            return null;
        } else {
            return nextSibling.getSibling(c);
        }
    }

    /**
     * Returns the number of children.
     */
    public int countChildren() {
        return (firstChild == null) ? 0 : firstChild.countSiblings();
    }

    /**
     * Count the number of siblings
     *
     * @return Number of siblings
     */
    private int countSiblings() {
        return (nextSibling == null) ? 1 : 1 + nextSibling.countSiblings();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String l) {
        this.label = l;
    }

    public boolean isTerminal() {
        return this.terminal;
    }

    public void setTerminal(boolean t) {
        this.terminal = t;
    }

    public PatriciaSetNode getParent() {
        return this.parent;
    }

    public void setParent(PatriciaSetNode parent) {
        this.parent = parent;
    }

    public void setParentAll(PatriciaSetNode parent){
        this.parent = parent;
        if (nextSibling != null){
            nextSibling.setParentAll(parent);
        }
    }

    /**
     * All terminal nodes labels
     *
     * @return terminal nodes keyes
     */
    public ArrayList<String> getKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(label);
        PatriciaSetNode node = firstChild;

        while (node != null){
            for (String str : node.getKeys()){
                keys.add(label + str);
            }
            node = node.nextSibling;
        }

        return keys;
    }
}
