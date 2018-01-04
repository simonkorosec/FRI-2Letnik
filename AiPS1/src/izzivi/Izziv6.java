package izzivi;

import java.util.Scanner;

interface Collection {
    String ERR_MSG_EMPTY = "Collection is empty.";
    String ERR_MSG_FULL = "Collection is full.";

    boolean isEmpty();

    boolean isFull();

    int count();

    String toString();
}

interface Sequence<T extends Comparable> extends Collection {
    String ERR_MSG_INDEX = "Wrong index in sequence.";

    T get(int i) throws CollectionException;

    T set(int i, T x) throws CollectionException;

    void insert(int i, T x) throws CollectionException;

    T delete(int i) throws CollectionException;

    void reverse();
}

class CollectionException extends Exception {
    CollectionException(String msg) {
        super(msg);
    }
}

class LinkedSequence<T extends Comparable> implements Sequence {
    private int count;
    private Node first;

    LinkedSequence() {
        this.count = 0;
        //this.first = new Node();
        this.first = null;
    }

    LinkedSequence(LinkedSequence<T> ls) {
        super();
        try {
            for (int i = 0; i < ls.count(); i++) {
                this.insert(0, ls.get(ls.count() - 1 - i));
            }
        } catch (CollectionException e) {
            e.printStackTrace();
        }
    }

    private Node jump(int index) throws CollectionException {
        if (index > this.count) {
            throw new CollectionException(ERR_MSG_INDEX);
        }

        Node p = this.first;

        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p;
    }

    @Override
    public boolean isEmpty() {
        return this.count == 0;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int count() {
        return this.count;
    }

    @Override
    public Comparable get(int i) throws CollectionException {
        return jump(i).value;
    }

    @Override
    public Comparable set(int i, Comparable x) throws CollectionException {
        Node p = jump(i);
        T o = p.value;
        p.value = (T) x;
        return o;
    }

    @Override
    public void insert(int i, Comparable x) throws CollectionException {
        this.count++;

        Node n = new Node();
        n.value = (T) x;

        if (this.first == null && i == 0) {
            this.first = n;
            this.first.next = null;
            this.first.prev = null;
            return;
        } else if (this.first == null) {
            throw new CollectionException(ERR_MSG_INDEX);
        }

        if (i == 0) {
            n.next = this.first;
            this.first.prev = n;
            this.first = n;
        } else {
            Node p = jump(i - 1);
            n.next = p.next;
            n.prev = p;
            p.next.prev = n;
            p.next = n;
        }
    }

    @Override
    public Comparable delete(int i) throws CollectionException {
        T o = null;
        if (i >= this.count) {
            throw new CollectionException(ERR_MSG_INDEX);
        }
        if (i == 0) {
            if (this.first.next != null) {
                this.first.next.prev = null;
                o = this.first.value;
            }
            this.first = this.first.next;
        } else {
            Node p = jump(i);
            p.prev.next = p.next;
            if (p.next != null) {
                p.next.prev = p.prev;
            }
            o = p.value;
        }
        this.count--;
        return o;
    }

    @Override
    public void reverse() {

    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("{");
        if (this.first != null) {
            s.append(this.first.value);

            Node p = this.first.next;
            while (p != null) {
                s.append(", ").append(p.value);
                p = p.next;
            }
        }
        return s.append("}").toString();
    }

    public void swap(int i, int j) throws CollectionException {
        T tmp = (T) this.get(i);
        this.set(i, this.get(j));
        this.set(j, tmp);

    }

    private class Node {
        T value;
        Node next, prev;

        Node() {
            this.value = null;
            this.next = null;
            this.prev = null;
        }
    }
}


public class Izziv6 {

    private static int[] insertionSort(LinkedSequence<Oseba> array, int left, int right) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave
        try {
            for (int i = left + 1; i <= right; i++) {
                int j = i;
                boolean loop = false;

                rez[0] += 2;
                rez[1]++;
                while (j > 0 && array.get(j - 1).compareTo(array.get(j)) > 0) {
                    if (loop) {
                        rez[1]++;
                    }
                    array.swap(j - 1, j);
                    rez[0]++;
                    j--;
                    loop = true;
                }
                if (j > 0 && loop) {
                    rez[1]++;
                }
            }
        } catch (CollectionException e) {
            e.printStackTrace();
        }
        return rez;
    }

    private static int[] quickSort(LinkedSequence<Oseba> array, int left, int right, int preklop) {
        int[] rez = new int[]{0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave

        if (Math.abs(right - left) <= preklop) {
            return insertionSort(array, left, right);
        }

        int[] t = partition(array, left, right);
        int r = t[2];
        rez[0] += t[0];
        rez[1] += t[1];

        t = quickSort(array, left, r - 1, preklop);
        rez[0] += t[0];
        rez[1] += t[1];

        t = quickSort(array, r + 1, right, preklop);
        rez[0] += t[0];
        rez[1] += t[1];

        return rez;
    }

    private static int[] partition(LinkedSequence<Oseba> array, int left, int right) {
        int[] rez = new int[]{0, 0, 0};    // rez[0] -> Premiki  | rez[1] -> Primerjave
        try {
            Oseba p = (Oseba) array.get(left);
            rez[0]++;

            int l = left, r = right + 1;
            while (true) {
                do {
                    l++;
                    rez[1]++;
                } while (l < right && array.get(l).compareTo(p) < 0);
                do {
                    r--;
                    rez[1]++;
                } while (array.get(r).compareTo(p) > 0);
                if (l >= r) {
                    break;
                }
                array.swap(l, r);
                rez[0] += 3;
            }
            array.swap(left, r);
            rez[0] += 3;
            rez[2] = r;

        } catch (CollectionException e) {
            e.printStackTrace();
        }

        return rez;
    }

    public static void main(String[] args) {
        LinkedSequence<Oseba> z1 = new LinkedSequence<>();
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Vnesite velikost zaporedja: ");
            int n = sc.nextInt();

            for (int i = 0; i < n; i++) {
                z1.insert(0, new Oseba());
            }
            while (true) {
                LinkedSequence<Oseba> z2 = new LinkedSequence<>(z1);

                System.out.print("Vnesite atribut: ");
                Oseba.setAtr(sc.nextInt());
                System.out.print("Vnesite smer urejanja: ");
                Oseba.setSmer(sc.nextInt());

                System.out.println(z2);
                System.out.print("Vnesite velikost za preklop: ");
                int v = sc.nextInt();

                int[] r = quickSort(z2, 0, z2.count() - 1, v);
                System.out.printf("%d %d\n", r[0], r[1]);

                System.out.println(z2);

                System.out.print("Želite ponoviti? Y/N ");
                String o = sc.next();
                if (o.equals("N") || o.equals("n")) {
                    break;
                }

//                System.out.println(z2);
//                System.out.println(z1);
            }
        } catch (CollectionException e) {
            e.printStackTrace();
        }
    }
}