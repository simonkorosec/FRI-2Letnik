package aps2.hashmap;

/**
 * Hash map with open addressing.
 */
public class HashMapOpenAddressing {
    private Element table[]; // table content, if element is not present, use Integer.MIN_VALUE for Element's key
    private aps2.hashmap.HashFunction.HashingMethod h;
    private CollisionProbeSequence c;
    private int m;

    public static enum CollisionProbeSequence {
        LinearProbing,    // new h(k) = (h(k) + i) mod m
        QuadraticProbing, // new h(k) = (h(k) + i^2) mod m
        DoubleHashing     // new h(k) = (h(k) + i*h(k)) mod m
    }

    public HashMapOpenAddressing(int m, aps2.hashmap.HashFunction.HashingMethod h, CollisionProbeSequence c) {
        this.table = new Element[m];
        this.h = h;
        this.c = c;
        this.m = m;

        // init empty slot as MIN_VALUE
        for (int i = 0; i < m; i++) {
            table[i] = new Element(Integer.MIN_VALUE, "");
        }
    }

    public Element[] getTable() {
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
        if (this.table[index].key == Integer.MIN_VALUE) {
            this.table[index] = new Element(k, v);
            return true;
        }
return false;
    }

    /**
     * Removes the element from the set.
     *
     * @param k Element key
     * @return true, if the element was removed; otherwise false
     */
    public boolean remove(int k) {
        throw new UnsupportedOperationException("You need to implement this function!");
    }

    /**
     * Finds the element.
     *
     * @param k Element key
     * @return true, if the element was found; false otherwise.
     */
    public boolean contains(int k) {
        throw new UnsupportedOperationException("You need to implement this function!");
    }

    /**
     * Maps the given key to its value, if the key exists in the hash map.
     *
     * @param k Element key
     * @return The value for the given key or null, if such a key does not exist.
     */
    public String get(int k) {
        throw new UnsupportedOperationException("You need to implement this function!");
    }

    private int hashIndex(int k) {
        if (this.h == aps2.hashmap.HashFunction.HashingMethod.DivisionMethod) {
            return aps2.hashmap.HashFunction.DivisionMethod(k, this.m);
        }
        return aps2.hashmap.HashFunction.KnuthMethod(k, this.m);
    }

}

