package aps2.patricia;

import java.util.Stack;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author matevz
 *
 */
public class PatriciaSet {
	private PatriciaSetNode root;
	
	PatriciaSet() {
		this.root = new PatriciaSetNode("", false);
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
        PatriciaSetNode current = null;

        for (int i = 0; i < key.length(); i++) {
            String label = key.substring(i);
            current = node.getChild(key.charAt(i));
            if (current == null){
                node.addChild(new PatriciaSetNode(label,true));
                return true;
            }
            int j;
            String nodeLabel = current.getLabel();
            for (j = 0; j < nodeLabel.length() && j <label.length(); j++) {
                if (label.charAt(j) == nodeLabel.charAt(j)){
                    i++;
                } else {
                    break;
                }
            }

            if (i == key.length() && label.length() == j){
                return false;
            }

            if (j == nodeLabel.length() && j == label.length()){
                return false;
            } else if (j == nodeLabel.length()){
                i--;
                node = current;
            } else {
                PatriciaSetNode insertPrev = new PatriciaSetNode(nodeLabel.substring(j), current.isTerminal());
                current.setLabel(nodeLabel.substring(0, j));
                current.setTerminal(false);
                insertPrev.firstChild = current.firstChild;
                if (current.firstChild != null){
                    current.firstChild.setParentAll(insertPrev);
                }
                current.firstChild = insertPrev;

                if (j == label.length()){
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
		throw new UnsupportedOperationException("You need to implement this function!");
	}
	
	/**
	 * Returns true, if the given key exists in PATRICIA tree and is a terminal
	 * node; otherwise false.
	 */
	public boolean contains(String key) {
		//throw new UnsupportedOperationException("You need to implement this function!");
		PatriciaSetNode node = root;
        String l = "";

        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            node = node.getChild(c);
            if (node == null){
                return false;
            }
            String label = node.getLabel();
            l += label;

            for (int j = 0; j < label.length(); j++) {
                if (c == label.charAt(j) && ++i < key.length()){
                    c = key.charAt(i);
                } else {
                    return false;
                }
            }
        }

        return node.isTerminal();
	}
	
	/**
	 * Returns the longest prefix of the given string which still exists in
	 * PATRICIA tree.
	 */
	public String longestPrefixOf(String s) {
		throw new UnsupportedOperationException("You need to implement this function!");
	}
	
	/**
	 * Returns all stored strings with the given prefix.
	 */
	public Set<String> keysWithPrefix(String s) {
		throw new UnsupportedOperationException("You need to implement this function!");
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

	/**
	 * Computes and returns the longest substring in the given text repeated at
	 * least 2 times by finding the deepest node.
	 */
	public static String longestRepeatedSubstring(String text) {
		throw new UnsupportedOperationException("You need to implement this function!");
	}
}
