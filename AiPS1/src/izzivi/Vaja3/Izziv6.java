package izzivi.Vaja3;

public class Izziv6 {

}

class CollectionException extends Exception {
    public CollectionException(String msg) {
        super(msg);
    }
}
interface Collection {
    static final String ERR_MSG_EMPTY = "izzivi.Vaja3.Izziv3.Collection is empty.";
    static final String ERR_MSG_FULL = "izzivi.Vaja3.Izziv3.Collection is full.";
    boolean isEmpty();
    boolean isFull();
    int count();
    String toString();
}

interface Sequence<T extends Comparable> extends Collection {
    static final String ERR_MSG_INDEX = "Wrong index in sequence.";
    T get(int i) throws CollectionException;
    T set(int i, T x) throws CollectionException;
    void insert(int i, T x) throws CollectionException;
    T delete(int i) throws CollectionException;
    void reverse();
}