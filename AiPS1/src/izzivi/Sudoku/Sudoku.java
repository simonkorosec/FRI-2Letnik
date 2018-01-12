package izzivi.Sudoku;


import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Sudoku {

    public static void main(String[] args) {
        String [] in1 = "020 000 010 000 608 000 630 000 089 001 307 900 000 201 000 003 406 700 960 000 025 000 104 000 080 000 040".split(" ");

        String [] in2 = "000 040 000 400 608 003 009 020 100 012 904 360 090 000 040 043 701 520 005 060 400 100 809 005 000 010 000".split(" ");

        //SudokuTable sudokuTable = new SudokuTable(28);
        SudokuTable sudokuTable = new SudokuTable(parseProblem(in2));
        sudokuTable.printTable();
        sudokuTable.solve();
    }

    static int[][] parseProblem(String[] args) {
        int[][] problem = new int[9][9]; // default 0 vals
        int i = 0, j = 0;
        for (int n = 0; n < args.length; ++n) {
            problem[i][j++] = Integer.parseInt(args[n].substring(0,1));
            problem[i][j++] = Integer.parseInt(args[n].substring(1,2));
            problem[i][j++] = Integer.parseInt(args[n].substring(2,3));

            if (j == 9){
                j = 0;
                i++;
            }

        }
        return problem;
    }
}


class SudokuTable {
    private static final String os = System.getProperty("os.name");
    private static final String DASH = "-------------------------";
    private static final String LINE = "--------+-------+--------";
    private int[][] table;

    SudokuTable(int n) {
        this.table = new int[9][9];
        this.randomizeSudoku(n);
    }

    SudokuTable(int[][] t) {
        this.table = t;
    }

    private void randomizeSudoku(int n) {
        for (int i = 0; i < n; i++) {
            int x, y, st;
            do {
                x = ThreadLocalRandom.current().nextInt(0, 9);
                y = ThreadLocalRandom.current().nextInt(0, 9);

            } while (this.table[x][y] != 0);

            do {
                st = ThreadLocalRandom.current().nextInt(1, 10);
            } while (!checkInput(st, x, y));
            this.table[x][y] = st;
        }
    }

    /**
     * Check if number n can be inserted at table[k][h]
     *
     * @param number number to check
     * @param x      first position
     * @param y      second position
     * @return true / false
     */
    private boolean checkInput(int number, int x, int y) {
        if (x > 8 || y > 8 || number < 1 || number > 9) {
            return false;
        }

        for (int i = 0; i < 9; i++) {
            if (this.table[x][i] == number || this.table[i][y] == number) {
                return false;
            }
        }

        int xOff = (x / 3) * 3;
        int yOff = (y / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (number == this.table[xOff + i][yOff + j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public void printTable() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("\n%s\n", DASH));

        for (int i = 0; i < 9; i++) {
            if (i != 0 && i % 3 == 0) {
                stringBuilder.append(LINE).append("\n");
            }
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0) {
                    stringBuilder.append("| ");
                }
                if (this.table[i][j] == 0) {
                    stringBuilder.append(". ");
                } else {
                    stringBuilder.append(String.format("%d ", this.table[i][j]));
                }
            }
            stringBuilder.append("|\n");
        }
        stringBuilder.append(DASH + "\n");

        try {
            Thread.sleep(100);
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        System.out.println(stringBuilder.toString());
    }

    public void solve() {
        if (insertNumber(0, 0)) {
            System.out.println("SOLVABLE");
            this.printTable();
        } else {
            System.out.println("UNSOLVABLE");
        }
    }

    private boolean insertNumber(int x, int y) {
        printTable();

        if (y == 9) {
            y = 0;
            if (++x == 9) {
                return true;
            }
        }

        if (this.table[x][y] != 0) {
            return insertNumber(x, y+1);
        }

        for (int value = 1; value <= 9; value++) {
            if (checkInput(value, x, y)) {
                this.table[x][y] = value;
                if (insertNumber(x, y+1)) {
                    return true;
                }
            }
        }

        this.table[x][y] = 0;
        return false;
    }
}