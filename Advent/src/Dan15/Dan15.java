package Dan15;

public class Dan15 {
    public static void main(String[] args) {
        Generator genA = new Generator(722L, 16807L, 4);
        Generator genB = new Generator(354L, 48271L, 8);

        long sum = 0;

        for (int i = 0; i < 5000000L; i++) {
            long stA = genA.nextValue();
            long stB = genB.nextValue();

            if ((stA & 0xFFFF) == (stB & 0xFFFF)){
                sum++;
            }
        }

        System.out.println(sum);
    }

}

class Generator {
    private long prev;
    private long factor;
    private int modul;

    public Generator(long prev, long factor, int modul) {
        this.prev = prev;
        this.factor = factor;
        this.modul = modul;
    }

    long nextValue() {
        while (true) {
            this.prev = (this.prev * this.factor) % 2147483647L;
            if (this.prev % this.modul == 0)
                break;
        }
        return this.prev;
    }

}
