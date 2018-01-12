public class Naloga3 {
}


class ResizableArray {

    int[] array;
    private int size;

    ResizableArray() {
        this.array = new int[10];
        this.size = 0;
    }

    void add(int x) {
        if (this.isFull()) {
            this.resize();
        }
        this.array[this.size] = x;
        this.size++;

    }

    private boolean isFull() {
        return this.size == this.array.length - 1;
    }

    private void resize() {
        int[] tmp = new int[this.array.length * 2];
        System.arraycopy(this.array, 0, tmp, 0, this.array.length);
        this.array = tmp;
    }

    void fixArray() {
        int[] tmp = new int[this.size];
        System.arraycopy(this.array, 0, tmp, 0, this.size);
        this.array = tmp;
    }
}