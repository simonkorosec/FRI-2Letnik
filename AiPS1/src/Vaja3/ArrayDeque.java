package Vaja3;


public class ArrayDeque<T> implements Deque<T>, Stack<T>, Sequence<T> {
    private static final int DEFAULT_CAPACITY = 64;

    private T[] elementi;
    private int stElementov;
    private int front;
    private int back;


    ArrayDeque() {
        this.elementi = (T[]) new Object[DEFAULT_CAPACITY];
    }

    @Override
    public boolean isEmpty() {
        return this.stElementov == 0;
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
        if (this.isEmpty())
            throw new CollectionException(ERR_MSG_EMPTY);
        return this.elementi[(this.back - 1 + this.elementi.length) % this.elementi.length];
    }

    @Override
    public void push(T x) throws CollectionException {
        this.enqueue(x);
    }

    @Override
    public T pop() throws CollectionException {
        if (this.isEmpty())
            throw new CollectionException(ERR_MSG_EMPTY);
        T tmp = this.elementi[(this.back - 1 + this.elementi.length) % this.elementi.length];
        this.elementi[(this.back - 1 + this.elementi.length) % this.elementi.length] = null;
        this.back = (this.back - 1 + this.elementi.length) % this.elementi.length;
        this.stElementov--;
        return tmp;
    }

    @Override
    public T front() throws CollectionException {
        if (this.isEmpty())
            throw new CollectionException(ERR_MSG_EMPTY);
        return this.elementi[this.front];
    }

    @Override
    public T back() throws CollectionException {
        if (this.isEmpty())
            throw new CollectionException(ERR_MSG_EMPTY);
        return this.elementi[(this.back - 1 + this.elementi.length) % this.elementi.length];
    }

    @Override
    public void enqueue(T x) throws CollectionException {
        if (this.isFull())
            throw new CollectionException(ERR_MSG_FULL);
        this.elementi[this.back] = x;
        this.stElementov++;
        this.back = (this.back + 1) % this.elementi.length;
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
        if (this.isEmpty())
            throw new CollectionException(ERR_MSG_EMPTY);
        T tmp = this.elementi[this.front];
        this.elementi[this.front] = null;
        this.front = (this.front + 1) % this.elementi.length;
        this.stElementov--;
        return tmp;
    }

    @Override
    public T dequeueBack() throws CollectionException {
        if (this.isEmpty())
            throw new CollectionException(ERR_MSG_EMPTY);
        T tmp = this.elementi[(this.back - 1 + this.elementi.length) % this.elementi.length];
        this.elementi[(this.back - 1 + this.elementi.length) % this.elementi.length] = null;
        this.back = (this.back - 1 + this.elementi.length) % this.elementi.length;
        this.stElementov--;
        return tmp;
    }

    @Override
    public T get(int i) throws CollectionException {
        if (i < 0 || i > this.stElementov)
            throw new CollectionException(ERR_MSG_INDEX);

        return this.elementi[(this.front + i) % this.elementi.length];
    }

    @Override
    public T set(int i, T x) throws CollectionException {
        if (i < 0 || i > this.stElementov)
            throw new CollectionException(ERR_MSG_INDEX);
        if (/*i == this.stElementov ||*/ this.isFull())
            throw new CollectionException(ERR_MSG_FULL);
        if (i == this.stElementov) {
            this.back = (this.back + 1) % this.elementi.length;
            this.stElementov++;
        }
        this.elementi[(this.front + i) % this.elementi.length] = x;


        // Preglej
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        if (this.count() > 0){
            sb.append(this.elementi[this.front]);
            for (int i = 1; i < this.stElementov; i++) {
                sb.append(", ").append(this.elementi[(this.front + i) % this.elementi.length]);
            }
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