package imenik;

import java.io.*;

interface Seznam<Tip> {
    // Dodajanje elementa v podatkovno strukturo
    void add(Tip e);
    // Odstranjevanje (in vračanje) prvega elementa iz pod. struk.
    Tip removeFirst();
    // Odstranjevanje (in vračanje) poljubnega elementa iz pod. struk.
    Tip remove(Tip e);
    // Vračanje prvega elementa iz pod. struk.
    Tip getFirst();
    // Ali je element prisoten v strukturi
    boolean exists(Tip e);
    // Število elementov v podatkovni strukturi
    int size();
    // Globina podatkovne strukture
    int depth();
    // Ali je podakovna struktura prazna
    boolean isEmpty();
    void print();
    void save(OutputStream outputStream) throws IOException;
    void restore(InputStream inputStream) throws IOException,
            ClassNotFoundException;
}
