package aps2.patricia;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author matevz
 */
public class PatriciaSet {
    private final PatriciaSetNode root;

    PatriciaSet() {
        this.root = new PatriciaSetNode("", false);
    }

    /**
     * Computes and returns the longest substring in the given text repeated at
     * least 2 times by finding the deepest node.
     */
    public static String longestRepeatedSubstring(String text) {
        throw new UnsupportedOperationException("You need to implement this function!");
    }

    public PatriciaSetNode getRoot() {
        return root;
    }

    /**
     * Inserts the given key to PATRICIA tree. Returns false, if such a key
     * already exists in the tree; otherwise true.
     */
    public boolean insert(String key) {
        PatriciaSetNode node = root;
        PatriciaSetNode current;
        StringBuilder neki = new StringBuilder();

        for (int i = 0; i < key.length(); i++) {
            String label = key.substring(i);
            current = node.getChild(key.charAt(i));
            if (current == null) {
                node.addChild(new PatriciaSetNode(label, true));
                return true;
            }
            int j;
            String nodeLabel = current.getLabel();
            neki.append(nodeLabel);
            for (j = 0; j < nodeLabel.length() && j < label.length(); j++) {
                if (label.charAt(j) == nodeLabel.charAt(j)) {
                    i++;
                } else {
                    break;
                }
            }

            if (neki.toString().equals(key) && !current.isTerminal()) {
                current.setTerminal(true);
                return true;
            } else if (neki.toString().equals(key) && current.isTerminal()) {
                return false;
            }

            if (j == nodeLabel.length() && j == label.length()) {
                return false;
            } else if (j == nodeLabel.length()) {
                i--;
                node = current;
            } else {
                PatriciaSetNode insertPrev = new PatriciaSetNode(nodeLabel.substring(j), current.isTerminal());
                current.setLabel(nodeLabel.substring(0, j));
                current.setTerminal(false);
                insertPrev.firstChild = current.firstChild;
                if (current.firstChild != null) {
                    current.firstChild.setParentAll(insertPrev);
                }
                current.firstChild = insertPrev;

                if (j == label.length()) {
                    current.setTerminal(true);
                    return true;
                } else {
                    PatriciaSetNode insertNext = new PatriciaSetNode(label.substring(j), true);
                    current.addChild(insertNext);
                    return true;
                }
            }
        }

        return true;
    }

    /**
     * Removes the given key from PATRICIA tree. Returns false, if a key didn't
     * exist or wasn't a terminal node; otherwise true.
     */
    public boolean remove(String key) {
        PatriciaSetNode node = root;
        StringBuilder l = new StringBuilder();

        for (int i = 0; i < key.length(); ) {
            node = node.getChild(key.charAt(i));
            if (node == null) {
                return false;
            }

            String nodeLabel = node.getLabel();
            for (int j = 0; j < nodeLabel.length(); j++) {
                if (i < key.length() && key.charAt(i) == nodeLabel.charAt(j)) {
                    i++;
                } else {
                    return false;
                }
            }
            l.append(nodeLabel);
        }

        if (!node.isTerminal() && l.toString().equals(key)) {
            return false;
        }


        String label = node.getLabel();
        node = node.getParent();
        node.removeChild(label.charAt(0));

        if (node.firstChild.nextSibling == null){
            PatriciaSetNode child = node.firstChild;
            label = node.getLabel() + child.getLabel();
            node.setLabel(label);
            node.setTerminal(child.isTerminal());
            node.firstChild = child.firstChild;
            if (child.firstChild != null) {
                child.firstChild.setParentAll(node);
            }
        }

        return true;
    }

    /**
     * Returns true, if the given key exists in PATRICIA tree and is a terminal
     * node; otherwise false.
     */
    public boolean contains(String key) {
        PatriciaSetNode node = root;
        StringBuilder l = new StringBuilder();

        for (int i = 0; i < key.length(); ) {
            node = node.getChild(key.charAt(i));
            if (node == null) {
                return false;
            }

            String nodeLabel = node.getLabel();
            for (int j = 0; j < nodeLabel.length(); j++) {
                if (i < key.length() && key.charAt(i) == nodeLabel.charAt(j)) {
                    i++;
                } else {
                    return false;
                }
            }
            l.append(nodeLabel);
        }

        return node.isTerminal() && l.toString().equals(key);
    }

    /**
     * Returns the longest prefix of the given string which still exists in
     * PATRICIA tree.
     */
    public String longestPrefixOf(String s) {
        PatriciaSetNode node = root;
        String prefix = "";

        for (int i = 0; i < s.length(); ) {
            node = node.getChild(s.charAt(i));
            if (node == null) {
                return prefix;
            }

            String nodeLabel = node.getLabel();
            for (int j = 0; j < nodeLabel.length(); j++) {
                if (i < s.length() && s.charAt(i) == nodeLabel.charAt(j)) {
                    i++;
                    prefix += nodeLabel.charAt(j);
                } else {
                    return prefix;
                }
            }
        }

        return prefix;
    }

    /**
     * Returns all stored strings with the given prefix.
     */
    public Set<String> keysWithPrefix(String s) {
        PatriciaSetNode node = root;
        StringBuilder prefix = new StringBuilder();
        Set<String> keys = new TreeSet<>();

        for (int i = 0; i < s.length(); ) {
            node = node.getChild(s.charAt(i));
            if (node == null) {
                break;
            }

            String nodeLabel = node.getLabel();
            for (int j = 0; j < nodeLabel.length(); j++) {
                if (i < s.length() && s.charAt(i) == nodeLabel.charAt(j)) {
                    i++;
                } else {
                    break;
                }
            }

            if (prefix.toString().length() + nodeLabel.length() < s.length()) {
                prefix.append(nodeLabel);
            }
        }

        if (node != null) {
            for (String k : node.getKeys()) {
                keys.add(prefix + k);
            }
        }

        return keys;
    }

    /**
     * Returns the node with the largest string-depth which is not a leaf.
     * String-depth is the sum of label lengths on that root-to-node path.
     * This node also corresponds to the longest common prefix of at least two
     * inserted strings.
     */
    public PatriciaSetNode getLongestPrefixNode() {
        throw new UnsupportedOperationException("You need to implement this function!");
    }
}
