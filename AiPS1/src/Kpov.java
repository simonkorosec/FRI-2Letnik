import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Date;

public class Kpov {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("ntp1.arnes.si", 37);
        InputStream vhod = s.getInputStream();
        long st = 0;
        for (int i = 0; i < 4; i++) {
            st = st << 8;
            st += vhod.read();
        }
        Date d = new Date((st - 2208988800L) * 1000);
        System.out.println(d);
   }
}
