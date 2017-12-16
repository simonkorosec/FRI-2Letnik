package Dan14;


public class Dan14 {

    public static void main(String[] args) {

    }

    static private String hash(String s) {

        int [] array = new int[32];
        for (int i = 0; i < s.length(); i++) {

        }

        int[] densHash = new int[16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                densHash[i] ^= array[i * 16 + j];
            }
        }

        for (int aDensHash : densHash) {
            System.out.printf("%02x", aDensHash);
        }
        System.out.println();

        return null;
    }
}


