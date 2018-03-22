package aps2.hashmap;

import org.junit.Test;
import org.omg.PortableInterceptor.INACTIVE;

/**
 * Hash map with open addressing.
 */
public class HashMapOpenAddressing {
    private Element table[]; // table content, if element is not present, use Integer.MIN_VALUE for Element's key
    private boolean pomozna[];
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
        this.pomozna = new boolean[m];
        this.h = h;
        this.c = c;
        this.m = m;

        // init empty slot as MIN_VALUE
        for (int i = 0; i < m; i++) {
            table[i] = new Element(Integer.MIN_VALUE, "");
            pomozna[i] = false;
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
        for (int i = 0; i < this.m; i++) {
            int index = nextHash(k, i);
            if (this.table[index].key == Integer.MIN_VALUE) {
                this.table[index] = new Element(k, v);
                this.pomozna[index] = true;
                return true;
            }

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
        if (this.contains(k)) {
            for (int i = 0; i < this.m; i++) {
                if (this.table[i].key == k) {
                    this.table[i] = new Element(Integer.MIN_VALUE, "");
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Finds the element.
     *
     * @param k Element key
     * @return true, if the element was found; false otherwise.
     */
    public boolean contains(int k) {
        for (int i = 0; i < this.m; i++) {
            int index = nextHash(k, i);
            if (this.table[index].key != Integer.MIN_VALUE && this.table[index].key == k) {
                return true;
            }
            if (this.table[index].key == Integer.MIN_VALUE && !this.pomozna[index]) {
                break;
            }
        }

        return false;
    }

    /**
     * Maps the given key to its value, if the key exists in the hash map.
     *
     * @param k Element key
     * @return The value for the given key or null, if such a key does not exist.
     */
    public String get(int k) {
        for (int i = 0; i < this.m; i++) {
            int index = nextHash(k, i);
            if (this.table[index].key != Integer.MIN_VALUE && this.table[index].key == k) {
                return this.table[index].value;
            }
            if (this.table[index].key == Integer.MIN_VALUE && !this.pomozna[index]) {
                break;
            }
        }

        return null;
    }

    private int hashIndex(int k) {
        if (this.h == aps2.hashmap.HashFunction.HashingMethod.DivisionMethod) {
            return aps2.hashmap.HashFunction.DivisionMethod(k, this.m);
        }
        return aps2.hashmap.HashFunction.KnuthMethod(k, this.m);
    }

    private int nextHash(int k, int i) {
        if (this.c == CollisionProbeSequence.LinearProbing) {
            return (hashIndex(k) + i) % this.m;
        } else if (this.c == CollisionProbeSequence.QuadraticProbing) {
            return (hashIndex(k) + i * i) % this.m;
        } else if (this.c == CollisionProbeSequence.DoubleHashing) {
            return (hashIndex(k) + i * hashIndex(k)) % this.m;
        }
        return -1;
    }


}

