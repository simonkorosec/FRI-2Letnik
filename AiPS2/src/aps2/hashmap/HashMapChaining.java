package aps2.hashmap;

import java.util.LinkedList;

/**
 * Hash map employing chaining on collisions.
 */
public class HashMapChaining {
	private LinkedList<Element> table[];
	private aps2.hashmap.HashFunction.HashingMethod h;
	private int m;
	
	public HashMapChaining(int m, aps2.hashmap.HashFunction.HashingMethod h) {
		this.h = h;
		this.m = m;
		this.table = new LinkedList[m];
		for (int i=0; i<table.length; i++) {
			table[i] = new LinkedList<Element>();
		}
	}
	
	public LinkedList<Element>[] getTable() {
		return this.table;
	}
	
	/**
	 * If the element doesn't exist yet, inserts it into the set.
	 * 
	 * @param k Element key
	 * @param v Element value
	 * @return true, if element was added; false otherwise.
	 */
	public boolean add(int k, String v) {
        int index = hashIndex(k);
        Element e = new Element(k, v);
        if (this.table[index].contains(e)){
            return false;
        }
        this.table[index].add(e);
        return true;
	}

	/**
	 * Removes the element from the set.
	 * 
	 * @param k Element key
	 * @return true, if the element was removed; otherwise false
	 */
	public boolean remove(int k) {
        int index = hashIndex(k);
	    return this.table[index].remove(new Element(k, "neki"));
	}

	/**
	 * Finds the element.
	 * 
	 * @param k Element key
	 * @return true, if the element was found; false otherwise.
	 */
	public boolean contains(int k) {
        int index = hashIndex(k);
        return this.table[index].contains(new Element(k, "neki"));
	}
	
	/**
	 * Maps the given key to its value, if the key exists in the hash map.
	 * 
	 * @param k Element key
	 * @return The value for the given key or null, if such a key does not exist.
	 */
	public String get(int k) {
        int index = hashIndex(k);
        for (Element e :  this.table[index]) {
            if (e.key == k){
                return e.value;
            }
        }
        return null;
	}

    private int hashIndex(int k){
	    if (this.h == aps2.hashmap.HashFunction.HashingMethod.DivisionMethod){
            return aps2.hashmap.HashFunction.DivisionMethod(k, this.m);
        }
        return aps2.hashmap.HashFunction.KnuthMethod(k, this.m);
    }
}

