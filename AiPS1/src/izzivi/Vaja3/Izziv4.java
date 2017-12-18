package izzivi.Vaja3;

public class Izziv4 {

    public static void main(String[] args) {
        //int size = Integer.parseInt(args[0]);
        int size = 10;

        StdDraw.setCanvasSize(1024, 720);
        StdDraw.setXscale(-1, size + 1);
        StdDraw.setYscale(size + 1, -1);

        try {


            Drevo d = new Drevo(size);
            d.drawLevelOrder();
            Thread.sleep(2 * 1000);
            StdDraw.clear();
            d.drawPreorder(0);
            Thread.sleep(2 * 1000);
            StdDraw.clear();
            d.drawPostorder(0);
            Thread.sleep(2 * 1000);
            StdDraw.clear();
            d.drawInorder(0);
        } catch (InterruptedException e) {
            System.out.println("Prišlo je do napake");
            System.exit(1);
        }

    }
}


class Drevo {

    private int [] pozX;
    private int [] pozY;
    private int dX;
    private int dY;
    private int size;

    Drevo(int size) {
        this.pozX = new int[size];
        this.pozY = new int[size];
        this.size = size;
        this.dX = 1;
        this.dY = 2;

        traverse(0, 0, 0);
    }

    private int traverse(int i, int x, int y){

        if (i >= this.size)
            return x;

        x = traverse(2 * i + 1, x, y + this.dY);
        pozX[i] = x;
        pozY[i] = y;

        return traverse(2 * i + 2, x + this.dX, y + this.dY);
    }

    void drawLevelOrder(){
        for (int i = 0; i < this.size; i++) {
            int oce = (i - 1) / 2;
            if (oce >= 0) {
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.line(this.pozX[i], this.pozY[i], this.pozX[oce], this.pozY[oce]);
            }
            StdDraw.setPenColor(StdDraw.BOOK_BLUE);
            StdDraw.filledCircle(this.pozX[i], this.pozY[i], 0.3);
        }
    }

    void drawPreorder(int i){
        if (i >= this.size)
            return;

        int oce = (i - 1) / 2;
        if (oce >= 0) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.line(this.pozX[i], this.pozY[i], this.pozX[oce], this.pozY[oce]);
        }
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        StdDraw.filledCircle(this.pozX[i], this.pozY[i], 0.3);

        drawPreorder(2*i + 1);
        drawPreorder(2*i + 2);
    }

    void drawPostorder(int i) {
        if (i >= this.size)
            return;

        drawPostorder(2*i + 1);
        drawPostorder(2*i + 2);

        int oce = (i - 1) / 2;
        if (oce >= 0) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.line(this.pozX[i], this.pozY[i], this.pozX[oce], this.pozY[oce]);
        }
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        StdDraw.filledCircle(this.pozX[i], this.pozY[i], 0.3);
    }

    void drawInorder(int i) {
        if (i >= this.size)
            return;

        drawInorder(2*i + 1);

        int oce = (i - 1) / 2;
        if (oce >= 0) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.line(this.pozX[i], this.pozY[i], this.pozX[oce], this.pozY[oce]);
        }
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        StdDraw.filledCircle(this.pozX[i], this.pozY[i], 0.3);

        drawInorder(2*i + 2);
    }

}
