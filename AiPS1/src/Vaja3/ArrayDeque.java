package Vaja3;

import java.util.Arrays;

public class ArrayDeque<T> implements Deque<T>, Stack<T>, Sequence<T> {
    private static final int DEFAULT_CAPACITY = 64;

    T [] elementi;
    int stElementov;
    int front;
    int back;


    public ArrayDeque() {
        this.elementi = (T[]) new Object[DEFAULT_CAPACITY];
    }

    @Override
    public boolean isEmpty() {
        return stElementov == 0;
    }

    @Override
    public boolean isFull() {
        return this.stElementov == this.elementi.length;
    }

    @Override
    public int count() {
        return this.stElementov;
    }

    @Override
    public T top() throws CollectionException {
        return null;
    }

    @Override
    public void push(T x) throws CollectionException {

    }

    @Override
    public T pop() throws CollectionException {
        return null;
    }

    @Override
    public T front() throws CollectionException {
        if (this.isEmpty())
            throw new CollectionException(ERR_MSG_EMPTY);
        T tmp = this.elementi[this.front];
        this.front++;
        this.stElementov--;
        return tmp;
    }

    @Override
    public T back() throws CollectionException {
        if (this.isEmpty())
            throw new CollectionException(ERR_MSG_EMPTY);
        T tmp = this.elementi[this.back - 1];
        this.back--;
        this.stElementov--;
        return tmp;
    }

    @Override
    public void enqueue(T x) throws CollectionException {
        if (this.isFull())
            throw new CollectionException(ERR_MSG_FULL);
        this.elementi[this.back % this.elementi.length] = x;
        this.stElementov++;
        this.back++;
    }

    @Override
    public void enqueueFront(T x) throws CollectionException {
        if (this.isFull())
            throw new CollectionException(ERR_MSG_FULL);
        this.front = (this.front - 1 + this.elementi.length) % this.elementi.length;
        this.elementi[this.front] = x;
        this.stElementov++;
    }

    @Override
    public T dequeue() throws CollectionException {
        return null;
    }

    @Override
    public T dequeueBack() throws CollectionException {
        return null;
    }

    @Override
    public T get(int i) throws CollectionException {
        return null;
    }

    @Override
    public T set(int i, T x) throws CollectionException {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        int poz = this.front;

        while (poz != this.back - 1){
            poz = poz % this.elementi.length;
            sb.append(this.elementi[poz]);
            poz++;
        }

        sb.append("]");
        return sb.toString();
    }
}

class CollectionException extends Exception {
    public CollectionException(String msg) {
        super(msg);
    }
}

interface Collection {
    static final String ERR_MSG_EMPTY = "Collection is empty.";
    static final String ERR_MSG_FULL = "Collection is full.";

    boolean isEmpty();
    boolean isFull();
    int count();
    String toString();
}

interface Stack<T> extends Collection {
    T top() throws CollectionException;
    void push(T x) throws CollectionException;
    T pop() throws CollectionException;
}

interface Deque<T> extends Collection {
    T front() throws CollectionException;
    T back() throws CollectionException;
    void enqueue(T x) throws CollectionException;
    void enqueueFront(T x) throws CollectionException;
    T dequeue() throws CollectionException;
    T dequeueBack() throws CollectionException;
}

interface Sequence<T> extends Collection {
    static final String ERR_MSG_INDEX = "Wrong index in sequence.";

    T get(int i) throws CollectionException;
    T set(int i, T x) throws CollectionException;
}